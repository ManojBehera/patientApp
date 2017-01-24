package com.example.imac.chs_pharmacy.PatientClass;


//this is the model to hold the patient data

import android.app.Application;
import android.util.Log;


public class Patient extends Application {

    //private variables
    public String patient_id;
    public String patient_name;
    public String prescription_id;
    public String address;
    public String city;
    public String state;
    public String quantity;
    public String refills;
    public String zip;
    public String label;
    public String dosage;
    public String ndc;
    private static final String TAG = "Patient";



    //create the constructor
        public Patient(String startPatientName, String startPrescriptionID, String startPatientID, String startQuantity, String startRefills, String startAddress, String startCity, String startState, String startZip, String startLabel, String startDosage, String startNdc) {

            this.patient_name = startPatientName;
            this.patient_id = startPatientID;
            this.prescription_id = startPrescriptionID;
            this.address = startAddress;
            this.city = startCity;
            this.state = startState;
            this.quantity = startQuantity;
            this.refills = startRefills;
            this. zip = startZip;
            this.dosage = startDosage;
            this.ndc = startNdc;
            this.label = startLabel;

             //getting the updated values from pharmacist
             Log.d(TAG, patient_name);

    }

    //need to create methods to set the patient information
    public void setPatient_id( String patientID ){

        this.patient_id = patientID;
    }

    public void setPatient_name( String patientName ){

        this.patient_name = patientName;
    }

    public String getPatientData( ){
        return patient_id;

    }

    public void setPrescription_id( String prescriptionName ){

        this.prescription_id = prescriptionName;

    }

    public void setAddress( String addressName ){

        this.address = addressName;

    }

    public void setCity( String cityName ){

        this.city = cityName;

    }

    public void setState( String stateName ){

        this.state = stateName;

    }

}