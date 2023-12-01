package com.example.myapplication_v5.service;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.tasks.Task;

public class AsyncTaskRunner {

    // Это класс для асинхрнного выполнения запроса геолокации
    // и установки метки текущей геопозиции
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    // Ваш собственный интерфейс RepeatableTask
    public interface RepeatableTask {
        void execute();
    }

    // Передаем метод task в метод
    // и выполняем задачу в методе run
    public void startAsyncTask(RepeatableTask task, long intervalMillis) {
        // Создайте новый Runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                // Выполните переданный метод
                task.execute();

                // Планируем следующий запуск через intervalMillis
                handler.postDelayed(this, intervalMillis);
            }
        };

        // Запустите первый раз через intervalMillis
        handler.postDelayed(runnable, intervalMillis);
    }


}
