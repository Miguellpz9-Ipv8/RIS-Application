package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import com.example.application.persistence.Order;
import com.example.application.persistence.OrderStatus;
import com.example.application.persistence.Patient;
import com.example.application.persistence.Alert;
import com.example.application.persistence.PatientAlertsList;
import com.example.application.persistence.PatientsAlerts;
import com.example.application.persistence.Role;
import com.example.application.persistence.User;
import com.example.application.persistence.UsersRoles;
import com.example.application.persistence.UsersRolesList;
import com.example.application.fileservice.StorageService;
import com.example.application.persistence.Appointment;
import com.example.application.persistence.DiagnosticReport;
import com.example.application.persistence.FileUpload;
import com.example.application.persistence.Modality;
import com.example.application.repositories.AppointmentRepository;
import com.example.application.repositories.DiagnosticRepository;
import com.example.application.repositories.FileUploadRepository;
import com.example.application.repositories.ModalityRepository;
import com.example.application.repositories.OrderRepository;
import com.example.application.repositories.OrderStatusRepository;
import com.example.application.repositories.AlertRepository;
import com.example.application.repositories.PatientRepository;
import com.example.application.repositories.PatientsAlertsRepository;
import com.example.application.repositories.RoleRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.repositories.UsersRolesRepository;

@Controller 
@RequestMapping(path="/admin") // This means URL's start with /admin (after Application path)
public class AdminController {
    @Autowired 
    private UserRepository userRepository;
    @Autowired 
    private RoleRepository roleRepository;
    @Autowired
    private UsersRolesRepository usersRolesRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModalityRepository modalityRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private DiagnosticRepository diagnosticRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private PatientsAlertsRepository patientsAlertsRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    private StorageService storageService;

    public AdminController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/dashboard")
    public String homeView(HttpSession session, Model model)
    {
        UsersRolesList rolesList = new UsersRolesList();

        ArrayList<User> referralMDList = new ArrayList<User>();
        Iterable<User> userList = userRepository.findAll();
        for(User user : userList)
        {
            Set<Role> userRolesList = user.getRoles();
            for(Role role : userRolesList)
            {
                if(role.getRole_id() == Integer.valueOf(8))
                {
                    referralMDList.add(user);
                }
            }
        }
        
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

        Iterable<DiagnosticReport> diagnosticReportList = diagnosticRepository.findAll();
        for(DiagnosticReport diagnosticReport : diagnosticReportList)
        {
            
            Optional<User> find_radiologist = userRepository.findById(diagnosticReport.getRadiologist());
            if(find_radiologist.isPresent())
            {
                diagnosticReport.setRadiologistObject(find_radiologist.get());
            }
        }

        ArrayList<User> radiologistList = new ArrayList<User>();
        ArrayList<User> technicianList = new ArrayList<User>();
        userList = userRepository.findAll();
        for(User user : userList)
        {
            Set<Role> userRolesList = user.getRoles();
            for(Role role : userRolesList)
            {
                if(role.getRole_id() == Integer.valueOf(10))
                {
                    radiologistList.add(user);
                }
                if(role.getRole_id() == Integer.valueOf(9))
                {
                    technicianList.add(user);
                }
            }
        }

        ArrayList<Order> orders_without_report_list = (ArrayList<Order>)orderRepository.findAll();
        ArrayList<Order> orderList = new ArrayList<Order>();
        Iterable<DiagnosticReport> diagnosticReportsList = diagnosticRepository.findAll();
        for(DiagnosticReport diagnosticReport : diagnosticReportsList)
        {
            for(Order order : orders_without_report_list)
            {
                if(diagnosticReport.getOrder() == order.getId())
                {
                    orderList.add(order);
                }
            }
        }
        orders_without_report_list.removeAll(orderList);

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

            String[] date_time = appointment.getDatetime().split(" ");
            if(date_time.length == 2)
            {
                appointment.setDate(date_time[0]);
                String time;
                if(date_time[1].charAt(0) == '0')
                {
                    time = date_time[1].substring(1, date_time[1].length() - 3);
                }
                else
                {
                    time = date_time[1].substring(0, date_time[1].length() - 3);
                }
                if(time.substring(0, 2).equals("8:") || time.substring(0, 2).equals("9:") || time.substring(0, 2).equals("10") || time.substring(0, 2).equals("11"))
                    time += "am";
                else
                    time += "pm";
                appointment.setTime(time);
            }

            complete_appointments_list.add(appointment);
        }

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

        Iterable<Order> order_list = orderRepository.findAll();
        ArrayList<Order> unscheduled_orders_list = new ArrayList<Order>();

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

        Iterable<Patient> patients_list = patientRepository.findAll();
        for(Patient patient : patients_list)
        {
            patient.setAlerts(patientsAlertsRepository.findByPatient(patient.getId()));
        }


        model.addAttribute("roles", rolesList);
        model.addAttribute("users_list", userRepository.findAll());
        model.addAttribute("roles_list", roleRepository.findAll());
        model.addAttribute("patients_list", patientRepository.findAll());
        model.addAttribute("orders_list", orders_list);
        model.addAttribute("user", new User());
        model.addAttribute("patient", new Patient());
        model.addAttribute("order", new Order());
        model.addAttribute("referral_mds_list", referralMDList);
        model.addAttribute("modalities_list", modalityRepository.findAll());
        model.addAttribute("order_status_list", orderStatusRepository.findAll());
        model.addAttribute("file_upload", new FileUpload());
        model.addAttribute("file_uploads_list", fileUploadRepository.findAll());
        model.addAttribute("diagnostic_report", new DiagnosticReport());
        model.addAttribute("diagnostic_reports_list", diagnosticRepository.findAll());
        model.addAttribute("radiologists_list", radiologistList);
        model.addAttribute("orders_without_report_list", orders_without_report_list);
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("appointments_list", complete_appointments_list);
        model.addAttribute("unscheduled_orders_list", unscheduled_orders_list);
        model.addAttribute("times_list", times_list);
        model.addAttribute("technicians_list", technicianList);
        model.addAttribute("modality", new Modality());
        model.addAttribute("alert", new Alert());
        model.addAttribute("patient_alerts_list", alertRepository.findAll());
        
        return "admin_dashboard";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user, @ModelAttribute("roles") UsersRolesList users_roles, Model model, BindingResult result)
    {

        if(user.getUser_id() == null)       //  Create new user if user_id == null
        {
            userRepository.save(user);
        } 
        else                                //  Update use on user_id
        {
            Optional<User> find_user = userRepository.findById(user.getUser_id());
            if(find_user.isPresent())       //Find current user
            {
                if(user.getPassword().isEmpty())        //See if we are changing the password
                {
                    user.setPassword(find_user.get().getPassword());       //If not, use old password
                }
                else
                {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));       //If so, encode new password
                }
            }
    
    
            usersRolesRepository.deleteByUserid(user.getUser_id());       //Delete old user roles
    
            userRepository.save(user);      //Save user first
    

            if(users_roles.getUsers_roles() == null)      // If no role is passed, create a default user (2) role
            {
                UsersRoles default_user = new UsersRoles();
                default_user.setUserid(user.getUser_id());
                default_user.setRole_id(Long.valueOf(2));
                ArrayList<UsersRoles> usersRoleList = new ArrayList<UsersRoles>();
                users_roles.setUsers_roles(usersRoleList);
            }
    
    
            for(UsersRoles role : users_roles.getUsers_roles())
            {
                if(role.getRole_id() != null)
                {
                    role.setUserid(user.getUser_id());
                    usersRolesRepository.save(role);      //Save lise of roles
                }
            }
        }
 
        return "redirect:dashboard";
    }

    @PostMapping("/updateModality")
    public String updateModality(@ModelAttribute("modality") Modality modality, Model model, BindingResult result)
    {
        modalityRepository.save(modality);

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/updatePatientAlert")
    public String updatePatientAlert(@ModelAttribute("alert") Alert alert, Model model, BindingResult result)
    {
        System.out.println(alert.getName());
        alertRepository.save(alert);

        return "redirect:/admin/dashboard";
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

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/updateOrder")
    public String updateOrder(@ModelAttribute("order") Order order, Model model, BindingResult result)
    {
        orderRepository.save(order);

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/updateAppointment")
    public String updateAppointment(@ModelAttribute("appointment") Appointment appointment, Model model, BindingResult result)
    {

        appointment.setDatetime(appointment.getDate() + " " + appointment.getTime().substring(0, appointment.getTime().length() - 2));      //  Cut of the -am or -pm
        Optional<Order> findOrder = orderRepository.findById(appointment.getOrder());
        if(findOrder.isPresent())
        {
            appointment.setPatient(findOrder.get().getPatient());
            appointment.setModality(findOrder.get().getModality());

            for(Order order : orderRepository.findAll())
            {
                if(order.getAppointment() == appointment.getId())
                    order.setAppointment(null);
            }

            findOrder.get().setAppointment(appointment.getId());
        }


        Appointment newAppointment = appointmentRepository.save(appointment);
        orderRepository.setAppointmentForOrder(newAppointment.getId(), newAppointment.getOrder());

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/updateFileUpload")
    public String updateFileUpload(@ModelAttribute("file_upload") FileUpload fileUpload, Model model, BindingResult result)
    {
        Optional<FileUpload> findFileUpload = fileUploadRepository.findById(fileUpload.getId());
        if(findFileUpload.isPresent())
        {
            FileUpload newFileUpload = findFileUpload.get();
            newFileUpload.setOrder(fileUpload.getOrder());
        
            fileUploadRepository.save(newFileUpload);
        }

        return "redirect:/admin/dashboard";
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

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/updateDiagnosticReport")
    public String updateDiagnosticReport(@ModelAttribute("diagnostic_report") DiagnosticReport diagnosticReport, Model model, BindingResult result)
    {
        diagnosticRepository.save(diagnosticReport);

        return "redirect:/admin/dashboard";
    }


}