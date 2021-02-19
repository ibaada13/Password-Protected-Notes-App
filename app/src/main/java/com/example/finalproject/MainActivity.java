package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button signinBtn;
    private Button signupBtn;
    private EditText email, password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText)findViewById(R.id.username1);
        password = (EditText)findViewById(R.id.password1);

        signinBtn = (Button) findViewById(R.id.signInBtn);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if(userEmail.isEmpty() || userPassword.isEmpty()){
                    toastMessage("Fill All Fields");
                    if (!userEmail.matches(emailPattern)){
                        toastMessage("Invalid Email");
                    }
                }
                else if(userEmail.matches(emailPattern)){
                    int status =Integer.parseInt( dbHelper.getLoginData(userEmail, userPassword));
                    if (status>0) {
                        toastMessage("Login Successful");
                        Intent i = new Intent(getApplicationContext(), SignIn.class);
                        startActivity(i);
                    } else {
                        toastMessage("Not Registered");
                    }
                }
            }
        });

        signupBtn = (Button) findViewById(R.id.signUpBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (MainActivity.this, SignUp.class));
            }
        });


    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}