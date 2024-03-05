package test.jira;

import com.google.gson.Gson;
import data.Jira.ProjectInfo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
        String summary = "This is my summary from API";
        ProjectInfo.IssueType issueType = new ProjectInfo.IssueType("10000");
        ProjectInfo.Project project = new ProjectInfo.Project(projectKey);
        ProjectInfo.Fields fields = new ProjectInfo.Fields(summary, project, issueType);
        ProjectInfo projectInfo = new ProjectInfo(fields);

        // Send request
        Response response = request.body(new Gson().toJson(projectInfo)).post(path);
        response.prettyPrint();
    }
}
