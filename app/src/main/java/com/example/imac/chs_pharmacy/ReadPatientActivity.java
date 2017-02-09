package com.example.imac.chs_pharmacy;

import android.bluetooth.BluetoothGattService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import android.util.Log;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

//need to create a model based on the xml. the pharmicist should be able to overwrite everything right now and save the updated data to the model

public class ReadPatientActivity extends AppCompatActivity {
    private static final String TAG = "ReadPatient";
    TextView tv;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    TextView tv7;
    TextView tv8;
    TextView tv9;
    TextView tv10;
    TextView tv11;
    TextView tv12;
    TextView tv13;
//    TextView tv14;
//    TextView tv15;

    EditText name;
    EditText rxid;
    EditText patient_id;
    EditText quantity;
    EditText refills;
    EditText address;
    EditText city;
    EditText state;
    EditText zip;
    EditText label;
    EditText dosage;
    EditText ndc;
    EditText npi;
    EditText HOA2;
    EditText HOA3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_patient);

        Intent intent = getIntent();

        //clear out array in Patient


        //this is the code scanned
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //message is passing as null
        Log.d(TAG, message + "scanned");
        TextView textView = new TextView(this);
        textView.setTextSize(40);

        Button btn = (Button) findViewById(R.id.bleButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the class data from the input boxes
                rxid = (EditText) findViewById(R.id.textView1);
                String rxidString = rxid.getText().toString();

                name   = (EditText)findViewById(R.id.textView2);
                String nameString = name.getText().toString();

                patient_id   = (EditText)findViewById(R.id.textView3);
                String patIdString = patient_id.getText().toString();

                quantity   = (EditText)findViewById(R.id.textView4);
                String qtyString = quantity.getText().toString();

                refills   = (EditText)findViewById(R.id.textView5);
                String refillString = refills.getText().toString();

                address   = (EditText)findViewById(R.id.textView6);
                String addressString = address.getText().toString();

                city   = (EditText)findViewById(R.id.textView7);
                String cityString = city.getText().toString();

                state   = (EditText)findViewById(R.id.textView8);
                String stateString = state.getText().toString();

                zip   = (EditText)findViewById(R.id.textView9);
                String zipString = zip.getText().toString();

                label   = (EditText)findViewById(R.id.textView10);
                String labelString = label.getText().toString();

                dosage   = (EditText)findViewById(R.id.textView11);
                String dosageString = dosage.getText().toString();

                ndc   = (EditText)findViewById(R.id.textView12);
                String ndcString = ndc.getText().toString();

                npi   = (EditText)findViewById(R.id.textView13);
                String npistring = npi.getText().toString();

                //create instance of singleton and save attributes
                Patient p = Patient.getInstance();
                p.resetWriteValues();
                p.setPatientName(nameString);
                p.setNPI(npistring);
                p.setRxid(rxidString);
                p.setPatient_id(patIdString);
                p.setQuantity(qtyString);
                p.setRefills(refillString);
                p.setAddress(addressString);
                p.setCity(cityString);
                p.setState(stateString);
                p.setZip(zipString);
                p.setLabel(labelString);
                p.setDosage(dosageString);
                p.setNdc(ndcString);

                //kick off the ble activity
                Intent intent = new Intent(ReadPatientActivity.this, DetectBleActivity.class);
;                startActivity(intent);
            }
        });

        //setting variables to text views
        tv2=(TextView)findViewById(R.id.textView2);
        tv=(TextView)findViewById(R.id.textView1);
        tv3=(TextView)findViewById(R.id.textView3);
        tv4=(TextView)findViewById(R.id.textView4);
        tv5=(TextView)findViewById(R.id.textView5);
        tv6=(TextView)findViewById(R.id.textView6);
        tv7=(TextView)findViewById(R.id.textView7);
        tv8=(TextView)findViewById(R.id.textView8);
        tv9=(TextView)findViewById(R.id.textView9);
        tv10=(TextView)findViewById(R.id.textView10);
        tv11=(TextView)findViewById(R.id.textView11);
        tv12=(TextView)findViewById(R.id.textView12);
        tv13=(TextView)findViewById(R.id.textView13);
//        tv14=(TextView)findViewById(R.id.textView14);
//        tv15=(TextView)findViewById(R.id.textView15);

        //samba logic
//        try{
//            String user = "guest";
//            String pass ="";
//
//            String url = "smb://fs01.hq.sfp.net/public/chs/" + message;
////            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
////                    null, user, pass);
////            SmbFile sfile = new SmbFile(url, auth);

            try {
                Log.d(TAG, "going to try looping through"+message);
//            File fXmlFile = new File(message + ".xml");
//                StringBuilder builder = null;
//                String user = "guest";
//                String pass ="";
//
//                String url = "smb://fs01.hq.sfp.net/public/chs/" + message + ".xml";
//
//                Log.d(TAG, url);
//
//
//                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, user, pass); //This class stores and encrypts NTLM user credentials.
//
//                SmbFile sFile = new SmbFile(url, auth);
//
//                SmbFileInputStream fXmlFile = new SmbFileInputStream(sFile);
//
////                InputStream fXmlFile = sFile.getInputStream();
//                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//                Document doc = dBuilder.parse(fXmlFile);



                InputStream fXmlFile = getAssets().open(message+".xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);

                doc.getDocumentElement().normalize();


                //make a list of all the prescription nodes. this will only be one.
                NodeList rootElement = doc.getElementsByTagName("Prescription");

                //this loop should only fire once since it's going through all prescription nodes
                for (int j = 0; j < rootElement.getLength(); j++) {
                    Log.d(TAG, "we're at the top level");

                    //get the array of all the children
                    Node dataRow = rootElement.item(j);
                    NodeList dataList = dataRow.getChildNodes();
//                Node dataNode = rootElement.item(j);

                    //go through each item. will need to check for more children
                    for (int i = 0; i < dataList.getLength(); i++) {
                        Node detail = dataList.item(i);

                        if (detail.getNodeName().equals("HOAList")) {
                            Log.d(TAG, "we in da list");

                            NodeList HOAList = detail.getChildNodes();
                            for (int k = 0; k < HOAList.getLength(); k++) {
                                Node thistime = HOAList.item(k);
                                String context = thistime.getTextContent();
                                //wtf why is it looping through blanks
                                if (k % 2 == 0) {
                                    // even
                                }
                                else {
                                    Log.d(TAG, context+k);
//                                HOAs.add(context);
                                    Patient p = Patient.getInstance();
                                    p.saveScheduleTime(context);
                                }
                            }
                        }
//                    tv13.setText(HOAs.get(0));
//                    tv14.setText(HOAs.get(1));
//                    tv15.setText(HOAs.get(2));
//                    for (String hoa : HOAs)
//                    {
//                        Log.d(TAG, hoa);
//                    }

                        if (detail.getNodeName().equals("Name")) {
                            String letter = detail.getTextContent();
                            Log.d(TAG, letter);
                        }

                        if (detail.getNodeName().equals("PrescriptionID")) {
                            tv.setText(detail.getTextContent());

                        }
                        if (detail.getNodeName().equals("Name")) {
                            tv2.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("PatientID")) {
                            tv3.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("Quantity")) {
                            tv4.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("Refills")) {
                            tv5.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("Address")) {
                            tv6.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("City")) {
                            tv7.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("State")) {
                            tv8.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("Zip")) {
                            tv9.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("Label")) {
                            tv10.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("DosageText")) {
                            tv11.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("NDC")) {
                            tv12.setText(detail.getTextContent());
                        }
                        if (detail.getNodeName().equals("NPI")) {
                            tv13.setText(detail.getTextContent());
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

}

}

