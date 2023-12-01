package com.example.myapplication_v5.data;

import android.os.Build;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


// Это класс для перевода данных заявления в json Формат
public class StatementAdapter {
    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        }
        return gsonBuilder.create();
    }

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return LocalDate.parse(json.getAsString(), formatter);
            }
            return null;
        }
    }

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new JsonPrimitive(formatter.format(src));
            }
            return null;
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return LocalDateTime.parse(json.getAsString(), formatter);
            }
            return null;
        }
    }
}
