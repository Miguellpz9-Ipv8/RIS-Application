package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "modalities")
public class Modality {
    @Id
    @Column(name = "modality_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;


    public Long getId() {
        return this.id;
    }
     
    public String getName(){
        return this.name;
    }  


    public void setId(Long id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
}
