package com.example.imac.chs_pharmacy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReadPatientActivity extends AppCompatActivity {
    TextView tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_patient);

        Intent intent = getIntent();

        //this is the code scanned
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);

        Button btn = (Button) findViewById(R.id.bleButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadPatientActivity.this, DetectBleActivity.class);
                startActivity(intent);
            }
        });


        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_read_patient);
        layout.addView(textView);

        tv2=(TextView)findViewById(R.id.textView2);

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
                    textView.setText( getValue("Name", element2));
                }
            }

        } catch (Exception e) {e.printStackTrace();}


    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    //we want this button to go to the new bluetooth view
    public void findBle(View view) {
        Intent intent = new Intent(this, ScanningActivity.class);
        startActivity(intent);
    }




}

