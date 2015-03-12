package com.treelzebub.readerbility.auth;

import java.io.Serializable;

import retrofit.client.Response;

/**
 * Created by Tre Murillo on 3/2/15.
 */
public class AccessToken implements Serializable {
    protected Response response;
    protected String token;
    protected String tokenSecret;
    protected long userId;

    public AccessToken(Response response) {
        this.response = response;
    }

    public AccessToken(String token, String tokenSecret) {
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public AccessToken(String token, String tokenSecret, long userId) {
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    private String tokenToString() {
        if (this.token == null) return "null";
        return "ACCESS_TOKEN_REMOVED";
    }
}
