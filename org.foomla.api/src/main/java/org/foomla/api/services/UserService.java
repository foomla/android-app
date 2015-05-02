package org.foomla.api.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.foomla.api.annotations.Secured;
import org.foomla.api.entities.User;
import org.foomla.api.entities.UserRole;

@Path("/users")
@Secured(UserRole.ADMIN)
public interface UserService extends FoomlaService {

    public class UserCredentials {

        private String password;
        private String username;

        public UserCredentials() {
        }

        public UserCredentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }

    public static String PATH_AUTHENTICATE = "/authenticate";
    public static String PATH_CONFIRM = "/confirm";
    public static String PATH_CURRENT = "/current";
    public static String PATH_EMAIL = "/email";
    public static String PATH_SOCIAL_LOGIN = "/social-login";

    @POST
    @Path(PATH_AUTHENTICATE)
    @Produces("application/json")
    @Consumes("application/json")
    Boolean authenticate(UserCredentials userCredentials);

    @PUT
    @Path("/password/change")
    @Secured({UserRole.USER})
    @Consumes("application/json")
    void changePassword(String password);

    @GET
    @Path(PATH_CONFIRM + "/{token}")
    @Produces("application/json")
    void confirm(@PathParam("token") String token);

    @POST
    @Consumes("application/json")
    void create(User user);

    @GET
    @Produces("application/json")
    List<User> getAll();

    @GET
    @Path(PATH_EMAIL + "/{email}")
    @Produces("application/json")
    User getByEmail(@PathParam("email") String email);

    @GET
    @Path("/{id}")
    @Produces("application/json")
    User getById(@PathParam("id") int id);

    @GET
    @Path(PATH_SOCIAL_LOGIN + "/{social-user-id}")
    @Produces("application/json")
    User getBySocialUserId(@PathParam("social-user-id") String socialUserId);

    @GET
    @Path(PATH_CURRENT)
    @Produces("application/json")
    @Secured({ UserRole.USER })
    User getCurrent();

    @GET
    @Consumes("application/json")
    @Path("/password/lost/{username}")
    void passwordLost(@PathParam("username") String username);

    @POST
    @Consumes("application/json")
    @Path("/register")
    void register(User user);

    @DELETE
    @Path("/{id}")
    void remove(@PathParam("id") int id);

    @DELETE
    @Path(PATH_CURRENT)
    @Secured({UserRole.USER})
    void removeCurrent();

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    void update(@PathParam("id") int id, User exercise);
}
