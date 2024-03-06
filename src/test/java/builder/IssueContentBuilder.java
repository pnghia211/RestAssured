package builder;

import com.google.gson.Gson;
import data.Jira.IssueInfo;

public class IssueContentBuilder {

    private IssueInfo issueInfo;

    public String build(String summary, String projectKey, String issueTypeId) {
        IssueInfo.IssueType issueType = new IssueInfo.IssueType(issueTypeId);
        IssueInfo.Project project = new IssueInfo.Project(projectKey);
        IssueInfo.Fields fields = new IssueInfo.Fields(summary, project, issueType);
        issueInfo = new IssueInfo(fields);
        return JSONBuilder.getJSON(issueInfo);
    }

    public IssueInfo getIssueInfo() {
        return issueInfo;
    }
}
