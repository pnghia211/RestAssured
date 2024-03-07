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

    private void getProjectInfo() {
        String path = "/rest/api/3/project/" + projectKey;

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

    public String getIssueTypeId(String issueTypeName) {
        getIssueTypes();
        String issueTypeId = null;
        for (Map<String, String> issueType : issueTypes) {
            if (issueType.get("name").equalsIgnoreCase(issueTypeName)) {
                issueTypeId = issueType.get("id");
                break;
            }
        }

        if (issueTypeId == null) {
            throw new RuntimeException("[ERR] cannot find the issue id for " + "\"" + issueTypeName + "\"");
        }
        return issueTypeId;
    }

    private void getIssueTypes() {
        issueTypes = projectInfo.get("issueTypes");
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
                "baseUri='" + baseUri + '\'' +
                ", projectKey='" + projectKey + '\'' +
                ", issueTypes=" + issueTypes +
                '}';
    }
}
