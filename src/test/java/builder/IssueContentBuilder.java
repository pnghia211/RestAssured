package builder;

import com.google.gson.Gson;
import data.Jira.IssueInfo;

public class IssueContentBuilder {
    public static String build (String summary, String projectKey,String issueTypeId) {
        IssueInfo.IssueType issueType = new IssueInfo.IssueType(issueTypeId);
        IssueInfo.Project project = new IssueInfo.Project(projectKey);
        IssueInfo.Fields fields = new IssueInfo.Fields(summary, project, issueType);
        IssueInfo issueInfo = new IssueInfo(fields);
        return JSONBuilder.getJSON(issueInfo);
    }
}
