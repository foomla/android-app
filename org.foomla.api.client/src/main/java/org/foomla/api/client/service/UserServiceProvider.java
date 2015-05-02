package org.foomla.api.client.service;

import java.util.ArrayList;
import java.util.List;

import org.foomla.api.client.FoomlaClient.RestConnectionSettings;
import org.foomla.api.entities.User;
import org.foomla.api.services.UserService;

public class UserServiceProvider extends ServiceProvider implements UserService {

    protected static class UserList extends ArrayList<User> {

    }

    public UserServiceProvider(RestConnectionSettings settings) {
        super(settings);
    }

    @Override
    public Boolean authenticate(UserCredentials userCredentials) {
        return url("/users/authenticate").post(userCredentials).as(Boolean.class);
    }

    @Override
    public void changePassword(String password) {
        url("/users/password/change/").put(password);
    }

    @Override
    public void confirm(String token) {
        url("/users/confirm/{}", token).get();
    }

    @Override
    public void create(User user) {
        url("/users").post(user);
    }

    @Override
    public List<User> getAll() {
        return url("/users").get().as(UserList.class);
    }

    @Override
    public User getByEmail(String email) {
        return url("/users/email/{}", email).get().as(User.class);
    }

    @Override
    public User getById(int id) {
        return url("/users/{}", id).get().as(User.class);
    }

    @Override
    public User getBySocialUserId(String socialUserId) {
        return url("/users/social-login/{}", socialUserId).get().as(User.class);
    }

    @Override
    public User getCurrent() {
        return url("/users/current").get().as(User.class);
    }

    @Override
    public void passwordLost(String username) {
        url("/users/password/lost/{}", username).get();
    }

    @Override
    public void register(User user) {
        url("/users/register").post(user);
    }

    @Override
    public void remove(int id) {
        url("/users/{}", id).delete();
    }

    @Override
    public void removeCurrent() {
        url("/users/current").delete();
    }

    @Override
    public void update(int id, User exercise) {
        url("/users/{}", id).put(exercise);
    }

}
