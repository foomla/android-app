package org.foomla.api.client.oauth;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.foomla.api.client.AuthorizationException;

import com.google.gson.Gson;

public class OAuthTokenAccess {

    private final OAuthClientCredentials clientCredentials;
    private final String tokenEndpoint;

    public OAuthTokenAccess(OAuthClientCredentials clientCredentials, String tokenEndpoint) {
        this.clientCredentials = clientCredentials;
        this.tokenEndpoint = tokenEndpoint;
    }

    public OAuthToken exchangeAuthCode(String authCode) throws AuthorizationException {
        String tokenUrlForAuthCodeExchange = getTokenUrlForAuthCodeExchange(authCode,
                this.clientCredentials.getClientId(), this.clientCredentials.getClientSecret());
        return getOAuthToken(tokenUrlForAuthCodeExchange);
    }

    public OAuthToken exchangeRefreshToken(String refreshToken) throws AuthorizationException {
        String tokenUrlForAuthCodeExchange = getTokenUrlForRefreshTokenExchange(refreshToken,
                this.clientCredentials.getClientId(), this.clientCredentials.getClientSecret());
        return getOAuthToken(tokenUrlForAuthCodeExchange);
    }

    private OAuthToken getOAuthToken(String tokenUrl) throws AuthorizationException {
        try {
            URL url = new URL(tokenUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpURLConnectionUtil.REQUEST_METHOD_POST);

            int responseCode = connection.getResponseCode();

            switch (responseCode) {
                case 200:
                case 201:

                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    OAuthToken oAuthToken = new Gson().fromJson(inputStreamReader, OAuthToken.class);
                    return oAuthToken;

                default:
                    throw new AuthorizationException("Could not retrieve OAuth token: " + responseCode + " - "
                            + HttpURLConnectionUtil.responseBodyToString(connection));
            }
        }
        catch (AuthorizationException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new AuthorizationException("Could not retrieve OAuth token: " + ex.getMessage(), ex);
        }
    }

    private String getTokenUrlForAuthCodeExchange(String code, String clientId, String clientSecret) {
        return String.format(tokenEndpoint + "?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s",
                clientId, clientSecret, code);
    }

    private String getTokenUrlForRefreshTokenExchange(String refreshToken, String clientId, String clientSecret) {
        return String.format(
                tokenEndpoint + "?grant_type=refresh_token&client_id=%s&client_secret=%s&refresh_token=%s", clientId,
                clientSecret, refreshToken);
    }
}
