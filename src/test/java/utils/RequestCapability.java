package utils;

import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.restassured.RestAssured.given;

public interface RequestCapability {
    Header defaultHeader = new Header("Content-type", "application/json; charset=UTF-8");
    Header acceptJSONHeader = new Header("Accept", "application/json");
    String token = "ATATT3xFfGF0MLnszRfabmDrF-Bf2Xzczt3MbluTmLTlfjUTu7lxNd-8QorQu5tKFkEaXsryvBFnbNetPM9SDuk2fJ55t69z8kGcgZoXVsnHejA7rPvVqpWs1Dw8Uu_jbTQLPs-euOIQ6aeOrxPJoPkAoXhS1nYkI5Hs0NVybJLndqaNzbd8CXs=6E2114DC";
    String email = "pnghia211@gmail.com";
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

    Function<String, Map<String, String>> getIssueInfo = issueKey -> {
        if (issueKey == null) {
            throw new IllegalArgumentException("[ERR] encode String cannot be null");
        }
        String baseUri = "https://pnghia211.atlassian.net";
        String email = System.getenv("email");
        String apiToken = System.getenv("token");
        String credString = AuthTokenHandler.getCredString(email, apiToken);

        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(getAuth.apply(credString));

        String getIssuePath = "/rest/api/3/issue/" + issueKey;
        Response response_ = request.get(getIssuePath);

        Map<String, Object> fields = JsonPath.from(response_.asString()).get("fields");
        String actualSummary = fields.get("summary").toString();
        Map<String, Object> status = (Map<String, Object>) fields.get("status");
        Map<String, Object> statusCategory = (Map<String, Object>) status.get("statusCategory");
        String actualStatus = statusCategory.get("name").toString();

        Map<String, String> issueInfo = new HashMap<>();
        issueInfo.put("summary", actualSummary);
        issueInfo.put("status", actualStatus);
        return issueInfo;
    };
}
