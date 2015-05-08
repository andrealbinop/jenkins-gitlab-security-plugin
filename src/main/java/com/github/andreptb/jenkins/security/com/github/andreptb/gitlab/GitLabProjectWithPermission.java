package com.github.andreptb.jenkins.security.com.github.andreptb.gitlab;

import org.gitlab.api.models.GitlabProject;

public class GitLabProjectWithPermission extends GitlabProject {

    private GitLabProjectPermissions permissions;

    public GitLabProjectPermissions getPermissions() {
        return permissions;
    }

    public void setPermissions(GitLabProjectPermissions permissions) {
        this.permissions = permissions;
    }
}
