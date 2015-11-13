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
import android.widget.EditText;
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

    public final static String URL = "http://192.168.56.1/backend/index.php";
    public EditText task_text;

    AsyncHttpClient client;
    List<Task> tasks;
    BoxAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        task_text = (EditText)findViewById(R.id.task_text);
        tasks = new ArrayList<Task>();
        client = new AsyncHttpClient();

        client.get(GenerateURL("get_tasks", 0, ""), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
                        boxAdapter = new BoxAdapter(MainActivity.this, tasks);
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

    public void NewTask(View v){

        client.get(GenerateURL("new_task", 0, task_text.getText().toString()), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        tasks.add(new Task(
                                tasks.size() + 1 + ".",
                                response.getInt("data"),
                                task_text.getText().toString(),
                                0
                        ));
                        boxAdapter.notifyDataSetChanged();
                        task_text.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

            }
        });
    }

    public String GenerateURL(String action, int id, String text){
        String generated_url = URL + "?action=";
        switch (action){
            case "get_tasks":
                generated_url += "get_tasks";
                break;
            case "new_task":
                generated_url += "write&text=" + text;
                break;
            case "edit_task":
                generated_url += "rewrite&id=" + id + "&text=" + text;
                break;
            case "delete":
                generated_url += "delete&id=" + id;
                break;
        }

        return generated_url;
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
