package com.example.finalproject;

public class UserData {
    int id,age;
    String Email;
    String FirstName;
    String LastName;
    String Password;
    String Note;

    public UserData(String FirstName, String LastName, String Email, String Password){
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Password = Password;
        this.Email = Email;
    }


    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        this.Note = note;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = Email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }



}