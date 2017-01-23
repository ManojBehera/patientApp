package com.example.imac.chs_pharmacy.PatientClass;

/**
 * Created by brittanybarnes1 on 1/22/17.
 */

//this is the model to hold the patient data

public class Patient {

    //private variables
    public int patient_id;
    public String patient_name;
    public int prescription_id;
    public String address;
    public String city;
    public String state;

    //create the constructor
    public Patient(int startPatientID, String startPatientName, int startPrescriptionID, String startAddress, String startCity, String startState) {

        patient_name = startPatientName;
        patient_id = startPatientID;
        prescription_id = startPrescriptionID;
        address = startAddress;
        city = startCity;
        state = startState;

    }

    //need to create methods to set the patient information
    public void setPatient_id( int patientID ){
        patient_id = patientID;
    }

    public void setPatient_name( String patientName ){
        patient_name = patientName;
    }

    public void setPrescription_id( int prescriptionName ){
        prescription_id = prescriptionName;
    }
}