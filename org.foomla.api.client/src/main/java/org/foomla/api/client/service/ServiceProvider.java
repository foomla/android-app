package org.foomla.api.client.service;

import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.foomla.api.client.AuthorizationException;
import org.foomla.api.client.FoomlaClient.ClientType;
import org.foomla.api.client.FoomlaClient.RestConnectionSettings;
import org.foomla.api.client.RemoteInvocationException;
import org.foomla.api.client.ServiceErrorException;
import org.foomla.api.client.oauth.OAuthToken;
import org.foomla.api.client.oauth.OAuthTokenAccess;
import org.foomla.api.services.ServiceError;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

public class ServiceProvider {

    class ServiceCall {

        private final ClientResource clientResource;

        public ServiceCall(ClientResource clientResource) {
            this.clientResource = clientResource;
        }

        public ServiceResponse delete() throws RemoteInvocationException, AuthorizationException {
            return new ServiceResponse(clientResource, callResource(clientResource, Method.DELETE, null));
        }

        public ServiceResponse get() throws RemoteInvocationException, AuthorizationException {
            return new ServiceResponse(clientResource, callResource(clientResource, Method.GET, null));
        }

        public ServiceResponse post(Object entity) throws RemoteInvocationException, AuthorizationException {
            return new ServiceResponse(clientResource, callResource(clientResource, Method.POST, entity));
        }

        public ServiceResponse put(Object entity) throws RemoteInvocationException, AuthorizationException {
            return new ServiceResponse(clientResource, callResource(clientResource, Method.PUT, entity));
        }

        public ServiceCall queryParameter(String name, String value) {
            this.clientResource.addQueryParameter(name, value);
            return this;
        }
    }

    class ServiceResponse {

        private final ClientResource clientResource;
        private final Representation representation;

        public ServiceResponse(ClientResource clientResource, Representation representation) {
            this.clientResource = clientResource;
            this.representation = representation;
        }

        public <T> T as(Class<T> type) {
            return clientResource.toObject(representation, type);
        }
    }

    private final OAuthTokenAccess oAuthTokenAccess;
    private final RestConnectionSettings settings;

    public ServiceProvider(final RestConnectionSettings settings) {
        this.settings = settings;
        this.oAuthTokenAccess = new OAuthTokenAccess(settings.oAuthClientCredentials, settings.oAuthTokenEndpoint);
    }

    public RestConnectionSettings getSettings() {
        return settings;
    }

    protected Representation callResource(ClientResource clientResource, Method method)
            throws RemoteInvocationException, AuthorizationException {
        return callResource(clientResource, method, null);
    }

    protected Representation callResource(ClientResource clientResource, Method method, Object entity)
            throws RemoteInvocationException, AuthorizationException {
        return callResource(clientResource, method, entity, true);
    }

    protected Representation callResource(ClientResource clientResource, Method method, Object entity,
            boolean refreshAccessToken) throws RemoteInvocationException, AuthorizationException {
        try {
            if (method.equals(Method.GET)) {
                return clientResource.get();
            }
            else if (method.equals(Method.DELETE)) {
                return clientResource.delete();
            }
            else if (method.equals(Method.PUT)) {
                return clientResource.put(entity);
            }
            else if (method.equals(Method.POST)) {
                return clientResource.post(entity);
            }
            else {
                throw new IllegalArgumentException("Unsupported method type: " + method);
            }
        }
        catch (ResourceException ex) {
            if (ex.getStatus().equals(Status.CLIENT_ERROR_UNAUTHORIZED)) {
                if (refreshAccessToken) {
                    refreshAccessToken(clientResource);
                    return callResource(clientResource, method, entity, false);
                }
                else {
                    throw new AuthorizationException(ex);
                }
            }
            else if (ex.getStatus().getCode() == ServiceError.FOOMLA_SERVICE_ERROR_CODE) {
                throw new ServiceErrorException(ServiceError.fromJson(ex.getMessage()), ex);
            }
            else {
                throw new RemoteInvocationException(ex);
            }
        }
    }

    protected ClientResource createClientResource(String path) {

        ClientResource clientResource = new ClientResource(settings.restUrl + path);
        setCustomHeaders(clientResource);
        setAccessTokenHeader(clientResource);

        return clientResource;
    }

    protected void setAccessTokenHeader(ClientResource clientResource) {

        OAuthToken oAuthToken = settings.oAuthToken;

        if (oAuthToken == null) {
            return;
        }

        String accessToken = oAuthToken.getAccessToken();

        if (accessToken == null) {
            return;
        }

        ChallengeScheme bearerChallengeScheme = new ChallengeScheme("Bearer", "Bearer");
        ChallengeResponse challengeResponse = new ChallengeResponse(bearerChallengeScheme);
        challengeResponse.setRawValue(accessToken);
        clientResource.setChallengeResponse(challengeResponse);
    }

    protected void setCustomHeaders(ClientResource clientResource) {
        setHeader(clientResource, "X-Client-Identifier", settings.uniqueClientIdentifier);
        ClientType clientType = settings.clientType;
        if (clientType != null) {
            setHeader(clientResource, "X-Client-Type", clientType.name());
        }
    }

    @SuppressWarnings("unchecked")
    protected void setHeader(ClientResource clientResource, String headerName, String headerValue) {

        if (headerValue == null) {
            return;
        }

        ConcurrentMap<String, Object> attrs = clientResource.getRequest().getAttributes();
        Series<Header> headers = (Series<Header>) attrs.get(HeaderConstants.ATTRIBUTE_HEADERS);
        if (headers == null) {
            headers = new Series<Header>(Header.class);
            Series<Header> prev = (Series<Header>) attrs.putIfAbsent(HeaderConstants.ATTRIBUTE_HEADERS, headers);
            if (prev != null) {
                headers = prev;
            }
        }

        headers.add(headerName, headerValue);
    }

    protected ServiceCall url(String url, Object... params) {
        url = formatUrl(url, params);
        return new ServiceCall(createClientResource(url));
    }

    private String formatUrl(String url, Object[] params) {
        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(url);

        StringBuilder builder = new StringBuilder();
        int i = 0;
        int index = 0;
        while (matcher.find()) {
            String replacement = String.valueOf(params[index]);
            builder.append(url.substring(i, matcher.start()));
            builder.append(replacement);
            i = matcher.end();
            index++;
        }

        builder.append(url.substring(i, url.length()));

        return builder.toString();
    }

    private void refreshAccessToken(ClientResource clientResource) throws AuthorizationException {

        OAuthToken oldOAuthToken = settings.oAuthToken;
        if (oldOAuthToken == null) {
            return;
        }

        String refreshToken = oldOAuthToken.getRefreshToken();
        if (refreshToken == null) {
            return;
        }

        OAuthToken oAuthToken = oAuthTokenAccess.exchangeRefreshToken(refreshToken);
        if (oAuthToken.getRefreshToken() == null) {
            oAuthToken.setRefreshToken(refreshToken);
        }

        settings.oAuthToken = oAuthToken;
        setAccessTokenHeader(clientResource);
    }

}
