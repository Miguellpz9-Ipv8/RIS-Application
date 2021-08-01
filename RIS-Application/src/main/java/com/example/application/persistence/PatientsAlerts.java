package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "patients_alerts")
public class PatientsAlerts {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private Long patient;
    private Long alert_id;
    private Long alert_value;


    public Long getPatient() {
        return this.patient;
    }
     
    public Long getAlert_id(){
        return this.alert_id;
    } 

    public Long getAlert_value(){
        return this.alert_value;
    }
    
    public void setPatient(Long patient){
        this.patient = patient;
    }

    public void setAlert_id(Long alert_id){
        this.alert_id = alert_id;
    }

    public void setAlert_value(Long alert_value){
        this.alert_value = alert_value;
    }


}
