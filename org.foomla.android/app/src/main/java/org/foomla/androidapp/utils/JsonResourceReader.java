package org.foomla.androidapp.utils;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;

public class JsonResourceReader {

    private final Resources resources;
    private final int resId;

    public JsonResourceReader(final Resources resources, final int resId) {
        this.resources = resources;
        this.resId = resId;
    }

    public <T> T read(Class<T> type) throws IOException {
        final String json = readJson();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        final Gson gson = builder.create();
        return gson.fromJson(json, type);
    }

    private String readJson() throws IOException {
        InputStream in = resources.openRawResource(resId);
        return IOUtils.toString(in);
    }
}
