package com.norandiaconu.venue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by NORAN on 11/23/2015.
 */

public class PostMessage extends FragmentActivity {
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
                        String text = textBox.getText().toString();
                        RequestParams params = new RequestParams();
                        params.put(new String("message"), text);
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.setBasicAuth("admin", "superstrongpassword");
                        client.post(url, params, new JsonHttpResponseHandler());
                        Toast toast = Toast.makeText(getApplicationContext(), "Message Posted", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(getApplicationContext(), MessageBoard.class);
                        intent.putExtra("arg", url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);                    }
                }
        );
    }

    @Override
    public boolean onNavigateUp() {
        this.finish();
        return false;
    }
}
