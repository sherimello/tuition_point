package com.example.tuitionpoint.classes;

public class Constants {
    public String databaseAddress = "https://tuition-point-57df4-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public String getUserName(String mail) {
        return mail.substring(0, mail.indexOf('@'));
    }
}
