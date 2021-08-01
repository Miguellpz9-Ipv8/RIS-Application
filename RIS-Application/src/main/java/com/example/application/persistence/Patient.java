package com.example.application.persistence;


import javax.persistence.*;
 
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    private String first_name;
    private String last_name;
    private String dob;
    private String sex;
    private String race;
    private String ethnicity;

    @Transient
    private Iterable<PatientsAlerts> alerts;

    public Long getId() {
        return this.id;
    }

    public String getFirst_name(){
        return this.first_name;
    }

    public String getLast_name(){
        return this.last_name;
    }

    public String getDob(){
        return this.dob;
    }

    public String getSex(){
        return this.sex;
    }

    public String getRace(){
        return this.race;
    }

    public String getEthnicity(){
        return this.ethnicity;
    }

    public Iterable<PatientsAlerts> getAlerts(){
        return this.alerts;
    }




    public void setId(Long id){
        this.id = id;
    }

    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }

    public void setLast_name(String last_name){
        this.last_name = last_name;
    }

    public void setDob(String dob){
        this.dob = dob;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public void setRace(String race){
        this.race = race;
    }

    public void setEthnicity(String ethnicity){
        this.ethnicity = ethnicity;
    }

    public void setAlerts(Iterable<PatientsAlerts> iterable){
        this.alerts = iterable;
    }


}