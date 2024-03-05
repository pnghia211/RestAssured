package utils;

import org.apache.commons.codec.binary.Base64;

public class AuthTokenHandler {
    public static String getCredString(String email, String apiToken) {
        String cred = email.concat(":").concat(apiToken);
        byte[] encodeCred = Base64.encodeBase64(cred.getBytes());
        return new String(encodeCred);
    }
}
