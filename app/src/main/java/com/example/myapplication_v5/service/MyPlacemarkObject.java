package com.example.myapplication_v5.service;

import com.yandex.mapkit.map.PlacemarkMapObject;

public class MyPlacemarkObject
{
    // Этот класс нужен для сохранения обработчиков событый для каждой метки
    PlacemarkMapObject placemarkMapObject;
    PlaceMarkTapListener placeMarkTapListener;
    PlaceMarkController placeMarkController;



    public MyPlacemarkObject(PlacemarkMapObject placemarkMapObject, PlaceMarkTapListener placeMarkTapListener,
                             PlaceMarkController placeMarkController) {
        this.placemarkMapObject = placemarkMapObject;
        this.placeMarkTapListener = placeMarkTapListener;
        this.placeMarkController = placeMarkController;

        placemarkMapObject.addTapListener(placeMarkTapListener);
    }

    // Удаляем старый обработчик событий и добавляем новый
    public void RefreshLitener(){
        placemarkMapObject.removeTapListener(placeMarkTapListener);
        placeMarkTapListener = new PlaceMarkTapListener(placeMarkController);
        placemarkMapObject.addTapListener(placeMarkTapListener);

    }


}
