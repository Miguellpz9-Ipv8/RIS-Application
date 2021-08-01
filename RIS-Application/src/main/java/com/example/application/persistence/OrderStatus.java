package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @Column(name = "order_status_id")
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
