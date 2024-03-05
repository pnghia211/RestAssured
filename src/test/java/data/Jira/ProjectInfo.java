package data.Jira;

public class ProjectInfo {
    private Fields fields;

    public ProjectInfo(Fields fields) {
        this.fields = fields;
    }

    public Fields getFields() {
        return fields;
    }

    public static class Fields {
        String summary;
        Project project;
        IssueType issuetype;

        public Fields(String summary, Project project, IssueType issuetype) {
            this.summary = summary;
            this.project = project;
            this.issuetype = issuetype;
        }

        public String getSummary() {
            return summary;
        }

        public Project getProject() {
            return project;
        }

        public IssueType getIssueTypes() {
            return issuetype;
        }
    }

    public static class Project {
        String key;

        public Project(String id) {
            this.key = id;
        }

        public String getKey() {
            return key;
        }
    }

    public static class IssueType {
        String id;

        public IssueType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
