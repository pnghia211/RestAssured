package test.jira;

import utils.ProjectInfo;
import utils.RequestCapability;

public class JiraTest implements RequestCapability {
    public static void main(String[] args) {
        String baseUri = "https://pnghia211.atlassian.net";
        String projectKey = "RES";

        ProjectInfo projectInfo = new ProjectInfo(baseUri, projectKey);
        System.out.println(projectInfo.getIssueTypeId("task"));
    }
}
