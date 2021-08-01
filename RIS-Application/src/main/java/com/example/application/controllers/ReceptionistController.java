package com.example.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.application.persistence.Appointment;
import com.example.application.repositories.AppointmentRepository;
import com.example.application.repositories.OrderRepository;

@Controller 
@RequestMapping(path="/staff") // This means URL's start with /admin (after Application path)
public class ReceptionistController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/updateAppointment")
    public String updateAppointment(@ModelAttribute("appointment") Appointment appointment, Model model, BindingResult result)
    {

        appointment.setDatetime(appointment.getDate() + " " + appointment.getTime().substring(0, appointment.getTime().length() - 2));      //  Cut of the -am or -pm

        Appointment newAppointment = appointmentRepository.save(appointment);
        orderRepository.setAppointmentForOrder(newAppointment.getId(), newAppointment.getOrder());

        return "redirect:/home";
    }

    @PostMapping("/checkinAppointment")
    public String checkinAppointment(@ModelAttribute("checkin_appointment") Appointment appointment, Model model, BindingResult result)
    {
        appointmentRepository.setCheckedInForAppointment(appointment.getId());
        return "redirect:/home";
    }
}