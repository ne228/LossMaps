package com.example.myapplication_v5.tasks.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication_v5.R;
import com.example.myapplication_v5.data.Statement;
import com.example.myapplication_v5.service.PlaceMarkController;
import com.example.myapplication_v5.tasks.PostStatementTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;

import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class StatementFormHandler extends AppCompatActivity {

    private Map map;
    private Context context;
    private PlaceMarkController placeMarkController;
    private AlertDialog dialog;
    private Point point;



    public StatementFormHandler(){

    }

    // Класс, для управления формой заявления
    public StatementFormHandler(Map map, Context context,  PlaceMarkController placeMarkController ,Point point) {
        this.context = context;
        this.map = map;
        this.point = point;
        this.placeMarkController = placeMarkController;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);
    }


    // Показать форму для заполнения заявения
    public void showFormDialog(Point point) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.form_layout, null);


        Button submitButton = dialogView.findViewById(R.id.submit_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        // Обработчик нажатия на кнопку "Готово"
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ... Обработка нажатия на "Готово" ...
                handleFormSubmission(dialogView, point);
            }
        });

        // Обработчик нажатия на кнопку "Отмена"
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissFormDialog();
            }
        });

        // Установка Title формы
        builder.setView(dialogView)
                .setTitle("Введите данные о пропавшем и заявителе");

        // Показать форму
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();

        // Передвинуть камеру в точку нажатия
        map.move(
                new CameraPosition(point,
                        map.getCameraPosition().getZoom(),
                        0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, (float) 0.3),
                null
        );

    }

    // Обработка нажатия на "Готово"
    private void handleFormSubmission(View dialogView, Point point) {

        // Получаем все данные с форм
        final EditText editTextLastName = dialogView.findViewById(R.id.editTextLastName);
        final EditText editTextFirstName = dialogView.findViewById(R.id.editTextFirstName);
        final EditText editTextPatronymic = dialogView.findViewById(R.id.editTextPatronymic);
        final DatePicker datePickerBirth = dialogView.findViewById(R.id.datePickerBirth);
        final EditText editTextFeatures = dialogView.findViewById(R.id.editTextFeatures);
        final EditText editTextApplicantLastName = dialogView.findViewById(R.id.editTextApplicantLastName);
        final EditText editTextApplicantFirstName = dialogView.findViewById(R.id.editTextApplicantFirstName);
        final EditText editTextApplicantPatronymic = dialogView.findViewById(R.id.editTextApplicantPatronymic);




        // Получаем все данные с форм
        String lastName = editTextLastName.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String patronymic = editTextPatronymic.getText().toString();
        int birthYear = datePickerBirth.getYear();
        int birthMonth = datePickerBirth.getMonth() + 1;
        int birthDay = datePickerBirth.getDayOfMonth();
        String features = editTextFeatures.getText().toString();


        // Приводим дату к нужному формату
        LocalDateTime birthDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            birthDate = LocalDateTime.of(birthYear, birthMonth, birthDay,0,0,0);
        }


        String applicantLastName = editTextApplicantLastName.getText().toString();
        String applicantFirstName = editTextApplicantFirstName.getText().toString();
        String applicantPatronymic = editTextApplicantPatronymic.getText().toString();


        Statement statement = new Statement(lastName, firstName, patronymic, birthDate, features,
                applicantLastName, applicantFirstName, applicantPatronymic);
        statement.setLatitude(point.getLatitude());
        statement.setLongitude(point.getLongitude());

        // Если форма заполнена отправляем данные на сервер и ставим метку на карту
        if (statement.isValid()) {
            onFormSubmitted(statement);
            dismissFormDialog();
            Toast.makeText(context, "Метка поставлена", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("Пожалуйста, заполните все поля");
            Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    // Скрыть форму
    private void dismissFormDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    // При нажатии кнопки Готово
    private void onFormSubmitted(Statement statement) {


        // Преобразовываем все данные в формат json
        Gson gson = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new Statement.LocalDateTimeAdapter())
                    .create();
        }

        String jsonStatement =  gson.toJson(statement);

        // Класс для асинхронной отправки запроса к серверу
        PostStatementTask postStatementTask = new PostStatementTask(jsonStatement);


        // Отправляем данные на сервер
        try {
            postStatementTask.execute();
            postStatementTask.get();
            int res = postStatementTask.getResult();
            //  Если ответ не пришел, выдаем ошибку и не ставим маркер
            if (res == 0){
                Toast.makeText(context, "Ошибка при отправке данных на сервер", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Если все успешно, ставим маркер на карту
        placeMarkController.addPlacemark_Record(statement);
    }


}
