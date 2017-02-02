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
    private static final String TAG = "Patient";

      //each setter sets the value to the array.
      private ArrayList<String> writeData = new ArrayList<>();


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


          //all values are returning as test patient?

          Log.d(TAG, "getting the next write value");

          if (!writeData.isEmpty()) {
              nextValue = writeData.get(0);
              writeData.remove(0);
          }
          else {
              nextValue = "none";
          }

          Log.d(TAG, "value is: " + nextValue);

          return nextValue;
      }

    public void setPatientName( String patientName ){

        patient_name = patientName;
        writeData.add("name");
        writeData.add(patientName);
    }

    public String getPatientName( ){
        return this.patient_name;
    }

      public void setRxid( String rxid ){

          rx_id = rxid;
          writeData.add("rxid");
          writeData.add(rxid);
      }

      public String getRxid( ){
          return this.rx_id;
      }

      public String getPatient_id() {
          return patient_id;
      }

      public void setPatient_id(String patient_id) {
          this.patient_id = patient_id;
          writeData.add("PatId");
          writeData.add(patient_id);
      }

      public String getQuantity() {
          return quantity;
      }

      public void setQuantity(String quantity) {
          this.quantity = quantity;
          writeData.add("quantity");
          writeData.add(quantity);
      }

      public String getRefills() {
          return refills;
      }

      public void setRefills(String refills) {
          this.refills = refills;
          writeData.add("refills");
          writeData.add(refills);
      }

      public String getAddress() {
          return address;
      }

      public void setAddress(String address) {
          this.address = address;
          writeData.add("address");
          writeData.add(address);
      }

      public String getCity() {
          return city;
      }

      public void setCity(String city) {
          this.city = city;
          writeData.add("city");
          writeData.add(city);
      }

      public String getState() {
          return state;
      }

      public void setState(String state) {
          this.state = state;
          writeData.add("state");
          writeData.add(state);
      }

      public String getZip() {
          return zip;
      }

      public void setZip(String zip) {
          this.zip = zip;
          writeData.add("zip");
          writeData.add(zip);
      }

      public String getLabel() {
          return label;
      }

      public void setLabel(String label) {
          this.label = label;
          writeData.add("label");
          writeData.add(label);
      }

      public String getDosage() {
          return dosage;
      }

      public void setDosage(String dosage) {
          this.dosage = dosage;
          writeData.add("dosage");
          writeData.add(dosage);
      }

      public String getNdc() {
          return ndc;
      }

      public void setNdc(String ndc) {
          this.ndc = ndc;
          writeData.add("ndc");
          writeData.add(ndc);
      }
}