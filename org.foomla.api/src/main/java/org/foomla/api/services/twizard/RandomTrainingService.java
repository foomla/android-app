package org.foomla.api.services.twizard;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.foomla.api.entities.twizard.Training;
import org.foomla.api.services.FoomlaService;

@Path("/training/random")
public interface RandomTrainingService extends FoomlaService {

    @GET
    @Produces("application/json")
    Training getRandom();
}

