package test_flow;

import builder.IssueContentBuilder;
import builder.IssueTransitionBuilder;
import data.Jira.IssueFields;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import utils.ProjectInfo;

import java.util.HashMap;
import java.util.Map;

public class IssueFlow {
    private RequestSpecification request;
    private Response response;
    private String baseUri;
    private String projectKey;
    private String issueTypeStr;
    private String issuePathPrefix = "/rest/api/3/issue";
    private String status = "To do";
    IssueFields issueFields;
    String issueKey;
    static Map<String, String> transitionTypeMap = new HashMap<>();

    static {
        transitionTypeMap.put("11", "To do");
        transitionTypeMap.put("21", "For dev");
        transitionTypeMap.put("31", "In progress");
        transitionTypeMap.put("41", "Done");
    }

    public IssueFlow(RequestSpecification request, String baseUri, String projectKey, String issueTypeStr) {
        this.request = request;
        this.baseUri = baseUri;
        this.projectKey = projectKey;
        this.issueTypeStr = issueTypeStr;
    }

    public void createIssue() {
        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        String taskTypeId = projectInfo.getIssueTypeId(issueTypeStr);

        int length = 20;
        boolean hasText = true;
        boolean hasNum = true;
        String randomSummary = RandomStringUtils.random(length, hasText, hasNum);

        IssueContentBuilder issueContentBuilder = new IssueContentBuilder();
        String projectInfoStr = issueContentBuilder.build(randomSummary, projectKey, taskTypeId);
        issueFields = issueContentBuilder.getIssueFields();

        this.response = request.body(projectInfoStr).post(issuePathPrefix);
        Map<String, String> responseBody = JsonPath.from(response.asString()).get();
        issueKey = responseBody.get("key");
    }

    public void verifyIssue() {
        Map<String, String> issueInfo = getIssueInfo();
        String expectedSummary = issueFields.getFields().getSummary();
        String expectedStatus = status;

        System.out.println(issueInfo.get("summary"));
        System.out.println(expectedSummary);
        System.out.println(issueInfo.get("status"));
        System.out.println(expectedStatus);
    }

    private Map<String, String> getIssueInfo() {
        String getIssuePath = "/rest/api/3/issue/" + issueKey;
        Response response_ = request.get(getIssuePath);

        JsonPath jsonPath = JsonPath.from(response_.asString());
        String actualSummary = jsonPath.getString("fields.summary");
        String actualStatus = jsonPath.getString("fields.status.statusCategory.name");

        Map<String, String> issueInfo = new HashMap<>();
        issueInfo.put("summary", actualSummary);
        issueInfo.put("status", actualStatus);
        return issueInfo;
    }

    public void updateTransitionIssue(String updateStatusStr) {
        String targetTransitionId = null;
        for (String transitionId : transitionTypeMap.keySet()) {
            if (transitionTypeMap.get(transitionId).equalsIgnoreCase(updateStatusStr)) {
                targetTransitionId = transitionId;
            }
        }

        if (targetTransitionId == null) {
            throw new RuntimeException("[ERR] issue status update not supported!!!");
        }

        String issueTransitionPath = issuePathPrefix + "/" + issueKey + "/transitions";
        IssueTransitionBuilder issueTransitionBuilder = new IssueTransitionBuilder();
        String transitionBody = issueTransitionBuilder.build(targetTransitionId);

        // Send update transition request
        request.body(transitionBody).post(issueTransitionPath).then().assertThat().statusCode(204);
        Map<String, String> issueInfo = getIssueInfo();
        String actualStatus = issueInfo.get("status");
        String expectedStatus = transitionTypeMap.get(targetTransitionId);
        System.out.println("Lasted status : " + actualStatus);
        System.out.println("Expected status : " + expectedStatus);
    }

    public void deleteIssue() {
        String issueDeletePath = issuePathPrefix + "/" + issueKey;
        request.delete(issueDeletePath).then().assertThat().statusCode(204);

        String issueGetPath = issuePathPrefix + "/" + issueKey;
        response = request.get(issueGetPath);
        JsonPath jsonPath = JsonPath.from(response.asString());
        String errorMessages = jsonPath.get("errorMessages[0]");
        System.out.println(errorMessages);
    }
}
