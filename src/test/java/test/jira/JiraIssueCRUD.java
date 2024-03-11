package test.jira;

import org.testng.annotations.Test;
import test.BaseTest;
import test_flow.IssueFlow;

public class JiraIssueCRUD extends BaseTest {

    @Test
    public void testE2EFlow() {
        IssueFlow issueFlow = new IssueFlow(request, baseUri, projectKey, "task");
        issueFlow.createIssue();
        issueFlow.verifyIssue();
        issueFlow.updateTransitionIssue("Done");
        issueFlow.verifyIssue();
        issueFlow.deleteIssue();
    }
}
