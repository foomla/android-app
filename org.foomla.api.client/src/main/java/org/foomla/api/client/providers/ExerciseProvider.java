package org.foomla.api.client.providers;

import java.util.ArrayList;
import java.util.List;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.providers.base.EntityProvider;
import org.foomla.api.client.service.ExerciseServiceProvider;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.TrainingPhase;
import org.foomla.api.services.twizard.ExerciseListService;
import org.foomla.api.services.twizard.ExerciseService;
import org.restlet.resource.ClientResource;


/**
 * Use {@link ExerciseServiceProvider} instead.
 */
@Deprecated
public class ExerciseProvider extends EntityProvider<Exercise> {

    protected static class ExerciseList extends ArrayList<Exercise> {

    }

    public ExerciseProvider(FoomlaClient.RestConnectionSettings restConnectionSettings) {
        super(restConnectionSettings);
    }

    public List<Exercise> getAllByTrainingPhase(TrainingPhase trainingPhase) {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityListResource);
        clientResource.addQueryParameter(ExerciseListService.ATTRIBUTE_TRAININGPHASE, trainingPhase.name());
        return (List<Exercise>) clientResource.get(getEntityProviderClasses().entityListType);
    }

    @Override
    public EntityProviderClasses getEntityProviderClasses() {
        return EntityProviderClasses.create(ExerciseListService.class, ExerciseService.class, ExerciseList.class,
                Exercise.class);
    }

    public Exercise getLatest() {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityListResource);
        clientResource.addSegment(ExerciseListService.PATH_LATEST.replace("/", ""));
        return (Exercise) clientResource.get(getEntityProviderClasses().entityType);
    }
}
