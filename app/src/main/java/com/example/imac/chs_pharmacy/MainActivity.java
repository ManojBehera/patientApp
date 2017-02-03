package com.example.imac.chs_pharmacy;

import java.io.IOException;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

//imports for zxing
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {
//    TextView tv1;
    public final static String EXTRA_MESSAGE = "com.example.imac.chs_pharmacy.MESSAGE";
    private static final String TAG = "Main";
    private Button scanBtn;
    private TextView formatTxt, contentTxt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate the button/ textviews
        scanBtn = (Button) findViewById(R.id.scan_button);
        formatTxt = (TextView) findViewById(R.id.scan_format);
        contentTxt = (TextView) findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);

    }


    public void onClick(View v){
    //respond to clicks
        //make sure the button was clicked
        if(v.getId()==R.id.scan_button){
            //create instance of class we imported
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);

            //start scanning
            scanIntegrator.initiateScan();
        }
    }

    //retrieve scan result
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //try to parse the result into an instance of the ZXing Intent Result class we imported
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        //we have a result
        if (scanningResult != null) {
            //get the contents of the scan
            String scanContent = scanningResult.getContents();
            Log.d(TAG, scanContent );

            //check out the format it is in
            String scanFormat = scanningResult.getFormatName();

            Intent patientIntent = new Intent(this, ReadPatientActivity.class);
            patientIntent.putExtra(EXTRA_MESSAGE, scanContent);
            startActivity(patientIntent);
        }

        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
