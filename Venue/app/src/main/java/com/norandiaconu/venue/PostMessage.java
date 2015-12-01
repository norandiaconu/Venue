package com.norandiaconu.venue;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by NORAN on 11/23/2015.
 */

public class PostMessage extends FragmentActivity {
    public EditText textField;
    String url = "";
    EditText textBox;
    Button mButton;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.post_layout);
        url = getIntent().getExtras().getString("arg");
        textBox = (EditText)findViewById(R.id.text_box);
        mButton = (Button)findViewById(R.id.button3);
        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.v("EditText", textBox.getText().toString());
                    }
                }
        );
        //TextView theView = (TextView)findViewById(R.id.box);

    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.post_layout, parent, false);
        textField = (EditText)v.findViewById(R.id.message_title);
        //textField.setText();
        return v;
    }
    */
}
