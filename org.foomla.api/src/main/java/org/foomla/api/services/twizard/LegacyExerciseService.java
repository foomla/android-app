package org.foomla.api.services.twizard;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.services.FoomlaService;

@Path("/exercice")
public interface LegacyExerciseService extends FoomlaService {

    @GET
    @Path("/{id}")
    @Produces("application/json")
    Exercise getById(@PathParam("id") int id);
}
