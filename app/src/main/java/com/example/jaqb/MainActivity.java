package com.example.jaqb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jaqb.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnSave;
    EditText name;
    DatabaseReference reff;
    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSave = (Button) findViewById(R.id.save);
        name = (EditText) findViewById(R.id.name);
        newUser = new User();
        reff = FirebaseDatabase.getInstance().getReference().child("User");
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                newUser.setUsername(name.getText().toString());
                reff.push().setValue(newUser);
                Toast.makeText(MainActivity.this, "User created", Toast.LENGTH_LONG).show();
            }
        });
    }
}
