package org.foomla.api.services.twizard;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.foomla.api.annotations.Secured;
import org.foomla.api.entities.UserRole;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.services.FoomlaService;

@Path("/exercises")
public interface ExerciseListService extends FoomlaService {

    String ATTRIBUTE_TRAININGPHASE = "trainingphase";
    String PATH_LATEST = "/latest";

    @POST
    @Consumes("application/json")
    @Secured(UserRole.ADMIN)
    void create(Exercise exercise);

    @GET
    @Produces("application/json")
    ExerciseList getAll(@QueryParam(ATTRIBUTE_TRAININGPHASE) String phase);

    @GET
    @Produces("application/json")
    @Path(PATH_LATEST)
    Exercise getLatest();
}
