package com.example.myapplication_v5.tasks;

import android.os.AsyncTask;
import com.example.myapplication_v5.data.Statement;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostStatementTask extends AsyncTask<Void, Void, String> {

    int result;
    String jsonStatement;

    public PostStatementTask(String jsonStatement) {
        this.jsonStatement = jsonStatement;
    }

    public int getResult() {
        return result;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            // Создание URL-объекта и открытие соединения
            URL url = new URL("https://firetests.ru/lossmaps/api/statements"); // Замените "ваш_url" на реальный URL
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса на POST
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            // Включение возможности отправки данных
            urlConnection.setDoOutput(true);


            // Получение потока вывода и запись JSON-строки в тело запроса
            try (OutputStream os = urlConnection.getOutputStream();
                 OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
                osw.write(jsonStatement);
                osw.flush();
            }

            // Получение ответа от сервера
            int responseCode = urlConnection.getResponseCode();


            if (responseCode == 201) {
                result = responseCode;
                // Обработка успешного ответа
            } else {
                // Обработка ошибки
                System.err.println("HTTP error code: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            // Обработка ошибок ввода-вывода (IO)
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // Обработка остальных исключений
            e.printStackTrace();
            return null;
        }

        return null;

    }



    @Override
    protected void onPostExecute(String result) {
        // Здесь вы можете обработать полученную JSON-строку
        if (result != null) {
            // Ваш код обработки JSON
            // Например, вы можете использовать библиотеку Gson для преобразования JSON в объекты Java
        } else {
            // Обработка ошибки
        }
    }
}
