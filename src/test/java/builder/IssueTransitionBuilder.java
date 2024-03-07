package builder;

import data.Jira.IssueTransitionInfo;

public class IssueTransitionBuilder {

    public String build(String transitionId) {
        IssueTransitionInfo.Transition transition = new IssueTransitionInfo.Transition(transitionId);
        IssueTransitionInfo issueTransitionInfo = new IssueTransitionInfo(transition);
        return JSONBuilder.getJSON(issueTransitionInfo);
    }
}
