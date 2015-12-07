package com.norandiaconu.venue;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by NORAN on 10/6/2015.
 */
public class MessageBoard extends ListActivity{
    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
    String url = "";
    SwipeRefreshLayout swipeLayout;
    ListView lView;

    String[] messageArray;
    int messagesLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messageboard_layout);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        lView = (ListView) findViewById(R.id.list);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_LONG);
                        toast.show();
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.setBasicAuth("admin", "superstrongpassword");
                        client.get(url, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray messages) {
                                Context context = getApplicationContext();
                                messagesLength = messages.length();
                                messageArray = new String[messagesLength];
                                for (int i = 0; i < messagesLength; i++) {
                                    try {
                                        JSONObject message = messages.getJSONObject(i);
                                        String temp = message.getString("message");
                                        messageArray[i] = temp;
                                    } catch (JSONException e) {
                                        Log.v("", "catch");
                                    }
                                }
                                setListAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, messageArray));
                                swipeLayout.setRefreshing(false);
                            }
                        });
                    }
                }, 2500);
            }
        });
        url = getIntent().getExtras().getString("arg");
        Log.v("", "MESSAGE: " + url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("admin", "superstrongpassword");
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray messages) {
                Context context = getApplicationContext();
                messagesLength = messages.length();
                messageArray = new String[messagesLength];
                for (int i = 0; i < messagesLength; i++) {
                    try {
                        JSONObject message = messages.getJSONObject(i);
                        String temp = message.getString("message");
                        messageArray[i] = temp;
                    } catch (JSONException e) {
                        Log.v("", "catch");
                    }
                }
                setListAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, messageArray));
            }
        });
        final ImageButton post_button = (ImageButton) findViewById(R.id.button2);
        post_button.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int diameter = 138;
                outline.setOval(0, 0, diameter, diameter);
            }
        });
        post_button.setClipToOutline(true);
        post_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostMessage.class);
                intent.putExtra("arg", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigateUp() {
        this.finish();
        return false;
    }
}
