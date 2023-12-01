package com.example.myapplication_v5.data;

import com.example.myapplication_v5.data.Statement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatementModel {

    // Это класс для получения данных  с сервера
    // После получения данных с сервера данный класс конверитуется в Statement с помощью метода toEntity
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String birthDate;
    private String features;

    private String applicantLastName;
    private String applicantFirstName;
    private String applicantPatronymic;


    private String dateTimeOfStatement;
    private double longitude;
    private double latitude;

    LocalDateTime toLocalDateTime(String stringDateTime){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime dateTime = LocalDateTime.parse(stringDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return  dateTime;
        }
        return null;
    }


    public Statement toEntity() {
        Statement statement = new Statement();
        statement.setId(id);
        statement.setLastName(lastName);
        statement.setFirstName(firstName);
        statement.setPatronymic(patronymic);

        statement.setBirthDate(toLocalDateTime(birthDate));
        statement.setFeatures(features);
        statement.setDateTimeOfStatement(toLocalDateTime(dateTimeOfStatement));
        statement.setApplicantLastName(applicantLastName);
        statement.setApplicantFirstName(applicantFirstName);
        statement.setApplicantPatronymic(applicantPatronymic);

        statement.setLatitude(latitude);
        statement.setLongitude(longitude);

        return statement;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getApplicantLastName() {
        return applicantLastName;
    }

    public void setApplicantLastName(String applicantLastName) {
        this.applicantLastName = applicantLastName;
    }

    public String getApplicantFirstName() {
        return applicantFirstName;
    }

    public void setApplicantFirstName(String applicantFirstName) {
        this.applicantFirstName = applicantFirstName;
    }

    public String getApplicantPatronymic() {
        return applicantPatronymic;
    }

    public void setApplicantPatronymic(String applicantPatronymic) {
        this.applicantPatronymic = applicantPatronymic;
    }

    public String getDateTimeOfStatement() {
        return dateTimeOfStatement;
    }

    public void setDateTimeOfStatement(String dateTimeOfStatement) {
        this.dateTimeOfStatement = dateTimeOfStatement;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
