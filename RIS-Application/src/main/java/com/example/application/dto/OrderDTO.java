package com.example.application.dto;

import com.example.application.persistence.Patient;
import com.example.application.persistence.User;

public class OrderDTO {
    private Long id;

    private Long patient;
    private Long modality;
    private Long referral_md;
    private String notes;
    private Long status;
    private Long report;
    private Long appointment;

    private Patient patientObject;
    private User referral_mdObject;
    

    public OrderDTO(
        Long id,
        Patient patientObject,
        User referral_mdObject,
        Long modality,
        String notes,
        Long status,
        Long report,
        Long appointment)
    {
        this.id = id;
        this.patientObject = patientObject;
        this.patient = patientObject.getId();
        this.referral_mdObject = referral_mdObject;
        this.referral_md = referral_mdObject.getUser_id();
        this.modality = modality;
        this.notes = notes;
        this.status = status;
        this.report = report;
        this.appointment = appointment;
    }


    //      GETTERS

    public Long getId(){
        return this.id;
    }

    public Long getPatient(){
        return this.patient;
    }

    public Long getModality(){
        return this.modality;
    }

    public Long getReferral_md(){
        return this.referral_md;
    }

    public String getNotes(){
        return this.notes;
    }

    public Long getStatus(){
        return this.status;
    }

    public Long getReport(){
        return this.report;
    }

    public Patient getPatientObject(){
        return this.patientObject;
    }

    public User getReferral_mdObject(){
        return this.referral_mdObject;
    }
    
    public Long getAppointment(){
        return this.appointment;
    }


    //      SETTERS

    public void setId(Long id){
        this.id = id;
    }

    public void setPatient(Long patient){
        this.patient = patient;
    }

    public void setModality(Long modality){
        this.modality = modality;
    }

    public void setReferral_md(Long referral_md){
        this.referral_md = referral_md;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public void setStatus(Long status){
        this.status = status;
    }

    public void setReport(Long report){
        this.report = report;
    }

    public void setPatientObject(Patient patientObject){
        this.patientObject = patientObject;
    }

    public void setReferral_mdObject(User referral_mdObject){
        this.referral_mdObject = referral_mdObject;
    }

    public void setAppointment(Long appointment){
        this.appointment = appointment;
    }
}
