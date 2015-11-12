package com.example.todolist.todolist;

public class Task {
    int id;
    String text;
    int parent;

    Task(int _id, String _text, int _parent){
        id = _id;
        text = _text;
        parent = _parent;
    }
}
