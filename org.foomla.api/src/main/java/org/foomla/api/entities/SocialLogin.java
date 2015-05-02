package org.foomla.api.entities;

public interface SocialLogin {

    String getSocialUserId();

    SocialLoginType getType();

    void setSocialUserId(String socialUserId);

    void setType(SocialLoginType socialLoginType);

}
