package com.example.tuitionpoint.classes;

public class TuitionRequest {
    public String name, student_class, subject, days, salary, address, description, status;

    public TuitionRequest(String name, String student_class, String subject, String days, String salary, String address, String description, String status) {
        this.name = name;
        this.student_class = student_class;
        this.subject = subject;
        this.days = days;
        this.salary = salary;
        this.address = address;
        this.description = description;
        this.status = status;
    }
}
