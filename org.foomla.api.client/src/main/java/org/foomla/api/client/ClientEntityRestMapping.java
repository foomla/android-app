package org.foomla.api.client;

import org.foomla.api.client.entities.ExerciseImageImpl;
import org.foomla.api.client.entities.ExerciseImpl;
import org.foomla.api.client.entities.ExercisePropertyImpl;
import org.foomla.api.client.entities.ExerciseRatingImpl;
import org.foomla.api.client.entities.SocialLoginImpl;
import org.foomla.api.client.entities.TrainingImpl;
import org.foomla.api.client.entities.UserImpl;
import org.foomla.api.converters.EntityRestMapping;
import org.foomla.api.entities.SocialLogin;
import org.foomla.api.entities.User;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.ExerciseImage;
import org.foomla.api.entities.twizard.ExerciseProperty;
import org.foomla.api.entities.twizard.ExerciseRating;
import org.foomla.api.entities.twizard.Training;

public class ClientEntityRestMapping extends EntityRestMapping {

    static {
        map(User.class, UserImpl.class);
        map(SocialLogin.class, SocialLoginImpl.class);
        map(Exercise.class, ExerciseImpl.class);
        map(ExerciseImage.class, ExerciseImageImpl.class);
        map(ExerciseProperty.class, ExercisePropertyImpl.class);
        map(ExerciseRating.class, ExerciseRatingImpl.class);
        map(Training.class, TrainingImpl.class);
    }
}
