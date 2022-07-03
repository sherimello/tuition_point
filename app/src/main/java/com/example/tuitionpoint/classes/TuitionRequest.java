package com.example.tuitionpoint.classes;

public class TuitionRequest {
    public String name, district, student_class, subject, days, salary, address, description, status, userName;

    public TuitionRequest(String name, String district, String student_class, String subject, String days, String salary, String address, String description, String status, String userName) {
        this.name = name;
        this.district = district;
        this.student_class = student_class;
        this.subject = subject;
        this.days = days;
        this.salary = salary;
        this.address = address;
        this.description = description;
        this.status = status;
        this.userName = userName;
    }
}
