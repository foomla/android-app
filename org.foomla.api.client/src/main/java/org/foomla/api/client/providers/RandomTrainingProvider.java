package org.foomla.api.client.providers;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.providers.base.EntityProvider;
import org.foomla.api.client.service.TrainingServiceProvider;
import org.foomla.api.entities.twizard.Training;
import org.foomla.api.services.twizard.RandomTrainingService;

/**
 * Use {@link TrainingServiceProvider} instead.
 */
@Deprecated
public class RandomTrainingProvider extends EntityProvider<Training> {

    public RandomTrainingProvider(FoomlaClient.RestConnectionSettings restConnectionSettings) {
        super(restConnectionSettings);
    }

    @Override
    protected EntityProviderClasses getEntityProviderClasses() {
        return EntityProviderClasses.create(null, RandomTrainingService.class, null,
                Training.class);
    }
}
