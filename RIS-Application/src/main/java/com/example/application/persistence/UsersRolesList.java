package com.example.application.persistence;

import java.util.List;

public class UsersRolesList {
    private List<UsersRoles> users_roles;

    public UsersRolesList(){

    }

    public List<UsersRoles> getUsers_roles(){
        return this.users_roles;
    }

    public void setUsers_roles(List<UsersRoles> users_roles){
        this.users_roles = users_roles;
    }
    
}
