package co.com.pragma.utils;

import org.jboss.aerogear.security.otp.Totp;

public class TowFactorAuthentication {


    public static String getTwoFactorCode(String secret) {

        String twoFactorCode;

        Totp totp = new Totp(secret);

        twoFactorCode = totp.now();

        while (!totp.verify(twoFactorCode)){
            twoFactorCode = totp.now();
        }

        return twoFactorCode;
    }

}
