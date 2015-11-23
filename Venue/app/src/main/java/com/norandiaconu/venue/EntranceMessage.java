package com.norandiaconu.venue;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by NORAN on 9/24/2015.
 */
public class EntranceMessage extends FragmentActivity {

    //final static String ARG_POSITION = "position";
    //int mCurrentPosition = -1;
    PlaceholderFragment f = new PlaceholderFragment();
    String fromIntentName = "";
    String fromIntentId = "";
    //String fromIntentUrl = "";
    String [] id = new String [3];
    String toMessage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_layout);

        Bundle ex = getIntent().getExtras();
        //String fromIntentName = ex.getString("key");
        //String fromIntentId = ex.getString("key2");
        id = ex.getStringArray("key");
        fromIntentName = id[0];
        fromIntentId = id[1];
        final String fromIntentUrl = id[2];
        toMessage = fromIntentUrl;
        Log.v("", "Entrance Name: " + fromIntentName);// + " " + fromIntentId);

        TextView theView = (TextView)findViewById(R.id.box);
        //theView.setText(fromIntentName + " " + fromIntentId + " " + fromIntentUrl);
        theView.setText(fromIntentName);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, f).commit();//new PlaceholderFragment()).commit();
        }
        final Button enter_button = new Button(this);//(Button) findViewById(R.id.enter_button);
        enter_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //String clickUrl = fromIntentUrl;
                //Bundle bundleName = new Bundle();
                //Log.v("", "URL1: " + clickUrl);
                //bundleName.putString("key2", clickUrl);
                //bundle2.putString("key2", fromIntentId);
                //bundle2.putStringArray("key2", id);
                Intent intent = new Intent(v.getContext(), MessageBoard.class);
                //intent.putExtras(bundleName);
                Log.v("", "1" + toMessage);
                intent.putExtra("arg", toMessage);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void clicked(View view) {
        //Bundle bundleName = new Bundle();
        //bundleName.putString("key2", toMessage);
        Intent intent = new Intent(this, MessageBoard.class);
        //intent.putExtras(bundleName);
        Log.v("", "2" + toMessage);
        intent.putExtra("arg", toMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.entrance_layout,
                    container, false);
            return rootView;
        }
    }
}