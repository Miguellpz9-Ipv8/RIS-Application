package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "diagnostic_reports")
public class DiagnosticReport {
    @Id
    @Column(name = "diagnostic_report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long order;
    private Long patient;
    private Long radiologist;
    private String diagnostic;

    @Transient
    private User radiologistObject;

    //  GETTERS

    public Long getOrder() {
        return this.order;
    }

    public Long getId() {
        return this.id;
    }

    public Long getPatient() {
        return this.patient;
    }

    public Long getRadiologist() {
        return this.radiologist;
    }

    public String getDiagnostic() {
        return this.diagnostic;
    }

    public User getRadiologistObject(){
        return this.radiologistObject;
    }

    //  SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
    
    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public void setRadiologist(Long radiologist) {
        this.radiologist = radiologist;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public void setRadiologistObject(User radiologistObject){
        this.radiologistObject = radiologistObject;
    }

}