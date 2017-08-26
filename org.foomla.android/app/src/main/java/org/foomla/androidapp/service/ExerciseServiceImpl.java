package org.foomla.androidapp.service;

import android.content.res.Resources;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;

import org.foomla.androidapp.R;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.TrainingPhase;
import org.foomla.androidapp.service.http.ExerciseHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExerciseServiceImpl implements ExerciseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private final Resources resources;
    private final Random random = new Random();

    private ExerciseHttpService exercisesService;

    private final List<Exercise> exercises = new ArrayList<>();

    public ExerciseServiceImpl(final Resources resources) throws IOException {
        this.resources = resources;

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        LOGGER.info(">>> calling: " + request.url().toString());
                        return chain.proceed(request);
                    }
                })
                .build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd.MM.yy HH:mm");

        exercisesService = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.exercise_url))
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(okHttpClient)
                .build()
                .create(ExerciseHttpService.class);
    }

    @Override
    public void list(final ExerciseService.Callback<List<Exercise>> callback, final boolean isPro) throws IOException {
        if (!exercises.isEmpty()) {
            callback.onResult(exercises);
        }

        exercises.clear();
        List<Exercise> list = loadExercises(isPro);
        exercises.addAll(list);
        callback.onResult(exercises);
    }

    private List<Exercise> loadExercises(boolean isPro) throws IOException {
        if (Locale.getDefault().getLanguage().equals(Locale.GERMAN.getLanguage())) {
            Call<List<Exercise>> exercises = isPro
                    ? exercisesService.getExercises(resources.getString(R.string.exercise_url_de_pro))
                    : exercisesService.getExercises(resources.getString(R.string.exercise_url_de_free));

            return exercises.execute().body();
        } else {
            Call<List<Exercise>> exercises = isPro
                    ? exercisesService.getExercises(resources.getString(R.string.exercise_url_en_pro))
                    : exercisesService.getExercises(resources.getString(R.string.exercise_url_en_free));

            return exercises.execute().body();
        }
    }

    @Override
    public List<Exercise> filter(final TrainingPhase trainingPhase) {
        return Lists.newArrayList(Collections2.filter(exercises, new Predicate<Exercise>() {
            @Override
            public boolean apply(Exercise input) {
                return input.getTrainingPhases().contains(trainingPhase);
            }
        }));
    }

    @Override
    public void random(final ExerciseService.Callback<Exercise> callback, boolean isPro) throws IOException {
        if (exercises.isEmpty()) {
            List<Exercise> collection = loadExercises(isPro);

            if (collection != null) {
                exercises.addAll(collection);
            }
        }

        if ((!exercises.isEmpty())) {
            int randomIndex = random.nextInt(exercises.size() - 1);
            callback.onResult(exercises.get(randomIndex));
        }
    }
}
