package com.example.imac.chs_pharmacy;

    import android.util.Log;

  public class Patient {

        private static Patient patientInstance = null;
        private String patient_name;
        private String rx_id;
        private static final String TAG = "Patient";


    private Patient() {
        patient_name = "Test";
        rx_id = "1234";

    }

    public static Patient getInstance() {
        if (patientInstance == null) {
            patientInstance = new Patient();
        }
        return patientInstance;
    }

    public void setPatientName( String patientName ){
        Log.d(TAG, "setting patient name");

        patient_name = patientName;
    }

    public String getPatientName( ){
        Log.d(TAG, "getting patient name");
        return this.patient_name;
    }

      public void setRxid( String rxid ){
          Log.d(TAG, "setting rx ");
          rx_id = rxid;
      }

      public String getRxid( ){
          Log.d(TAG, "gettingrxid");
          return this.rx_id;
      }
}