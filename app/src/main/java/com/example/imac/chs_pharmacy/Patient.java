package com.example.imac.chs_pharmacy;

    import android.bluetooth.BluetoothDevice;
    import android.util.Log;

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


    private Patient() {

    }

    public static Patient getInstance() {
        if (patientInstance == null) {
            patientInstance = new Patient();
        }
        return patientInstance;
    }

    public void setPatientName( String patientName ){

        patient_name = patientName;
    }

    public String getPatientName( ){
        return this.patient_name;
    }

      public void setRxid( String rxid ){
          rx_id = rxid;
      }

      public String getRxid( ){
          return this.rx_id;
      }

      public String getPatient_id() {
          return patient_id;
      }

      public void setPatient_id(String patient_id) {
          this.patient_id = patient_id;
      }

      public String getQuantity() {
          return quantity;
      }

      public void setQuantity(String quantity) {
          this.quantity = quantity;
      }

      public String getRefills() {
          return refills;
      }

      public void setRefills(String refills) {
          this.refills = refills;
      }

      public String getAddress() {
          return address;
      }

      public void setAddress(String address) {
          this.address = address;
      }

      public String getCity() {
          return city;
      }

      public void setCity(String city) {
          this.city = city;
      }

      public String getState() {
          return state;
      }

      public void setState(String state) {
          this.state = state;
      }

      public String getZip() {
          return zip;
      }

      public void setZip(String zip) {
          this.zip = zip;
      }

      public String getLabel() {
          return label;
      }

      public void setLabel(String label) {
          this.label = label;
      }

      public String getDosage() {
          return dosage;
      }

      public void setDosage(String dosage) {
          this.dosage = dosage;
      }

      public String getNdc() {
          return ndc;
      }

      public void setNdc(String ndc) {
          this.ndc = ndc;
      }

  }