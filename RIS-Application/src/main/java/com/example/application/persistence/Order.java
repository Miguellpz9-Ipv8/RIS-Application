package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patient;
    private Long modality;
    private Long referral_md;
    private Long appointment;
    private String notes;
    private Long status;
    private Long report;

    @Transient
    Patient patientObject;
    @Transient
    User referral_mdObject;
    @Transient
    Modality modalityObject;
    @Transient
    OrderStatus statusObject;
    


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
    
    public Long getAppointment(){
        return this.appointment;
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

    public User getReferralMDObject(){
        return this.referral_mdObject;
    }

    public Modality getModalityObject(){
        return this.modalityObject;
    }

    public OrderStatus getStatusObject(){
        return this.statusObject;
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

    public void setAppointment(Long appointment){
        this.appointment = appointment;
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

    public void setReferralMDObject(User referralMDObject){
        this.referral_mdObject = referralMDObject;
    }

    public void setModalityObject(Modality modalityObject){
        this.modalityObject = modalityObject;
    }

    public void setStatusObject(OrderStatus statusObject){
        this.statusObject = statusObject;
    }

    



}