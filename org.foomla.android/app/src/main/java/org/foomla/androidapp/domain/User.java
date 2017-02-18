package org.foomla.androidapp.domain;

import java.io.Serializable;
import java.util.List;

public interface User extends Serializable, Entity {

    String getEmail();

    String getFirstname();

    @Override
    Integer getId();

    String getLastname();

    String getPassword();

    List<UserRole> getUserRoles();

    boolean isActive();

    void setEmail(String email);

    void setFirstname(String firstname);

    @Override
    void setId(Integer id);

    void setActive(boolean isActive);

    void setLastname(String lastname);

    void setPassword(String password);

    void setUserRoles(List<UserRole> userRoles);

}