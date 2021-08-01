package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer role_id;
     
    private String name;

    @Transient
    private boolean remove;


    public Integer getRole_id() {
        return this.role_id;
    }
     
    public String getName(){
        return this.name;
    }  

    public boolean getRemove(){
        return this.remove;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRemove(boolean remove){
        this.remove = remove;
    }


}
