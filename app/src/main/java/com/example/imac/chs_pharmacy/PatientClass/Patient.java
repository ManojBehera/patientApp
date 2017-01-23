package com.example.imac.chs_pharmacy.PatientClass;


//this is the model to hold the patient data

import android.util.Log;


public class Patient {

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

             patient_name = startPatientName;
             patient_id = startPatientID;
             prescription_id = startPrescriptionID;
             address = startAddress;
             city = startCity;
             state = startState;
             quantity = startQuantity;
             refills = startRefills;
             zip = startZip;
             dosage = startDosage;
             ndc = startNdc;
             label = startLabel;

             //getting the updated values from pharmacist
             Log.d(TAG, patient_name);

    }

    //need to create methods to set the patient information
    public void setPatient_id( String patientID ){

        patient_id = patientID;
    }

    public void setPatient_name( String patientName ){

        patient_name = patientName;
    }

    public void setPrescription_id( String prescriptionName ){

        prescription_id = prescriptionName;

    }

    public void setAddress( String addressName ){

        address = addressName;

    }

    public void setCity( String cityName ){

        city = cityName;

    }

    public void setState( String stateName ){

        state = stateName;

    }

}