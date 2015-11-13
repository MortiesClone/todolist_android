package com.example.todolist.todolist;

public class Task {
    String number_of_task;
    int id;
    String text;
    int parent;

    Task(String _number_of_task, int _id, String _text, int _parent){
        number_of_task = _number_of_task;
        id = _id;
        text = _text;
        parent = _parent;
    }
}
