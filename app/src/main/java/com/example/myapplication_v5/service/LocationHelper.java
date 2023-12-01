package com.example.myapplication_v5.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.yandex.mapkit.geometry.Point;

public class LocationHelper {

    // Это класс для получения координаты текущей геопозиции
    private final Context context;
    private final LocationListener locationListener;

    public LocationHelper(Context context) {
        this.context = context;
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Обработка изменения местоположения
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Обработка изменения статуса местоположения (например, отключение GPS)
            }

            @Override
            public void onProviderEnabled(String provider) {
                // Обработка включения провайдера местоположения
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Обработка выключения провайдера местоположения
            }
        };
    }



    public void requestLocationUpdates() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Проверка разрешений
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // Обработка отсутствия необходимых разрешений
            return;
        }

        // Запрос обновлений местоположения
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void stopLocationUpdates() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }


    // Получаем текущую геопозиции
    public Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Проверка разрешений
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // Обработка отсутствия необходимых разрешений
            return null;
        }

        // Получение последнего известного местоположения
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    // Метод, который можно вызвать для получения текущих координат пользователя
    // Преобразуме Location в Point, который более понятен для Я.Карт
    public Point getCurrentLocation() {
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            double userLatitude = lastKnownLocation.getLatitude();
            double userLongitude = lastKnownLocation.getLongitude();
            return new Point(userLatitude, userLongitude);
            // Используйте userLatitude и userLongitude по вашему усмотрению
        } else {
            // Последнее известное местоположение не доступно, запросим обновления местоположения
            return null;
        }
    }


}
