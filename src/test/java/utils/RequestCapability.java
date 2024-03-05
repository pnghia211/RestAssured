package utils;

import io.restassured.http.Header;

import java.util.function.Function;

public interface RequestCapability {
    Header defaultHeader = new Header("Content-type", "application/json; charset=UTF-8");
    Header acceptJSONHeader = new Header("Accept", "application/json");

    static Header getAuth(String encodeString) {
        if (encodeString == null) {
            throw new IllegalArgumentException("[ERR] encode String cannot be null");
        }
        return new Header("Authorization", "Basic " + encodeString);
    }

    Function<String, Header> getAuth = encodeString -> {
        if (encodeString == null) {
            throw new IllegalArgumentException("[ERR] encode String cannot be null");
        }
        return new Header("Authorization", "Basic " + encodeString);
    };
}
