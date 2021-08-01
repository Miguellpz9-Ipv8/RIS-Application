package com.example.application.persistence;
import javax.persistence.*;
 
@Entity
@Table(name = "users_roles")
public class UsersRoles {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userid;
    private Long role_id;


    public Long getUserid() {
        return this.userid;
    }
     
    public Long getRole_id(){
        return this.role_id;
    } 
    
    public void setUserid(Long user_id){
        this.userid = user_id;
    }

    public void setRole_id(Long role_id){
        this.role_id = role_id;
    }


}
