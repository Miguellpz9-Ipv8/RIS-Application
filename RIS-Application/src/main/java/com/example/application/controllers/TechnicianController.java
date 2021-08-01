package com.example.application.controllers;

import java.util.Optional;

import com.example.application.fileservice.StorageService;
import com.example.application.persistence.FileUpload;
import com.example.application.persistence.Modality;
import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.repositories.AlertRepository;
import com.example.application.repositories.FileUploadRepository;
import com.example.application.repositories.ModalityRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.repositories.PatientsAlertsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(path="/imaging") // This means URL's start with /admin (after Application path)
public class TechnicianController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private ModalityRepository modalityRepository;
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private PatientsAlertsRepository patientsAlertsRepository;

    private StorageService storageService;

    public TechnicianController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/order/{order_id}")
    public String completeOrder(@PathVariable Long order_id, Model model)
    {
        Optional<Order> get_order = orderRepository.findById(order_id);
        if(get_order.isPresent())
        {
            Optional<Patient> get_patient = patientRepository.findById(get_order.get().getPatient());
            if(get_patient.isPresent())
            {
                Order order = get_order.get();
                
                Optional<Modality> find_modality = modalityRepository.findById(order.getModality());
                if(find_modality.isPresent())
                {
                    Modality modality = find_modality.get();

                    order.setModalityObject(modality);
                }


                Patient patient = get_patient.get();
                patient.setAlerts(patientsAlertsRepository.findByPatient(patient.getId()));

                model.addAttribute("order", order);
                model.addAttribute("patient", patient);
                model.addAttribute("file_uploads_list", fileUploadRepository.getAllFileUploadsByOrderId(order.getId()));
                model.addAttribute("patient_alerts_list", alertRepository.findAll());
                return "imaging_form";
            }
        }
        return "redirect:/home";
    }
    
    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file, @ModelAttribute("order") Order order) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String filetype = filename.substring(filename.lastIndexOf(".") + 1);

        FileUpload fileUpload = new FileUpload();
        fileUpload.setFilename(filename);
        fileUpload.setFiletype(file.getContentType());
        fileUpload.setActive(1);
        fileUpload.setOrder(order.getId());

        fileUpload = fileUploadRepository.save(fileUpload);
        fileUpload.setFilename("RIS_" + fileUpload.getId() + "." + filetype);

        fileUpload = fileUploadRepository.save(fileUpload);

        storageService.store(file, fileUpload.getFilename());

        return "redirect:/imaging/order/" + order.getId();
    }

    @PostMapping("completeOrder")
    public String completeOrder(@ModelAttribute("order") Order order){
        
        orderRepository.setStatusForOrder(Long.valueOf(2), order.getId());
        return "redirect:/home";
    }

}