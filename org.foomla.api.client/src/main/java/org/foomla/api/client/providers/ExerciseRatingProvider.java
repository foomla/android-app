package org.foomla.api.client.providers;

import java.util.ArrayList;
import java.util.List;

import org.foomla.api.client.AuthorizationException;
import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.RemoteInvocationException;
import org.foomla.api.client.providers.base.EntityProvider;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.ExerciseRating;
import org.foomla.api.services.twizard.ExerciseRatingService;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

/**
 * Use ExerciseRatingServiceProvider instead.
 */
@Deprecated
public class ExerciseRatingProvider extends EntityProvider<ExerciseRating> {

    protected static class ExerciseRatingList extends ArrayList<ExerciseRating> { }

    public ExerciseRatingProvider(final FoomlaClient.RestConnectionSettings restConnectionSettings) {
        super(restConnectionSettings);
    }

    @SuppressWarnings("unchecked")
    public List<ExerciseRating> getExerciseRating(final Exercise exercise) throws RemoteInvocationException,
    AuthorizationException {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityListResource);
        clientResource.addSegment(String.valueOf(exercise.getId()));

        Representation representation = callResource(clientResource, Method.GET);

        return (List<ExerciseRating>) clientResource
                .toObject(representation, getEntityProviderClasses().entityListType);
    }

    @Override
    protected EntityProviderClasses getEntityProviderClasses() {
        return EntityProviderClasses.create(ExerciseRatingService.class, ExerciseRatingService.class,
                ExerciseRatingList.class, ExerciseRating.class);
    }
}
