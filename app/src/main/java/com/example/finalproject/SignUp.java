package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SignUp extends AppCompatActivity {

    private Button confirmButton, cancelButton;
    public EditText mEmail, fName, lName, password;
    String userEmail, userPassword;
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmail = (EditText)findViewById(R.id.email1);
        String regEmail = mEmail.getText().toString();

        fName = (EditText)findViewById(R.id.firstName1);
        String regFirstName = fName.getText().toString();

        lName = (EditText)findViewById(R.id.lastName1);
        String regLastName = lName.getText().toString();

        password = (EditText)findViewById(R.id.password3);
        String regPass = password.getText().toString();

        userEmail = "canada2038@gmail.com";
        userPassword = "fall2020";

        confirmButton = (Button) findViewById(R.id.confirmBtn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userEmail, userPassword);
                    }
                });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(userEmail));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(mEmail.getText().toString().trim()));
                    message.setText(fName.getText().toString().trim());
                    message.setText(lName.getText().toString().trim());
                    message.setText(password.getText().toString().trim());

                    new SendMail().execute(message);

                } catch (MessagingException e) {
                    e.printStackTrace();
                }

                int check =Integer.parseInt( dbHelper.getLoginData(regEmail, regPass));
                if(check>0){
                    toastMessage("Account Exists");
                }
                else {
                    if (fName.equals(null)|| lName.equals(null) || regEmail.equals(null)|| regPass.equals(null)) {
                        toastMessage("Fill all Fields");
                    }
                    else{
                        UserData currentUser = new UserData(fName.getText().toString(),
                                lName.getText().toString(),
                                mEmail.getText().toString(),
                                password.getText().toString());
                        boolean status = dbHelper.addUser(currentUser);
                        if (status) {
                            toastMessage("Successful Registration");
                        } else {
                            toastMessage("Unable to Register");
                        }
                    }
                }
            }
        });

        cancelButton = (Button) findViewById(R.id.cancelBtn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (SignUp.this, MainActivity.class));
            }
        });
    }

    private class SendMail extends AsyncTask<Message, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SignUp.this,
                    "Please Wait", "Sending Mail. . . ", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success!!";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error!!";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s.equals("Success!!")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setCancelable(false);
                builder.setMessage("Confirmation Email Sent!!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        fName.setText("");
                        lName.setText("");
                        mEmail.setText("");
                        password.setText("");
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}