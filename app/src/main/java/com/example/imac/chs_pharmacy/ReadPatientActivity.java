package com.example.imac.chs_pharmacy;

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
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import android.util.Log;

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
    TextView tv14;
    TextView tv15;

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
    EditText HOA1;
    EditText HOA2;
    EditText HOA3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_patient);

        Intent intent = getIntent();

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

                HOA1   = (EditText)findViewById(R.id.textView13);
                String HOA1STRING = HOA1.getText().toString();

                HOA2   = (EditText)findViewById(R.id.textView14);
                String HOA2STRING = HOA2.getText().toString();

                HOA3   = (EditText)findViewById(R.id.textView15);
                String HOA3STRING = HOA3.getText().toString();

                //create instance of singleton and save attributes
                Patient p = Patient.getInstance();

                p.setPatientName(nameString);
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
        tv14=(TextView)findViewById(R.id.textView14);
        tv15=(TextView)findViewById(R.id.textView15);

        try {
            InputStream is = getAssets().open(message+".xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("Prescription");
            
            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;


                    //setting the values to the variables
                    tv2.setText( getValue("Name", element2));
                    tv.setText( getValue("PrescriptionID", element2));
                    tv3.setText( getValue("PatientID", element2));
                    tv4.setText( getValue("Quantity", element2));
                    tv5.setText( getValue("Refills", element2));
                    tv6.setText( getValue("Address", element2));
                    tv7.setText(getValue("City", element2));
                    tv8.setText(getValue("State", element2));
                    tv9.setText(getValue("Zip", element2));
                    tv10.setText(getValue("Label", element2));
                    tv11.setText(getValue("DosageText", element2));
                    tv12.setText(getValue("NDC", element2));

                }

            }


        } catch (Exception e) {e.printStackTrace();}


    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }


}

