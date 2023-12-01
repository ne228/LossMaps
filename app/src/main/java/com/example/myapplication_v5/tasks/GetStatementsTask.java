package com.example.myapplication_v5.tasks;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetStatementsTask extends AsyncTask<Void, Void, String> {

    String result;

    public String getResult() {
        return result;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            // Задайте URL вашего запроса
            URL url = new URL("https://firetests.ru/lossmaps/api/statements");

            // Откройте соединение
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                // Установите метод запроса (GET, POST и т. д.)
                urlConnection.setRequestMethod("GET");

                // Получите ответ от сервера
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                this.result = result.toString();
                return result.toString();
            } finally {
                // Не забудьте закрыть соединение
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
