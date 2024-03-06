package test.jira;

import builder.IssueContentBuilder;
import builder.JSONBuilder;
import data.Jira.IssueInfo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ProjectInfo;
import utils.RequestCapability;
import utils.AuthTokenHandler;

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
        String summary = "This is my summary from API";
        String projectInfoStr = IssueContentBuilder.build(summary, projectKey, taskTypeId);

        // Send request
        Response response = request.body(projectInfoStr).post(path);
        response.prettyPrint();
    }
}
