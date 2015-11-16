package com.example.todolist.todolist;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    ListView lvMain;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        task_text = (EditText)findViewById(R.id.task_text);
        tasks = new ArrayList<Task>();
        client = new AsyncHttpClient();
        lvMain = (ListView) findViewById(R.id.lvMain);
        intent = new Intent(MainActivity.this, MoreInformationActivity.class);

        client.get(GenerateURL("get_tasks", ""), new JsonHttpResponseHandler() {
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
                        lvMain.setAdapter(boxAdapter);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject erorResponse) {
                        Log.e("error", "fail");
                    }
                }
        );
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("id", tasks.get(position).id + "");
                intent.putExtra("task", tasks.get(position).text);
                intent.putExtra("edit", GenerateURL("edit_task", ""));
                intent.putExtra("delete", GenerateURL("delete_task", ""));
                startActivity(intent);
            }
        });
    }

    public void NewTask(View v){

        client.get(GenerateURL("new_task", task_text.getText().toString()), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        tasks.add(new Task(
                                tasks.size() + 1 + ".",
                                response.getInt("data"),
                                task_text.getText().toString(),
                                0                              //parent
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

    public String GenerateURL(String action, String text){
        String generated_url = URL + "?action=";
        switch (action){
            case "get_tasks":
                generated_url += "get_tasks";
                break;
            case "new_task":
                generated_url += "write&text=" + text;
                break;
            case "edit_task":
                generated_url += "rewrite";
                break;
            case "delete_task":
                generated_url += "delete";
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

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
