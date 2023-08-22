package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class AddTaskActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addTaskButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        firebaseAuth = FirebaseAuth.getInstance()

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        addTaskButton = findViewById(R.id.addTaskButton)

        addTaskButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (title.isNotEmpty()) {
                // Create a new task object
                val task = Task(
                    id = UUID.randomUUID().toString(), // Generate a unique ID
                    title = title,
                    description = description,
                    isCompleted = false // New tasks are initially marked as not completed
                )

                // Save the task to Firebase
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val databaseReference = FirebaseDatabase.getInstance()
                        .getReference("users/${currentUser.uid}/tasks/${task.id}")
                    databaseReference.setValue(task)
                        .addOnCompleteListener { taskCreationTask ->
                            if (taskCreationTask.isSuccessful) {
                                // Task added successfully
                                // After adding a new task to Firebase
//                                taskAdapter.notifyDataSetChanged()

                                finish() // Close the activity
                            } else {
                                // Handle task creation error
                            }
                        }
                }
            }
        }
    }
}