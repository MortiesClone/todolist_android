package com.example.todolist.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class BoxAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater layoutInflater;
    List<Task> objects;

    BoxAdapter(Context _context, List<Task> _list){
        ctx = _context;
        objects = _list;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Task getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = layoutInflater.inflate(R.layout.item, parent, false);
        }

        Task task = getItem(position);

        ((TextView) view.findViewById(R.id.task)).setText(task.text);

        return view;
    }
}