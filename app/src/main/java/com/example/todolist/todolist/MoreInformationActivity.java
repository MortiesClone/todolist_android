package com.example.todolist.todolist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MoreInformationActivity extends AppCompatActivity {

    TextView task;

    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information);

        task = (EditText)findViewById(R.id.task);
        task.setText(getIntent().getStringExtra("task"));
    }

    public void EditTask(View v){
        String url = getIntent().getStringExtra("edit");
        String id = getIntent().getStringExtra("id");
        url += "&id=" + id + "&text=" + task.getText().toString();
        Send(url);
    }

    public void DeleteTask(View v){
        String url = getIntent().getStringExtra("delete");
        String id = getIntent().getStringExtra("id");

        url += "&id=" + id;

        Send(url);
    }

    public void BackToBegin(View v){
        finish();
    }

    public void Send(String url){
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getBoolean("status")) {
                        Toast.makeText(MoreInformationActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

}
