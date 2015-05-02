package org.foomla.api.entities;

import java.io.Serializable;
import java.util.List;

import org.foomla.api.entities.base.Entity;

public interface User extends Serializable, Entity {

    String getEmail();

    String getFirstname();

    @Override
    Integer getId();

    String getLastname();

    String getPassword();

    SocialLogin getSocialLogin();

    List<UserRole> getUserRoles();

    boolean isActive();

    void setEmail(String email);

    void setFirstname(String firstname);

    @Override
    void setId(Integer id);

    void setActive(boolean isActive);

    void setLastname(String lastname);

    void setPassword(String password);

    void setSocialLogin(SocialLogin socialLogin);

    void setUserRoles(List<UserRole> userRoles);

}