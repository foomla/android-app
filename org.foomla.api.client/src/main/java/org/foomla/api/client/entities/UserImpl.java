package org.foomla.api.client.entities;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.foomla.api.entities.SocialLogin;
import org.foomla.api.entities.User;
import org.foomla.api.entities.UserRole;

@JsonDeserialize(as = UserImpl.class)
public class UserImpl implements User {

    private static final long serialVersionUID = 1L;

    private boolean active;

    private String email;

    private String firstname;

    private Integer id;

    private String lastname;

    private String password;

    private SocialLogin socialLogin;

    private List<UserRole> userRoles;

    public UserImpl() {
    }

    public UserImpl(String email) {
        this.email = email;
    }

    public UserImpl(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.foomla.backend.api.domain.User#getEmail()
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.foomla.api.entities.User#getEmail()
     */
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @JsonIgnore
    public String getFullname() {
        return this.firstname + " " + lastname;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.foomla.backend.api.domain.User#getPassword()
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.foomla.api.entities.User#getPassword()
     */
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public SocialLogin getSocialLogin() {
        return socialLogin;
    }

    @Override
    public List<UserRole> getUserRoles() {
        return this.userRoles;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.foomla.backend.api.domain.User#setEmail(java.lang.String)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.foomla.api.entities.User#setEmail(java.lang.String)
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.foomla.backend.api.domain.User#setLastname(java.lang.String)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.foomla.api.entities.User#setLastname(java.lang.String)
     */
    @Override
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setSocialLogin(SocialLogin socialLogin) {
        this.socialLogin = socialLogin;
    }

    @Override
    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
