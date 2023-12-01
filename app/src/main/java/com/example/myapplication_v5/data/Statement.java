package com.example.myapplication_v5.data;

import android.os.Build;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.*;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statement implements Serializable {

    // Это класс для хранения данных заявления
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime birthDate;
    private String features;

    private String applicantLastName;
    private String applicantFirstName;
    private String applicantPatronymic;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime dateTimeOfStatement;
    private double longitude;
    private double latitude;

   public Statement(){

   }



    public Statement(String lastName, String firstName, String patronymic, LocalDateTime birthDate,
                     String features, String applicantLastName,
                     String applicantFirstName, String applicantPatronymic) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.features = features;

        this.applicantLastName = applicantLastName;
        this.applicantFirstName = applicantFirstName;
        this.applicantPatronymic = applicantPatronymic;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.dateTimeOfStatement = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // Геттеры и сеттеры...

    // Метод для проверки, что все поля заполнены
    public boolean isValid() {
        return lastName != null && !lastName.isEmpty()
                && firstName != null && !firstName.isEmpty()
                && patronymic != null && !patronymic.isEmpty()
                && birthDate != null
                && features != null && !features.isEmpty()

                && applicantLastName != null && !applicantLastName.isEmpty()
                && applicantFirstName != null && !applicantFirstName.isEmpty()
                && applicantPatronymic != null && !applicantPatronymic.isEmpty();
    }


    public LocalDateTime getDateTimeOfStatement() {
        return dateTimeOfStatement;
    }

    public void setDateTimeOfStatement(LocalDateTime dateTimeOfStatement) {
        this.dateTimeOfStatement = dateTimeOfStatement;
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

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
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

    public static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new JsonPrimitive(formatter.format(src));
            }

            return null;
        }


        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }
    }
}
