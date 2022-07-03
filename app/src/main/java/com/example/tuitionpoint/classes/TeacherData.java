package com.example.tuitionpoint.classes;

public class TeacherData {
    public String email, password, fullname, address, phone, study_level, institution;

    public TeacherData(String email, String password, String fullname, String address, String phone, String study_level, String institution) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.study_level = study_level;
        this.institution = institution;
    }
}
