package com.example.myapplication_v5.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.myapplication_v5.R;
import com.example.myapplication_v5.data.Statement;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PlaceMarkController {

    // Класс, который управляет всеми метками
    private Map map;
    private MapTapListener mapTapListener;
    private Context context;
    ArrayList<MyPlacemarkObject> MyPlacemarkObjects;

    public PlaceMarkController(Map map, Context context) {
        this.context = context;
        this.map = map;
        mapTapListener = new MapTapListener(map, context, this);
        map.addInputListener(mapTapListener);
        MyPlacemarkObjects = new ArrayList<>();
    }

    // Это метод для установки маркера текущей геопозиции
    public void addPlacemark_MyGeo(Point location) {
        MapObject placemark = map.
                getMapObjects().addPlacemark(location,
                        ImageProvider.fromResource(
                                context,
                                R.drawable.icons8_48));

        // Установите метку как "перетаскиваемую"
        placemark.setDraggable(true);

        // Добавьте слушателя для обработки событий перетаскивания
        placemark.addTapListener((mapObject, point) -> {
            // Обработайте событие нажатия на метку
            // Возможно, откройте информационное окно или выполните другие действия
            return false;
        });
    }

    // Показать информацию в метке "Заявление"
    public void showPlacemark(PlacemarkMapObject placemark){
        Log.d("MapTap", "PLACEMARK tapped!");

        // Получаем данные с метки
        Statement statement = (Statement) placemark.getUserData();

        // Создаем форму с информацией
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.statement_layout, null);

        // Получение элементов интерфейса из макета
        TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
        TextView lastNameTextView = dialogView.findViewById(R.id.lastNameTextView);
        TextView firstNameTextView = dialogView.findViewById(R.id.firstNameTextView);
        TextView patronymicTextView = dialogView.findViewById(R.id.patronymicTextView);
        TextView birthDateTextView = dialogView.findViewById(R.id.birthDateTextView);
        TextView featuresTextView = dialogView.findViewById(R.id.featuresTextView);

        TextView applicantLastNameTextView = dialogView.findViewById(R.id.applicantLastNameTextView);
        TextView applicantFirstNameTextView = dialogView.findViewById(R.id.applicantFirstNameTextView);
        TextView applicantPatronymicTextView = dialogView.findViewById(R.id.applicantPatronymicTextView);
        TextView dateTimeOfStatementTextView = dialogView.findViewById(R.id.dateTimeOfStatementTextView);

        // Заполнение данными
        titleTextView.setText("Информация о заявлении №" + statement.getId());
        lastNameTextView.setText("Фамилия: " + statement.getLastName());
        firstNameTextView.setText("Имя: " + statement.getFirstName());
        patronymicTextView.setText("Отчество: " + statement.getPatronymic());
        DateTimeFormatter dateFormatter;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            birthDateTextView.setText("Дата рождения: " + statement.getBirthDate().format(dateFormatter));
        }

        featuresTextView.setText("Приметы: " + statement.getFeatures());

        applicantLastNameTextView.setText("Фамилия заявителя: " + statement.getApplicantLastName());
        applicantFirstNameTextView.setText("Имя заявителя: " + statement.getApplicantFirstName());
        applicantPatronymicTextView.setText("Отчество заявителя: " + statement.getApplicantPatronymic());
        DateTimeFormatter dateTimeFormatter;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HHч mmм");
            dateTimeOfStatementTextView.setText("Дата и время заявления: " + statement.getDateTimeOfStatement().format(dateTimeFormatter));
        }

        // Перемещаем камеру в нажатую точку
        Point point = new Point(statement.getLatitude(),statement.getLongitude());
        map.move(
                new CameraPosition(point,
                        map.getCameraPosition().getZoom(),
                        0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, (float) 0.3),
                null
        );

        // Создание AlertDialog
        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Закрытие диалога
                        Refresh();
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    // Создать метку "Заявление" и добавить данные в него
    public void addPlacemark_Record(Statement statement) {
        // Создайте метку с изображением
        Point location = new Point(statement.getLatitude(),statement.getLongitude());
        MapObject placemark = map
                .getMapObjects().addPlacemark(location,
                        ImageProvider.fromResource(
                                context,
                                R.drawable.black_marker_50));

        placemark.setDraggable(true);
        placemark.setUserData(statement);

        // Добавляем метку и его обработчик события в массив всех меток
        MyPlacemarkObject placemarkObject = new MyPlacemarkObject((PlacemarkMapObject) placemark, new PlaceMarkTapListener(this), this);
        MyPlacemarkObjects.add(placemarkObject);
    }

    // При каждом добавлении метки
    // обновляем все обработчики событий
    public void Refresh(){
        // Обновляем обработичк события на карту
        map.removeInputListener(mapTapListener);
        mapTapListener = null;
        mapTapListener = new MapTapListener(map, context, this);
        map.addInputListener(mapTapListener);

        // Обновляем все маркеры "Заявление"
        for (MyPlacemarkObject myPlacemarkObject: MyPlacemarkObjects)
            myPlacemarkObject.RefreshLitener();
    }
}
