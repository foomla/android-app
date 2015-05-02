package org.foomla.api.services.twizard;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.foomla.api.annotations.Secured;
import org.foomla.api.entities.UserRole;
import org.foomla.api.entities.twizard.ExerciseRating;
import org.foomla.api.services.FoomlaService;

@Path("/exercise-ratings")
public interface ExerciseRatingService extends FoomlaService {

    @POST
    @Consumes("application/json")
    @Path("/{id}")
    @Secured(UserRole.USER)
    void create(@PathParam("id") int exerciseId, ExerciseRating exerciseRating);

    @GET
    @Path("/{id}")
    @Produces("application/json")
    ExerciseRatingList getAllByExerciseId(@PathParam("id") int exerciseId);
}
