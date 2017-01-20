package com.example.imac.chs_pharmacy;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;
import android.widget.ImageView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;




public class MainActivity extends Activity {
//    TextView tv1;
    public final static String EXTRA_MESSAGE = "com.example.imac.chs_pharmacy.MESSAGE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final SurfaceView cameraView = (SurfaceView)findViewById(R.id.camera_view);

       final TextView barcodeInfo = (TextView)findViewById(R.id.code_info);



       final BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();


        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(24.0f);

       final CameraSource cameraSource = builder.build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    //this is where we need to call the next activity
                    sendMessage(barcodes.valueAt(0).displayValue);
//                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
//                        public void run() {
//                            barcodeInfo.setText(    // Update the TextView
//                                    barcodes.valueAt(0).displayValue
//                            );
//                        }
//                    });
                }
            }
        });


        //detect the barcode
//        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
//        SparseArray<Barcode> barcodes = detector.detect(frame);
//
//        //decode the barcode
//        Barcode thisCode = barcodes.valueAt(0);
//        TextView txtView = (TextView) findViewById(R.id.txtContent);
//        txtView.setText(thisCode.rawValue);

        //this chunk of code reads the pharmacy xml file, and can pull data needed from it
        //don't need it for now
//        tv1=(TextView)findViewById(R.id.textView1);
//
//        try {
//            InputStream is = getAssets().open("customer.xml");
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(is);
//
//            Element element=doc.getDocumentElement();
//            element.normalize();
//
//            NodeList nList = doc.getElementsByTagName("Prescription");
//
//            for (int i=0; i<nList.getLength(); i++) {
//
//                Node node = nList.item(i);
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element2 = (Element) node;
//                    tv1.setText(tv1.getText()+"\nName : " + getValue("Name", element2)+"\n");
//                    tv1.setText(tv1.getText()+"Address : " + getValue("Address", element2)+"\n");
//
//                }
//            }
//
//        } catch (Exception e) {e.printStackTrace();}

    }

//    private static String getValue(String tag, Element element) {
//        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
//        Node node = nodeList.item(0);
//        return node.getNodeValue();
//    }


//    /** Called when the user clicks the Send button */
    public void sendMessage(String scanned) {
        Intent intent = new Intent(this, ReadPatientActivity.class);
        intent.putExtra(EXTRA_MESSAGE, scanned);
        startActivity(intent);
    }


}
