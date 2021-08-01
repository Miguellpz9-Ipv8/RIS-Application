package com.example.application.persistence;

import java.util.List;

public class PatientAlertsList {

    private List<PatientsAlerts> patients_alerts;

    public PatientAlertsList(){

    }

    public List<PatientsAlerts> getPatients_alerts(){
        return this.patients_alerts;
    }

    public void setPatients_alerts(List<PatientsAlerts> patients_alerts){
        this.patients_alerts = patients_alerts;
    }

}
