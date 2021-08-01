package com.example.application.controllers;

import java.security.Principal;
import java.util.Optional;

import com.example.application.persistence.DiagnosticReport;
import com.example.application.persistence.Modality;
import com.example.application.persistence.Order;
import com.example.application.persistence.Patient;
import com.example.application.repositories.AlertRepository;
import com.example.application.repositories.AppointmentRepository;
import com.example.application.repositories.DiagnosticRepository;
import com.example.application.repositories.FileUploadRepository;
import com.example.application.repositories.ModalityRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.repositories.PatientsAlertsRepository;
import com.example.application.security.AppUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller 
@RequestMapping(path="/diagnostics") // This means URL's start with /demo (after Application path)
public class RadiologistController {
    
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private DiagnosticRepository diagnosticRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ModalityRepository modalityRepository;
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private PatientsAlertsRepository patientsAlertsRepository;

    @GetMapping("/order/{order_id}")
    public String orderView(Model model, @PathVariable("order_id") Long order_id, Principal principal)
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
                DiagnosticReport diagnosticReport = new DiagnosticReport();
                diagnosticReport.setOrder(order.getId());
                diagnosticReport.setPatient(patient.getId());
                model.addAttribute("diagnostics", diagnosticReport);
                return "diagnostics_form";
            }
        }
        return "redirect:/home";
    }

    @PostMapping("/submitReport")
    public String submitReport(@ModelAttribute("patient") Patient patient, @ModelAttribute("order") Order order, @ModelAttribute("diagnostics") DiagnosticReport diagnosticReport, Model model)
    {      
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        diagnosticReport.setPatient(patient.getId());
        diagnosticReport.setOrder(order.getId());
        diagnosticReport.setRadiologist(((AppUserDetails) loggedInUser.getPrincipal()).getUserId());
  
        orderRepository.setStatusForOrder(Long.valueOf(3), order.getId());
        appointmentRepository.setClosedForAppointment(order.getAppointment());
        
        diagnosticRepository.deleteByOrder(order.getId());
        diagnosticRepository.save(diagnosticReport);

        return "redirect:/home";
    }
}