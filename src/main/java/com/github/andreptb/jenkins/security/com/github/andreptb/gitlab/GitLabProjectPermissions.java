package com.github.andreptb.jenkins.security.com.github.andreptb.gitlab;

import org.codehaus.jackson.annotate.JsonProperty;

public class GitLabProjectPermissions {

    @JsonProperty("project_access")
    private GitLabProjectPermission projectAccess;
    @JsonProperty("group_access")
    private GitLabProjectPermission groupAccess;

    public GitLabProjectPermission getProjectAccess() {
        return projectAccess;
    }

    public void setProjectAccess(GitLabProjectPermission projectAccess) {
        this.projectAccess = projectAccess;
    }

    public GitLabProjectPermission getGroupAccess() {
        return groupAccess;
    }

    public void setGroupAccess(GitLabProjectPermission groupAccess) {
        this.groupAccess = groupAccess;
    }
}
