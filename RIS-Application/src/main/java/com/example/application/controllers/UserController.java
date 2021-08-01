package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.example.application.fileservice.StorageService;
import com.example.application.persistence.Appointment;
import com.example.application.persistence.DiagnosticReport;
import com.example.application.persistence.FileUpload;
import com.example.application.persistence.Modality;
import com.example.application.persistence.Order;
import com.example.application.persistence.OrderStatus;
import com.example.application.persistence.Patient;
import com.example.application.persistence.Role;
import com.example.application.persistence.User;
import com.example.application.repositories.AlertRepository;
import com.example.application.repositories.AppointmentRepository;
import com.example.application.repositories.DiagnosticRepository;
import com.example.application.repositories.FileUploadRepository;
import com.example.application.repositories.ModalityRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.OrderStatusRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.repositories.PatientsAlertsRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.security.AppUserDetails;

@Controller 
public class UserController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ModalityRepository modalityRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private DiagnosticRepository diagnosticRepository;
    @Autowired
    private PatientsAlertsRepository patientsAlertsRepository;
    @Autowired
    private AlertRepository alertRepository;
    
    private StorageService storageService;

    public UserController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @GetMapping("/home")
    public String homeView(HttpSession session, Model model)
    {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        //////////////////////////////      REFERRAL MD DASHBOARD       //////////////////////////////
        
        Iterable<Order> order_list = orderRepository.findAllOrdersByReferralmd(((AppUserDetails) loggedInUser.getPrincipal()).getUserId());
        ArrayList<Order> placed_orders_list = new ArrayList<Order>();

        for(Order order : order_list)
        {
            Optional<Patient> find_patient = patientRepository.findById(order.getPatient());
            if(find_patient.isPresent())
            {
                order.setPatientObject(find_patient.get());
            }
            
            Optional<User> find_referral_md = userRepository.findById(order.getReferral_md());
            if(find_referral_md.isPresent())
            {
                order.setReferralMDObject(find_referral_md.get());
            }
            
            Optional<Modality> find_modality = modalityRepository.findById(order.getModality());
            if(find_modality.isPresent())
            {
                order.setModalityObject(find_modality.get());
            }
            
            Optional<OrderStatus> find_order_status = orderStatusRepository.findById(order.getStatus());
            if(find_order_status.isPresent())
            {
                order.setStatusObject(find_order_status.get());
            }

            placed_orders_list.add(order);

        }

        model.addAttribute("placed_orders_list", placed_orders_list);

        //////////////////////////////      RECEPTIONIST DASHBOARD      //////////////////////////////

        Iterable<Appointment> appointments_list = appointmentRepository.findAll();

        //  Find checked-in/closed appointments

        ArrayList<Appointment> checked_in_appointments_list = new ArrayList<Appointment>();

        for(Appointment appointment : appointments_list)
        {
            if(appointment.getCheckedin() == 1)
            {
                Optional<Patient> find_patient = patientRepository.findById(appointment.getPatient());
                if(find_patient.isPresent())
                {
                    appointment.setPatientObject(find_patient.get());
                }
                
                Optional<Modality> find_modality = modalityRepository.findById(appointment.getModality());
                if(find_modality.isPresent())
                {
                    appointment.setModalityObject(find_modality.get());
                }
                
                Optional<User> find_radiologist = userRepository.findById(appointment.getRadiologist());
                if(find_radiologist.isPresent())
                {
                    appointment.setRadiologistObject(find_radiologist.get());
                }
                
                Optional<User> find_technician = userRepository.findById(appointment.getTechnician());
                if(find_technician.isPresent())
                {
                    appointment.setTechnicianObject(find_technician.get());
                }
                
                checked_in_appointments_list.add(appointment);
            }
        }

        //  Find today's appointments

        ArrayList<Appointment> todays_appointments_list = new ArrayList<Appointment>();

        for(Appointment appointment : appointments_list) 
        {
            String datetime = appointment.getDatetime();
            String date = datetime.split(" ")[0];
            String today = LocalDate.now().toString();

            if(date.equals(today) && appointment.getCheckedin() != 1)
            {
                Optional<Patient> find_patient = patientRepository.findById(appointment.getPatient());
                if(find_patient.isPresent())
                {
                    appointment.setPatientObject(find_patient.get());
                }
                
                Optional<Modality> find_modality = modalityRepository.findById(appointment.getModality());
                if(find_modality.isPresent())
                {
                    appointment.setModalityObject(find_modality.get());
                }
                
                Optional<User> find_radiologist = userRepository.findById(appointment.getRadiologist());
                if(find_radiologist.isPresent())
                {
                    appointment.setRadiologistObject(find_radiologist.get());
                }
                
                Optional<User> find_technician = userRepository.findById(appointment.getTechnician());
                if(find_technician.isPresent())
                {
                    appointment.setTechnicianObject(find_technician.get());
                }

                todays_appointments_list.add(appointment);
            }
        }

        
        model.addAttribute("appointments_list", appointments_list);
        model.addAttribute("todays_appointments_list", todays_appointments_list);
        model.addAttribute("checked_in_appointments_list", checked_in_appointments_list);
        model.addAttribute("checkin_appointment", new Appointment());


        //  Create all timeslots

        ArrayList<String> times_list = new ArrayList<String>();
        int[] times = {8, 9, 10, 11, 12, 1, 2, 3, 4};
        for(int time : times)
        {
            if(time >= 8 && time < 12)
            {
                times_list.add(time + ":00am");
                times_list.add(time + ":30am");
            }
            else
            {
                times_list.add(time + ":00pm");
                times_list.add(time + ":30pm");
            }
        }

        //  Find doctors and technicians

        Iterable<User> users = userRepository.findAll();
        LinkedList<User> radiologists = new LinkedList<User>();
        LinkedList<User> technicians = new LinkedList<User>();

        for(User user : users)
        {
            for(Role role : user.getRoles())
            {
                if(role.getRole_id() == 10)          //  Is user a Radiologist
                {
                    radiologists.add(user);
                }
                if(role.getRole_id() == 9)          //  Is user a Technician
                {
                    technicians.add(user);
                }
            }
        }

        //  Find all orders without appointments and add them to an OrdersDTO list (for patient and referral md info)

        order_list = orderRepository.findAll();
        LinkedList<Order> unscheduled_orders_list = new LinkedList<Order>();

        for(Order order : order_list)
        {
            if(order.getAppointment() == null || order.getAppointment() <= 0)
            {
                Optional<Patient> find_patient = patientRepository.findById(order.getPatient());
                if(find_patient.isPresent())
                {
                    order.setPatientObject(find_patient.get());
                }
                
                Optional<User> find_referral_md = userRepository.findById(order.getReferral_md());
                if(find_referral_md.isPresent())
                {
                    order.setReferralMDObject(find_referral_md.get());
                }
                
                Optional<Modality> find_modality = modalityRepository.findById(order.getModality());
                if(find_modality.isPresent())
                {
                    order.setModalityObject(find_modality.get());
                }
            
                Optional<OrderStatus> find_order_status = orderStatusRepository.findById(order.getStatus());
                if(find_order_status.isPresent())
                {
                    order.setStatusObject(find_order_status.get());
                }

                unscheduled_orders_list.add(order);
            }
        }

        model.addAttribute("times_list", times_list);
        model.addAttribute("radiologists_list", radiologists);
        model.addAttribute("technicians_list", technicians);
        model.addAttribute("unscheduled_orders_list", unscheduled_orders_list);
        model.addAttribute("appointment", new Appointment());
        System.out.println(order_list.iterator().hasNext());

        //////////////////////////////      RADIOLOGIST DASHBOARD   //////////////////////////////

        order_list = orderRepository.findAll();
        ArrayList<Order> complete_imaging_orders_list = new ArrayList<Order>();

        for(Order order : order_list)
        {
            if(order.getStatus() == Long.valueOf(2))
            {
                Optional<Patient> find_patient = patientRepository.findById(order.getPatient());
                if(find_patient.isPresent())
                {
                    order.setPatientObject(find_patient.get());
                }
                
                Optional<User> find_referral_md = userRepository.findById(order.getReferral_md());
                if(find_referral_md.isPresent())
                {
                    order.setReferralMDObject(find_referral_md.get());
                }
                
                Optional<Modality> find_modality = modalityRepository.findById(order.getModality());
                if(find_modality.isPresent())
                {
                    order.setModalityObject(find_modality.get());
                }
                
                Optional<OrderStatus> find_order_status = orderStatusRepository.findById(order.getStatus());
                if(find_order_status.isPresent())
                {
                    order.setStatusObject(find_order_status.get());
                }

                complete_imaging_orders_list.add(order);
            }
        }
        Iterable<Appointment> appointments_list_calendar = appointmentRepository.findAll();
        ArrayList<Appointment> complete_appointments_list = new ArrayList<Appointment>();

        for(Appointment appointment : appointments_list)
        {
            Optional<Patient> find_patient = patientRepository.findById(appointment.getPatient());
            if(find_patient.isPresent())
            {
                appointment.setPatientObject(find_patient.get());
            }
            
            Optional<Modality> find_modality = modalityRepository.findById(appointment.getModality());
            if(find_modality.isPresent())
            {
                appointment.setModalityObject(find_modality.get());
            }
            
            Optional<User> find_radiologist = userRepository.findById(appointment.getRadiologist());
            if(find_radiologist.isPresent())
            {
                appointment.setRadiologistObject(find_radiologist.get());
            }
            
            Optional<User> find_technician = userRepository.findById(appointment.getTechnician());
            if(find_technician.isPresent())
            {
                appointment.setTechnicianObject(find_technician.get());
            }

            complete_appointments_list.add(appointment);
        }

        model.addAttribute("appointments_list", complete_appointments_list);

        model.addAttribute("complete_imaging_orders_list", complete_imaging_orders_list);
        model.addAttribute("modalities_list", modalityRepository.findAll());
        model.addAttribute("modality", new Modality());


        return "home";
    }

    @GetMapping("/appointments")
    public String getAppointments(Model model) 
    {
        Iterable<Appointment> appointments_list = appointmentRepository.findAll();
        ArrayList<Appointment> complete_appointments_list = new ArrayList<Appointment>();

        for(Appointment appointment : appointments_list)
        {
            Optional<Patient> find_patient = patientRepository.findById(appointment.getPatient());
            if(find_patient.isPresent())
            {
                appointment.setPatientObject(find_patient.get());
            }
            
            Optional<Modality> find_modality = modalityRepository.findById(appointment.getModality());
            if(find_modality.isPresent())
            {
                appointment.setModalityObject(find_modality.get());
            }
            
            Optional<User> find_radiologist = userRepository.findById(appointment.getRadiologist());
            if(find_radiologist.isPresent())
            {
                appointment.setRadiologistObject(find_radiologist.get());
            }
            
            Optional<User> find_technician = userRepository.findById(appointment.getTechnician());
            if(find_technician.isPresent())
            {
                appointment.setTechnicianObject(find_technician.get());
            }

            complete_appointments_list.add(appointment);
        }

        model.addAttribute("appointments_list", complete_appointments_list);

        return "appointments";
    }
  

    @GetMapping("/orders")
    public String getOrders(Model model) 
    {
        Iterable<Order> orders = orderRepository.findAll();
        ArrayList<Order> orders_list = new ArrayList<Order>();

        for(Order order : orders)
        {
            Optional<Patient> find_patient = patientRepository.findById(order.getPatient());
            if(find_patient.isPresent())
            {
                order.setPatientObject(find_patient.get());
            }
            
            Optional<User> find_referral_md = userRepository.findById(order.getReferral_md());
            if(find_referral_md.isPresent())
            {
                order.setReferralMDObject(find_referral_md.get());
            }
            
            Optional<Modality> find_modality = modalityRepository.findById(order.getModality());
            if(find_modality.isPresent())
            {
                order.setModalityObject(find_modality.get());
            }
            
            Optional<OrderStatus> find_order_status = orderStatusRepository.findById(order.getStatus());
            if(find_order_status.isPresent())
            {
                order.setStatusObject(find_order_status.get());
            }

            orders_list.add(order);

        }

        model.addAttribute("orders_list", orders_list);

        return "orders";
    }
    @GetMapping("/modality")
    public String getOrder(Model model) 
    {
        

        return "modality";
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

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/login")
    public String getUserLoginPage(Model model) {
        if (isAuthenticated()) {
            return "redirect:home";
        }
        //System.out.println("Attempted a log in" + model);
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/403")
    public String accessDenied()
    {
        return "access_denied";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}


// NEW STUFF