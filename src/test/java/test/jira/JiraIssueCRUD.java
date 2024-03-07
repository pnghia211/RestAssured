package test.jira;

import builder.IssueContentBuilder;
import builder.IssueTransitionBuilder;
import data.Jira.IssueFields;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import test_flow.IssueFlow;
import utils.AuthTokenHandler;
import utils.ProjectInfo;
import utils.RequestCapability;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.restassured.RestAssured.given;

public class JiraIssueCRUD implements RequestCapability {
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

        IssueFlow issueFlow = new IssueFlow(request,baseUri,projectKey,"task");
        System.out.println("-----> CREATE");
        issueFlow.createIssue();
        System.out.println("-----> READ");
        issueFlow.verifyIssue();
        System.out.println("-----> UPDATE");
        issueFlow.updateTransitionIssue("Done");
        issueFlow.verifyIssue();
        System.out.println("-----> DELETE");
        issueFlow.deleteIssue();
    }
}
