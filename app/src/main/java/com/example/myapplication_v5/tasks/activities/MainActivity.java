package com.example.myapplication_v5.tasks.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.myapplication_v5.service.AsyncTaskRunner;
import com.example.myapplication_v5.R;
import com.example.myapplication_v5.data.Statement;
import com.example.myapplication_v5.data.StatementAdapter;
import com.example.myapplication_v5.data.StatementModel;
import com.example.myapplication_v5.service.LocationHelper;
import com.example.myapplication_v5.service.PlaceMarkController;

import com.example.myapplication_v5.tasks.GetStatementsTask;
//import com.example.myapplication_v5.tasks.PostStatementsTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationHelper locationHelper;
    private PlaceMarkController placeMarkController;
    private Map map;
    private AsyncTaskRunner asyncTaskRunner;

    MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("LossMaps");
        setContentView(R.layout.activity_main);

        // Инициализация Я.Карт
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapview);
        map = mapView.getMap();

        map.move(
                new CameraPosition(new Point(0, 0),
                        5.0f,
                        0.0f,
                        0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null
        );

        // Инициализация класса, который показывает текущие координаты
        locationHelper = new LocationHelper(this);

        // Класс для асинхронной обработки запроса текущего местоположения
        asyncTaskRunner = new AsyncTaskRunner();

        // Класс, контролирующий метки, события нажатия на них, и выдачу информации о метке.
        placeMarkController = new PlaceMarkController(map, this);


        // Получение разрешения на геолокацию
        // Проверка и запрос разрешений на местоположение
        if (checkLocationPermission()) {
            // Если разрешения уже предоставлены, начать получение местоположения
            locationHelper.getCurrentLocation();
        } else {
            // Если разрешения не предоставлены, запросить их
            requestLocationPermission();
        }

        // Инициализация кнопки MyLocation
        initMyLocationButton();

        // Рза в секунду установка маркера собственной геолокации
        asyncTaskRunner.startAsyncTask(this::setUserLocationMarker,1000);

        // Загрузка данных с сервера и устнавока маркеров
        try {
            loadPlacemarksFromServer();
        }
        catch (Exception e){
            Toast.makeText(this, "Ошибка при получении данных с сервера", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadPlacemarksFromServer(){
        GetStatementsTask task = new GetStatementsTask();
        String json;
        try {
            task.execute();
             task.get();
            json = task.getResult();

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Gson gson = StatementAdapter.createGson();

        List<StatementModel> statementModelList = new Gson()
                .fromJson(json, new TypeToken<List<StatementModel>>(){}.getType());

        for (StatementModel statementModel : statementModelList){
            Statement statement = statementModel.toEntity();
            placeMarkController.addPlacemark_Record(statement);
        }
    }



    // Следующие 3 метода нужны для запроса разрешения на доступ к геолокации
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешения получены, начать получение местоположения
                locationHelper.getCurrentLocation();
            } else {
                // Разрешения не предоставлены, можно предпринять дополнительные шаги (показать сообщение и т.д.)
            }
        }
    }


    // Инициализация кнопки MyLocation
    private void initMyLocationButton() {
        ImageButton myLocationButton = findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToUserLocation();
            }
        });
    }


    // Установка маркера текущей геопозиции на координаты текущей геопозиции
    // Выполняется ра в 1000 мсек
    private void setUserLocationMarker(){
       // Получите долготу текущего местоположения пользователя
        Point point = locationHelper.getCurrentLocation();
        placeMarkController.addPlacemark_MyGeo(point);
    }
    // Передвинуть камеру на текущее половжение геолокации
    private void moveToUserLocation() {
        Map map = mapView.getMap();
        Point point = locationHelper.getCurrentLocation();

        map.move(
                new CameraPosition(point,
                        map.getCameraPosition().getZoom(),
                        0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, (float) 0.3),
                null
        );



    }



    // Методы, необходимые для корректного старта и завершения Я.Карт
    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}