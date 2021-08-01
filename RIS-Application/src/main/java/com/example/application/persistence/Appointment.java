package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @Column(name = "appointment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patient;
    @Column(name = "order_id")
    private Long order;
    private Long modality;
    @Column(name = "date_time")
    private String datetime;
    private Long radiologist;
    private Long technician;
    @Column(name = "phone_number")
    private String phonenumber;
    @Column(name = "email_address")
    private String emailaddress;
    @Column(name = "checked_in")
    private int checkedin;
    private int closed;

    @Transient
    private String date;
    @Transient
    private String time;
    @Transient
    private Patient patientObject;
    @Transient
    private Modality modalityObject;
    @Transient
    private User radiologistObject;
    @Transient
    private User technicianObject;



    //  GETTERS

    public Long getId(){
        return this.id;
    }

    public Long getPatient() {
        return this.patient;
    }

    public Long getOrder(){
        return this.order;
    }

    public Long getModality() {
        return this.modality;
    }
    
    public String getDatetime() {
        return this.datetime;
    }

    public Long getRadiologist() {
        return this.radiologist;
    }

    public Long getTechnician() {
        return this.technician;
    }

    public String getPhonenumber(){
        return this.phonenumber;
    }

    public String getEmailaddress(){
        return this.emailaddress;
    }

    public String getDate(){
        return this.date;
    }

    public String getTime(){
        return this.time;
    }

    public int getCheckedin(){
        return this.checkedin;
    }

    public int getClosed(){
        return this.closed;
    }

    public Patient getPatientObject(){
        return this.patientObject;
    }

    public Modality getModalityObject(){
        return this.modalityObject;
    }

    public User getRadiologistObject(){
        return this.radiologistObject;
    }

    public User getTechnicianObject(){
        return this.technicianObject;
    }


    //  SETTERS

    public void setId(Long id){
        this.id = id;
    }

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public void setOrder(Long order){
        this.order = order;
    }

    public void setModality(Long modality) {
        this.modality = modality;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setRadiologist(Long radiologist) {
        this.radiologist = radiologist;
    }

    public void setTechnician(Long technician) {
        this.technician = technician;
    }

    public void setPhonenumber(String phonenumber){
        this.phonenumber = phonenumber;
    }

    public void setEmailaddress(String emailaddress){
        this.emailaddress = emailaddress;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setCheckedin(int checkedin){
        this.checkedin = checkedin;
    }

    public void setClosed(int closed){
        this.closed = closed;
    }

    public void setPatientObject(Patient patientObject){
        this.patientObject = patientObject;
    }

    public void setModalityObject(Modality modalityObject){
        this.modalityObject = modalityObject;
    }

    public void setRadiologistObject(User radiologistObject){
        this.radiologistObject = radiologistObject;
    }

    public void setTechnicianObject(User technicianObject){
        this.technicianObject = technicianObject;
    }


    



}