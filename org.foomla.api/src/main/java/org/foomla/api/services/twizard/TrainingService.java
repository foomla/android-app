package org.foomla.api.services.twizard;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.foomla.api.annotations.Secured;
import org.foomla.api.entities.UserRole;
import org.foomla.api.entities.twizard.Training;
import org.foomla.api.services.FoomlaService;

@Path("/trainings")
@Secured(UserRole.USER)
public interface TrainingService extends FoomlaService {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    Training create(Training training);

    @GET
    @Produces("application/json")
    List<Training> getAll();

    @GET
    @Path("/{id}")
    @Produces("application/json")
    Training getById(@PathParam("id") int id);

    @DELETE
    @Path("/{id}")
    void remove(@PathParam("id") int id);

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    void update(@PathParam("id") int id, Training training);
}
