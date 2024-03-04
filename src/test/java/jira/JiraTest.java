package jira;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.codec.binary.Base64;
import utils.RequestCapability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraTest implements RequestCapability {
    public static void main(String[] args) {
        String baseUri = "https://pnghia211.atlassian.net";
        String path = "/rest/api/3/project/RES";

        String email = System.getenv("email");
        String apiToken = System.getenv("token");
        String cred = email.concat(":").concat(apiToken);
        byte[] encodeCred = Base64.encodeBase64(cred.getBytes());
        String credString = new String(encodeCred);

        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(getAuth.apply(credString));

        Response response = request.get(path);
        response.prettyPrint();

        Map<String, List<Map<String, String>>> projectInfo = JsonPath.from(response.asString()).get();
        List<Map<String, String>> issueTypes = projectInfo.get("issueTypes");
        for (Map<String, String> issueType : issueTypes) {
            System.out.println(issueType.get("id"));
            System.out.println(issueType.get("name"));
            System.out.println("-----------");
        }
    }
}
