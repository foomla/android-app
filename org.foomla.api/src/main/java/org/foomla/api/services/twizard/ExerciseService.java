package org.foomla.api.services.twizard;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.foomla.api.annotations.Secured;
import org.foomla.api.entities.UserRole;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.services.FoomlaService;

@Path("/exercise")
public interface ExerciseService extends FoomlaService {

    @GET
    @Path("/{id}")
    @Produces("application/json")
    Exercise getById(@PathParam("id") int id);

    @DELETE
    @Path("/{id}")
    @Secured(UserRole.ADMIN)
    void remove(@PathParam("id") int id);

    @PUT
    @Consumes("application/json")
    @Secured(UserRole.ADMIN)
    void update(Exercise exercise);
}
