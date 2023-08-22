package com.example.todolist

import TaskAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaskListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("tasks")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter()
        recyclerView.adapter = taskAdapter

        // Add Firebase ValueEventListener to populate the task list
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                // Handle data changes and update the UI
//                val tasks = mutableListOf<com.example.todolist.Task>()
//                for (data in snapshot.children) {
//                    val task = data.getValue(Task::class.java)
//                    if (task != null) {
//                        tasks.add(task)
//                    }
//                }
//                taskAdapter.submitList(tasks)
                val fabAddTask = findViewById<FloatingActionButton>(R.id.fabAddTask)

                fabAddTask.setOnClickListener {
                    // Open a new activity or dialog to create a new task
                    // Example: Start an AddTaskActivity
                    // After adding a new task to Firebase
                    taskAdapter.notifyDataSetChanged()

                    val intent = Intent(this@TaskListActivity,AddTaskActivity::class.java)
                    startActivity(intent)
                }

                // Handle data changes and update the UI
                val tasks = mutableListOf<com.example.todolist.Task>() // Replace 'com.example.todolist' with the actual package name of your Task class
                for (data in snapshot.children) {
                    // Check if the data can be deserialized into a Task object
                    val task = data.getValue(com.example.todolist.Task::class.java) // Specify the package name
                    if (task != null) {
                        tasks.add(task)
                    } else {
                        // Handle the case where data could not be deserialized into a Task
                        // This might indicate a data structure mismatch
                        Log.e(TAG, "Error parsing task data at ${data.key}")
                    }
                }
                taskAdapter.submitList(tasks)

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read error
            }
        })
    }

}