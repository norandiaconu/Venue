package com.norandiaconu.venue;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by NORAN on 10/6/2015.
 */
public class MessageBoard extends ListActivity{// implements LoaderManager.LoaderCallbacks<Cursor> {
    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
    String url = "";

    String[] messageArray;
    int messagesLength = 0;
    //private Button post_button;

    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[]{ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    // This is the select criteria
    static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.v("", "1");
        Log.v("", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messageboard_layout);
        //LinearLayout layout = (LinearLayout) getWindow().findViewById(R.id.title_complex);
        //layout.addView(new Button(this));
        Context context = getApplicationContext();
        url = getIntent().getExtras().getString("arg");
        Log.v("", "MESSAGE: " + url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("admin", "superstrongpassword");
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray messages) {
                Context context = getApplicationContext();
                //CharSequence text = "TOAST" + url;
                int duration = Toast.LENGTH_LONG;
                //CharSequence text = messages.toString();
                CharSequence text = "" + messages.length();
                //

                messagesLength = messages.length();
                messageArray = new String[messagesLength];
                for (int i = 0; i < messagesLength; i++) {
                    try {
                        JSONObject message = messages.getJSONObject(i);
                        String temp = message.getString("message");
                        //messageArray[i] = message.getString("message");
                        messageArray[i] = temp;
                    } catch (JSONException e) {
                        Log.v("", "catch");
                    }
                }
                setListAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, messageArray));

                //
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                //Log.v("", "MESSAGENESS");
            }
        });
        //final Button post_button = new Button(this);//(Button) findViewById(R.id.enter_button);
        final Button post_button = (Button) findViewById(R.id.button2);
        //View v = getLayoutInflater(R.layout.messageboard_layout, getViewGroup(), false);
        //post_button = (Button)v.
        post_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostMessage.class);
                //Log.v("", "1" + toMessage);
                intent.putExtra("arg", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //messageArray = new String[1];
        //messageArray[0] = "TEST";
        //messageArray[messagesLength] = "1TEST";
        //setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, messageArray));
    }


}
/*
        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        //String [] fromColumns = messageArray;
        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        Log.v("", "onCreateLoader");
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        Log.v("", "onLoadFinished");
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        Log.v("", "onLoaderReset");
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
        //Log.v("", "MESSAGE: " + id);
        Log.v("", "onListItemClick");
    }

}
*/