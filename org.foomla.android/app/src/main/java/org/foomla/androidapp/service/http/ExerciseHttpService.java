package org.foomla.androidapp.service.http;

import org.foomla.androidapp.domain.Exercise;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExerciseHttpService {

    @GET("/app/exercises/{version}")
    Call<List<Exercise>> getExercises(@Path("version") String version);
}
