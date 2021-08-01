package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "imaging_info")
public class ImagingInfo {
    @Id
    @Column(name = "imaging_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long imaging;
    private Long patient;
    private Long modality;
    private Long technician;
    @Column(name = "image_url")
    private String image;
    private String notes;

    public void setImaging(Long imaging) {
        this.imaging = imaging;
    }

    public Long getImaging() {
        return this.imaging;
    }

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public Long getPatient() {
        return this.patient;
    }

    public void setModality(Long modality) {
        this.modality = modality;
    }

    public Long getModality() {
        return this.modality;
    }

    public void setTechnician(Long technician) {
        this.technician = technician;
    }

    public Long getTechnicain() {
        return this.technician;
    }
    
    public void setImage(String image) {
        this.image = image;
    }

    public String getimage() {
        return this.image;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return this.notes;
    }

    

}