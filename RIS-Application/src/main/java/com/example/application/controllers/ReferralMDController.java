package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

import com.example.application.persistence.DiagnosticReport;
import com.example.application.persistence.FileUpload;
import com.example.application.persistence.Modality;
import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.persistence.PatientAlertsList;
import com.example.application.persistence.PatientsAlerts;
import com.example.application.persistence.User;
import com.example.application.repositories.AlertRepository;
import com.example.application.repositories.DiagnosticRepository;
import com.example.application.repositories.FileUploadRepository;
import com.example.application.repositories.ModalityRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.repositories.PatientsAlertsRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.security.AppUserDetails;

@Controller 
@RequestMapping(path="/referral") // This means URL's start with /demo (after Application path)
public class ReferralMDController {
    
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private DiagnosticRepository diagnosticRepository;
    @Autowired
    private ModalityRepository modalityRepository;
    @Autowired
    private PatientsAlertsRepository patientsAlertsRepository;
    @Autowired
    private AlertRepository alertRepository;

    @GetMapping("")
    public String getIndex(Model model)
    {

        Iterable<Patient> patients_list = patientRepository.findAll();
        for(Patient patient : patients_list)
        {
            patient.setAlerts(patientsAlertsRepository.findByPatient(patient.getId()));
        }

        model.addAttribute("patient", new Patient());
        model.addAttribute("patient_list", patients_list);
        model.addAttribute("patient_alerts_list", alertRepository.findAll());
        return "referral_index";
    }

    @PostMapping("/updatePatient")
    public String updatePatient(@ModelAttribute("patient") Patient patient, @ModelAttribute("patients_alerts") PatientAlertsList patients_alerts, Model model, BindingResult result)
    {
        Long patient_id;

        if(patient.getId() == null)     //  Create new patient
        {
            patient_id = patientRepository.save(patient).getId();
    
            for(PatientsAlerts alert : patients_alerts.getPatients_alerts())
            {
                alert.setPatient(patient_id);
                patientsAlertsRepository.save(alert);
            }
            
        }
        else                            //  Use existing patient with patient_id
        {
            patient_id = patient.getId();
        }

        return "redirect:/referral/neworder/" + patient_id;
    }

    @GetMapping("/neworder/{patient_id}")
    public String getNewOrder(Model model, @PathVariable("patient_id") Long patient_id, Principal principal)
    {
        
        Optional<Patient> find_patient = patientRepository.findById(patient_id);
        if(find_patient.isPresent())
        {
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            
            Patient patient = find_patient.get();
            Order order = new Order();
            order.setPatient(patient_id);
            order.setReferral_md(((AppUserDetails)loggedInUser.getPrincipal()).getUserId());

            patient.setAlerts(patientsAlertsRepository.findByPatient(patient.getId()));

            model.addAttribute("patient", patient);
            model.addAttribute("order", order);
            model.addAttribute("patient_alerts_list", alertRepository.findAll());
            model.addAttribute("modalities_list", modalityRepository.findAll());
        }
        return "order_form";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@ModelAttribute("order") Order order, Model model, BindingResult result)
    {

        order.setStatus(Long.valueOf(1));
        orderRepository.save(order);
        

        return "redirect:/home";
    }

    @GetMapping("/order/{order_id}")
    public String getOrder(Model model, @PathVariable("order_id") Long order_id, Principal principal)
    {
        Optional<Order> find_order = orderRepository.findById(order_id);
        if(find_order.isPresent())
        {
            Order order = find_order.get();

            Optional<Modality> find_modality = modalityRepository.findById(order.getModality());
            if(find_modality.isPresent())
            {
                Modality modality = find_modality.get();

                order.setModalityObject(modality);
            }

            model.addAttribute("order", order);

            Optional<Patient> find_patient = patientRepository.findById(order.getPatient());
            if(find_patient.isPresent())
            {
                Patient patient = find_patient.get();
                patient.setAlerts(patientsAlertsRepository.findByPatient(patient.getId()));

                model.addAttribute("patient", patient);
            }

            Optional<User> find_referralMD = userRepository.findById(order.getReferral_md());
            if(find_referralMD.isPresent())
            {
                User referralMD = find_referralMD.get();

                model.addAttribute("referral_md", referralMD);
            }

            Iterable<FileUpload> file_uploads_list = fileUploadRepository.getAllFileUploadsByOrderId(order.getId());

            model.addAttribute("file_uploads_list", file_uploads_list);

            DiagnosticReport diagnostics = diagnosticRepository.getDiagnosticReportByOrderId(order.getId());
            if(diagnostics != null)
            {
                model.addAttribute("diagnostics", diagnostics);
            }


            model.addAttribute("patient_alerts_list", alertRepository.findAll());
        }

        return "order_overview";
    }

}