package test.jira;

import builder.IssueContentBuilder;
import builder.JSONBuilder;
import data.Jira.IssueInfo;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import utils.ProjectInfo;
import utils.RequestCapability;
import utils.AuthTokenHandler;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CreateIssue implements RequestCapability {
    public static void main(String[] args) {

        // API info
        String baseUri = "https://pnghia211.atlassian.net";
        String projectKey = "RES";
        String path = "/rest/api/3/issue";
        String email = System.getenv("email");
        String apiToken = System.getenv("token");
        String credString = AuthTokenHandler.getCredString(email, apiToken);

        // Request Object
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.header(defaultHeader);
        request.header(acceptJSONHeader);
        request.header(getAuth.apply(credString));

        // Define body data
        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId("task");

        int length = 20;
        boolean hasText = true;
        boolean hasNum = true;
        String randomSummary = RandomStringUtils.random(length,hasText,hasNum);

        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
        String projectInfoStr = issueContentBuilder.build(randomSummary, projectKey, taskTypeId);

        // Send request
        Response response = request.body(projectInfoStr).post(path);
        response.prettyPrint();

        // Get request
        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        String getIssuePath = "/rest/api/3/issue/" + responseBody.get("key");

        response = request.get(getIssuePath);

        IssueInfo issueInfo = issueContentBuilder.getIssueInfo();
        String expectedSummary = issueInfo.getFields().getSummary();
        String expectedStatus = "To Do";


        JsonPath jsonPath = new JsonPath(response.asString());
        String actualSummary = jsonPath.getString("fields.summary");
        String actualStatus = jsonPath.getString("fields.status.statusCategory.name");

        System.out.println(expectedSummary);
        System.out.println(actualSummary);

        System.out.println(expectedStatus);
        System.out.println(actualStatus);
    }
}
