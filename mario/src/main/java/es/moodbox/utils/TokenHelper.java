package es.moodbox.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by victoriza on 28/08/15.
 */
public class TokenHelper {

    public static String generateToken(){
        return new BigInteger(130, new SecureRandom()).toString(64);
    }
}
