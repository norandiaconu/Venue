package com.norandiaconu.venue;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by NORAN on 9/24/2015.
 */
public class EntranceMessage extends FragmentActivity {

    PlaceholderFragment f = new PlaceholderFragment();
    String fromIntentName = "";
    String fromIntentId = "";
    String [] id = new String [3];
    String toMessage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_layout);

        Bundle ex = getIntent().getExtras();
        id = ex.getStringArray("key");
        fromIntentName = id[0];
        fromIntentId = id[1];
        final String fromIntentUrl = id[2];
        toMessage = fromIntentUrl;
        Log.v("", "Entrance Name: " + fromIntentName);

        TextView theView = (TextView)findViewById(R.id.box);
        theView.setText(fromIntentName);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, f).commit();
        }
        final Button enter_button = new Button(this);
        enter_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MessageBoard.class);
                Log.v("", "1" + toMessage);
                intent.putExtra("arg", toMessage);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void clicked(View view) {
        Intent intent = new Intent(this, MessageBoard.class);
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