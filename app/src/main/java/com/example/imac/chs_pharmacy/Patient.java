package com.example.imac.chs_pharmacy;

    import android.bluetooth.BluetoothGattService;
    import android.util.Log;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;

public class Patient {

    private static Patient patientInstance = null;
    private String patient_name;
    private String rx_id;
    private String patient_id;
    private String quantity;
    private String refills;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String label;
    private String dosage;
    private String ndc;
    private String nextValue;
    private String NPI;
    private static final String TAG = "Patient";

    //each setter sets the value to the array.
    private ArrayList<String> writeData = new ArrayList<>();
    private ArrayList<String> HOAs = new ArrayList<>();


    private Patient() {

    }

    public static Patient getInstance() {
        if (patientInstance == null) {
            patientInstance = new Patient();
        }
        return patientInstance;
    }

    public String getNextWriteValue() {

        //all this needs to do is get the first value, pop it off, and return it
        Log.d(TAG, "getting the next write value");

        if (!writeData.isEmpty()) {
            nextValue = writeData.get(0);
            writeData.remove(0);
        } else {
            nextValue = "none";
        }

        Log.d(TAG, "value is: " + nextValue);

        return nextValue;
    }

    public void resetWriteValues() {
        //this function clears out the array so it doesn't get filled up
        writeData.clear();
    }

    public void saveScheduleTime(String time) {
        Log.d(TAG, "saveschedule time is: " + time);
        HOAs.add(time);
    }

    public String getScheduleTimes() {
        Log.d(TAG, "inside schedule times");
        //iterate through the array and build out a string.
        //so, for each item, concatenate it on until i = 0 then return string
        StringBuilder sb = new StringBuilder(14);
        for (String hoa : HOAs) {
            sb.append(hoa);
            Log.d(TAG, hoa);
        }
        Log.d(TAG, "getschedule time is: " + sb.toString());
//        System.out.println(sb.toString());
        return sb.toString();
    }

    public void setPatientName(String patientName) {
        Log.d(TAG, "setting name");
        patient_name = patientName;
        writeData.add("Name");
        writeData.add(patientName);
    }

    public void setNPI(String npi) {
        Log.d(TAG, "npi is: " + npi);
        NPI = npi;
        writeData.add("NPI");
        writeData.add(npi);
    }

    public String getPatientName() {
        return this.patient_name;
    }

    public String checkPatientData(String thisName, String thisAddress, String thisLabel, String thisCity, String thisDosage,
                                   String thisNpi, String thisNdc, String thisQty, String thisState, String thisRefills,
                                   String thisRxid, String thisPatid, String thisZip) {

        if ((patient_name == thisName) && (address == thisAddress) && (label == thisLabel) && (city == thisCity)
                && (dosage == thisDosage) && (NPI == thisNpi) && (ndc == thisNdc) && (quantity == thisQty)
                && (state == thisState) && (refills == thisRefills) && (rx_id == thisRxid) && (patient_id == thisPatid) && (zip == thisZip)){
            return "true";
        }

        else {
            return "false";
        }

    }

    public void setRxid(String rxid) {
        Log.d(TAG, "setting rxid");
        rx_id = rxid;
        writeData.add("PrescriptionID");
        writeData.add(rxid);
    }

    public String getRxid() {
        return this.rx_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        Log.d(TAG, "set patid");
        this.patient_id = patient_id;
        writeData.add("PatientID");
        writeData.add(patient_id);
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        Log.d(TAG, "setting qty");
        this.quantity = quantity;
        writeData.add("Quantity");
        writeData.add(quantity);
    }

    public String getRefills() {
        return refills;
    }

    public void setRefills(String refills) {
        Log.d(TAG, "setting refills");
        this.refills = refills;
        writeData.add("Refills");
        writeData.add(refills);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        Log.d(TAG, "setting address");
        this.address = address;
        writeData.add("Address");
        writeData.add(address);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        Log.d(TAG, "setting city");
        this.city = city;
        writeData.add("City");
        writeData.add(city);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        Log.d(TAG, "setting state");
        this.state = state;
        writeData.add("State");
        writeData.add(state);
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        Log.d(TAG, "setting zip");
        this.zip = zip;
        writeData.add("Zip");
        writeData.add(zip);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        Log.d(TAG, "setting label");
        this.label = label;
        writeData.add("Label");
        writeData.add(label);
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        Log.d(TAG, "setting dosage");
        this.dosage = dosage;
        writeData.add("DosageText");
        writeData.add(dosage);
    }

    public String getNdc() {
        return ndc;
    }

    public void setNdc(String ndc) {
        Log.d(TAG, "setting ndc");
        this.ndc = ndc;
        writeData.add("NDC");
        writeData.add(ndc);
    }

}