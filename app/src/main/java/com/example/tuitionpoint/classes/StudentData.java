package com.example.tuitionpoint.classes;

public class StudentData {
    public String email, password, fullname, address, phone, study_level, institution, student_ID;

    public StudentData(String email, String password, String fullname, String address, String phone, String study_level, String institution, String student_ID) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.study_level = study_level;
        this.institution = institution;
        this.student_ID = student_ID;
    }
}
