package com.example.todolist.todolist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MoreInformation extends Fragment {

    TextView task;
    Button edit, delete, back;
    AsyncHttpClient client = new AsyncHttpClient();
    Activity activity;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_information, container, false);

        activity = getActivity();
        intent   = activity.getIntent();

        task = (TextView)view.findViewById(R.id.task);
        edit = (Button)view.findViewById(R.id.edit_task);
        delete = (Button)view.findViewById(R.id.delete_task);
        back = (Button)view.findViewById(R.id.back);

        task.setText(intent.getStringExtra("task"));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Clicks();
    }

    public void Clicks(){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = intent.getStringExtra("edit");
                String id = intent.getStringExtra("id");
                url += "&id=" + id + "&text=" + task.getText().toString();
                Send(url);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = intent.getStringExtra("delete");
                String id = intent.getStringExtra("id");
                url += "&id=" + id;
                Send(url);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    public void Send(String url){
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        Toast.makeText(activity, R.string.success, Toast.LENGTH_SHORT).show();
                        activity.setResult(Activity.RESULT_OK, new Intent().putExtra("123", "ok"));
                        activity.finish();
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
