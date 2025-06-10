package test.jira;

import builder.IssueContentBuilder;
import builder.IssueTransitionBuilder;
import data.Jira.IssueFields;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import utils.ProjectInfo;
import utils.RequestCapability;
import utils.AuthTokenHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
        request.baseUri(baseUri)
                .header(defaultHeader)
                .header(acceptJSONHeader)
                .header(getAuth.apply(credString));

        // Define body data
        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId("task");

        int length = 20;
        boolean hasText = true;
        boolean hasNum = true;
        String randomSummary = RandomStringUtils.random(length, hasText, hasNum);

        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
        String projectInfoStr = issueContentBuilder.build(randomSummary, projectKey, taskTypeId);

        // Send post request
        Response response = request.body(projectInfoStr).post(path);
        response.prettyPrint();

        // Get request
        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        final String CREATED_ISSUE_KEY = responseBody.get("key");

        IssueFields issueFields = issueContentBuilder.getIssueFields();
        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = "To Do";

        //Read created Jira task
        Function<String, Map<String, String>> getIssueInfo = issueKey -> {
            String getIssuePath = "/rest/api/3/issue/" + issueKey;
            Response response_ = request.get(getIssuePath);

            JsonPath jsonPath = JsonPath.from(response_.asString());
            String actualSummary = jsonPath.getString("fields.summary");
            String actualStatus = jsonPath.getString("fields.status.statusCategory.name");

            Map<String, String> issueInfo = new HashMap<>();
            issueInfo.put("summary", actualSummary);
            issueInfo.put("status", actualStatus);
            return issueInfo;
        };

        Map<String, String> issueInfo = getIssueInfo.apply(CREATED_ISSUE_KEY);
        System.out.println(expectedSummary);
        System.out.println(issueInfo.get("summary"));

        System.out.println(expectedStatus);
        System.out.println(issueInfo.get("status"));

        // Update (transition) created jira task
        String issueTransitionPath = "/rest/api/3/issue/" + CREATED_ISSUE_KEY + "/transitions";
        String DONE_STATUS_ID = "41";
        IssueTransitionBuilder issueTransitionBuilder = new IssueTransitionBuilder();
        String transitionBody = issueTransitionBuilder.build(DONE_STATUS_ID);

        // Send update transition request
        request.body(transitionBody).post(issueTransitionPath).then().assertThat().statusCode(204);
        issueInfo = getIssueInfo.apply(CREATED_ISSUE_KEY);
        System.out.println("Update status :" + issueInfo.get("status"));

        // Delete created jira task
        String issueDeletePath = "/rest/api/3/issue/" + CREATED_ISSUE_KEY;
        request.delete(issueDeletePath).then().assertThat().statusCode(204);

        // Verify not existing issue
        String issueGetPath = "/rest/api/3/issue/" + CREATED_ISSUE_KEY;
        response = request.get(issueGetPath);
        JsonPath jsonPath = JsonPath.from(response.asString());
        String errorMessages = jsonPath.get("errorMessages[0]");
        System.out.println(errorMessages);
    }
}
