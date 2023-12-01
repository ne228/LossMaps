package com.example.myapplication_v5;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("cc67be39-a723-4fad-9358-6a49911f9817");
    }
}
