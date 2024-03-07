package builder;

import data.Jira.IssueFields;

public class IssueContentBuilder {

    private IssueFields issueFields;

    public String build(String summary, String projectKey, String issueTypeId) {
        IssueFields.IssueType issueType = new IssueFields.IssueType(issueTypeId);
        IssueFields.Project project = new IssueFields.Project(projectKey);
        IssueFields.Fields fields = new IssueFields.Fields(summary, project, issueType);
        issueFields = new IssueFields(fields);
        return JSONBuilder.getJSON(issueFields);
    }

    public IssueFields getIssueInfo() {
        return issueFields;
    }
}
