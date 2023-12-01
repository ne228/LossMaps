package com.example.myapplication_v5.service;

import androidx.annotation.NonNull;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;

public class PlaceMarkTapListener implements MapObjectTapListener {

    // Класс для создания обработчика событий
    // нажатия на маркер "Заявление"
    PlaceMarkController placeMarkController;
    public PlaceMarkTapListener(PlaceMarkController placeMarkController) {
        this.placeMarkController = placeMarkController;
    }

    @Override
    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {

        // Если объект - маркер, то обрабатываем событий
        if (mapObject instanceof PlacemarkMapObject) {
            PlacemarkMapObject placemarkMapObject = (PlacemarkMapObject)mapObject;
            placeMarkController.showPlacemark(placemarkMapObject);

            return true;
        }
        return true;

    }
}
