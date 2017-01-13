package com.example.imac.chs_pharmacy;

/**
 * Created by imac on 1/13/17.
 */

public class customerClass {
    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.AdapterView.OnItemClickListener;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.Toast;

    public class SimpleList extends Activity {  //SimpleList is the name of this class

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            final ListView lv=(ListView)findViewById(R.id.listView1);

            //final ArrayList<String> myNewList = new ArrayList<String>();

            ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.simple_array,android.R.layout.simple_list_item_1);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    String item=lv.getItemAtPosition(arg2).toString();
                    String itemordered;
                    itemordered = item + " added to list";
                    Toast.makeText(getApplicationContext(), itemordered, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
