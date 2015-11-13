package com.example.todolist.todolist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public final static String URL = "http://192.168.56.1/backend/index.php?action=get_tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        List<Task> tasks = new ArrayList<Task>();

                        try {
                            Log.e("status", response.getBoolean("status") + "");
                            if (response.getBoolean("status")) {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0, j = 1; i < data.length(); i++, j++) {
                                    tasks.add(new Task(
                                            j + ". ",
                                            data.getJSONObject(i).getInt("id"),
                                            data.getJSONObject(i).getString("text"),
                                            isNull(data.getJSONObject(i).getString("parent"))
                                    ));
                                }
                            } else {
                                String error = response.getString("error");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", Log.getStackTraceString(e));
                        }
                        BoxAdapter boxAdapter = new BoxAdapter(MainActivity.this, tasks);
                        ListView lvMain = (ListView) findViewById(R.id.lvMain);
                        lvMain.setAdapter(boxAdapter);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject erorResponse) {
                        Log.e("error", "fail");
                    }
                }
        );
    }

    public int isNull(String str){
        if(str == "null")
            return 0;
        else
            return Integer.parseInt(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
