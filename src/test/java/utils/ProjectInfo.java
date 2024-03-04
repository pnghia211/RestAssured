package utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.codec.binary.Base64;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProjectInfo implements RequestCapability {
    private String baseUri;
    private String projectKey;
    private List<Map<String, String>> issueTypes;
    private Map<String, List<Map<String, String>>> projectInfo;

    public ProjectInfo(String baseUri, String projectKey) {
        this.baseUri = baseUri;
        this.projectKey = projectKey;
        getProjectInfo();
    }

    public String getIssueTypeId(String issueTypeName) {
        getIssueTypes();
        String issueTypeId = null;

        for (Map<String, String> issueType : issueTypes) {
            if (issueType.get("name").equalsIgnoreCase(issueTypeName)) {
                System.out.println(issueType.get("id"));
                break;
            }
        }

        if (issueTypeId == null) {
            throw new RuntimeException("[ERR] issue type id cannot be null");
        }
        return issueTypeId;
    }

    private void getProjectInfo() {
        String path = "/rest/api/3/project" + projectKey;

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
        projectInfo = JsonPath.from(response.asString()).get();
    }

    private void getIssueTypes() {
        issueTypes = projectInfo.get("issueTypes");
    }
}
