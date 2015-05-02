package org.foomla.api.client;

import java.lang.reflect.Constructor;

import org.foomla.api.annotations.AnnotationsUtil;
import org.foomla.api.client.oauth.OAuthClientCredentials;
import org.foomla.api.client.oauth.OAuthToken;
import org.foomla.api.client.oauth.OAuthTokenAccess;
import org.foomla.api.client.service.ServiceProvider;
import org.foomla.api.converters.CustomJacksonConverter;
import org.restlet.engine.Engine;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoomlaClient {

    public enum ClientType {
        ANDROID, WEBADMIN, WEBAPP
    }

    public class RestConnectionSettings {

        public ClientType clientType;
        public final OAuthClientCredentials oAuthClientCredentials;
        public OAuthToken oAuthToken;
        public final String oAuthTokenEndpoint;
        public final String restUrl;
        public String uniqueClientIdentifier;

        public RestConnectionSettings(final String restUrl, final String oAuthTokenEndpoint,
                final OAuthClientCredentials oAuthClientCredentials) {
            this(restUrl, oAuthTokenEndpoint, oAuthClientCredentials, null);
        }

        public RestConnectionSettings(final String restUrl, final String oAuthTokenEndpoint,
                final OAuthClientCredentials oAuthClientCredentials, final OAuthToken oAuthToken) {
            this.restUrl = restUrl;
            this.oAuthTokenEndpoint = oAuthTokenEndpoint;
            this.oAuthClientCredentials = oAuthClientCredentials;
            this.oAuthToken = oAuthToken;
        }
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(FoomlaClient.class);

    protected static void init() {
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().getRegisteredConverters().add(new CustomJacksonConverter(new ClientEntityRestMapping()));
    }

    private final RestConnectionSettings restConnectionSettings;

    public FoomlaClient(final String restUrl, final String oAuthTokenEndpoint,
            final OAuthClientCredentials oAuthClientCredentials) {
        this.restConnectionSettings = new RestConnectionSettings(restUrl, oAuthTokenEndpoint, oAuthClientCredentials);
        init();
    }

    public FoomlaClient(final String restUrl, final String oAuthTokenEndpoint,
            final OAuthClientCredentials oAuthClientCredentials, final OAuthToken oAuthToken) {
        this.restConnectionSettings = new RestConnectionSettings(restUrl, oAuthTokenEndpoint, oAuthClientCredentials,
                oAuthToken);
        init();
    }

    public FoomlaClient(final String restUrl, final String oAuthTokenEndpoint,
            final OAuthClientCredentials oAuthClientCredentials, final String authCode) throws AuthorizationException {
        OAuthToken oAuthToken = getOAuthToken(oAuthTokenEndpoint, oAuthClientCredentials, authCode);
        this.restConnectionSettings = new RestConnectionSettings(restUrl, oAuthTokenEndpoint, oAuthClientCredentials,
                oAuthToken);
        init();
    }

    public void authorize(String authCode) {
        OAuthToken oAuthToken = getOAuthToken(restConnectionSettings.oAuthTokenEndpoint,
                restConnectionSettings.oAuthClientCredentials, authCode);
        this.restConnectionSettings.oAuthToken = oAuthToken;
    }

    public void exchangeRefreshToken() {
        OAuthToken oAuthToken = this.restConnectionSettings.oAuthToken;
        if (oAuthToken != null) {
            String refreshToken = oAuthToken.getRefreshToken();
            if (refreshToken != null) {
                OAuthToken refreshedOAuthToken = createOAuthTokenAccess().exchangeRefreshToken(refreshToken);
                this.restConnectionSettings.oAuthToken = refreshedOAuthToken;
            }
        }
    }

    public <T extends ServiceProvider> T getProvider(final Class<T> providerClass) {
        LOGGER.info("Create Provider {} for {}", providerClass, restConnectionSettings.restUrl);

        try {
            Constructor<T> constructor = providerClass.getConstructor(RestConnectionSettings.class);
            T provider = constructor.newInstance(restConnectionSettings);
            return provider;
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

    }

    public RestConnectionSettings getRestConnectionSettings() {
        return restConnectionSettings;
    }

    public boolean isAuthorized() {
        return this.restConnectionSettings.oAuthToken != null;
    }

    public void setClientType(final ClientType clientType) {
        restConnectionSettings.clientType = clientType;
    }

    public void setUniqueClientIdentifier(final String clientIdentifier) {
        restConnectionSettings.uniqueClientIdentifier = clientIdentifier;
    }

    protected ClientResource createClientResource(final Class<?> resourceInterface) {
        return new ClientResource(restConnectionSettings.restUrl + AnnotationsUtil.getRestPath(resourceInterface));
    }

    private OAuthTokenAccess createOAuthTokenAccess() {
        return new OAuthTokenAccess(this.restConnectionSettings.oAuthClientCredentials,
                this.restConnectionSettings.oAuthTokenEndpoint);
    }

    private OAuthToken getOAuthToken(final String oAuthTokenEndpoint,
            final OAuthClientCredentials oAuthClientCredentials, final String authCode) throws AuthorizationException {
        return new OAuthTokenAccess(oAuthClientCredentials, oAuthTokenEndpoint).exchangeAuthCode(authCode);
    }
}
