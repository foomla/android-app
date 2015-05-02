package org.foomla.api.client.entities;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.foomla.api.entities.SocialLogin;
import org.foomla.api.entities.SocialLoginType;


@JsonDeserialize(as = SocialLoginImpl.class)
public class SocialLoginImpl implements SocialLogin {

    private String socialUserId;

    private SocialLoginType type;

    @Override
    public String getSocialUserId() {
        return socialUserId;
    }

    @Override
    public SocialLoginType getType() {
        return type;
    }

    @Override
    public void setSocialUserId(String socialUserId) {
        this.socialUserId = socialUserId;
    }

    @Override
    public void setType(SocialLoginType type) {
        this.type = type;
    }

}
