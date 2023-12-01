package com.example.myapplication_v5.service;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.myapplication_v5.tasks.activities.StatementFormHandler;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;

public class MapTapListener implements InputListener {

    // Класс для обработки событий нажатия на карту
    android.content.Context context;
    Map map;

   PlaceMarkController placeMarkController;


    public MapTapListener(Map map,  android.content.Context context, PlaceMarkController placeMarkController) {
        this.context = context;
        this.map = map;
        this.placeMarkController = placeMarkController;
    }

    // Вызывается при событии нажатия на карту
    @Override
    public void onMapTap(@NonNull Map map, @NonNull Point point) {

        Point yandexPoint = new Point(point.getLatitude(), point.getLongitude());
        // Вызываем форму
        // Задачи обработки формы берет на себя StatementFormHandler
        StatementFormHandler formHandler = new StatementFormHandler(this.map, context, placeMarkController, point);
        Log.d("MapTap", " tapped!");
        formHandler.showFormDialog(point);
    }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

    }




}
