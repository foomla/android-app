package org.foomla.api.client.providers;

import java.util.ArrayList;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.providers.base.EntityProvider;
import org.foomla.api.client.service.TrainingServiceProvider;
import org.foomla.api.entities.twizard.Training;
import org.foomla.api.services.twizard.TrainingService;
import org.restlet.resource.ClientResource;

/**
 * Use {@link TrainingServiceProvider} instead.
 */
@Deprecated
public class TrainingProvider extends EntityProvider<Training> {

    protected static class TrainingList extends ArrayList<Training> { }

    public TrainingProvider(final FoomlaClient.RestConnectionSettings restConnectionSettings) {
        super(restConnectionSettings);
    }

    @Override
    public EntityProviderClasses getEntityProviderClasses() {
        return EntityProviderClasses.create(TrainingService.class, null, TrainingList.class, Training.class);
    }

    public Training saveTraining(final Training training) {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityListResource);
        return (Training) clientResource.post(training, getEntityProviderClasses().entityListType);
    }
}
