package com.github.andreptb.jenkins.security.model;

import hudson.model.Job;
import hudson.security.ACL;
import hudson.security.Permission;
import org.acegisecurity.Authentication;

/**
 * Created by Andre on 03/05/2015.
 */
public class GitLabACL extends ACL {

    private Job<?, ?> job;

    public GitLabACL(Job<?, ?> job) {
        this.job = job;
    }

    @Override
    public boolean hasPermission(Authentication a, Permission permission) {
        Object details = a.getDetails();
        if(details instanceof GitLabUserDetails) {
            return hasPermission((GitLabUserDetails) details, permission);
        }
        return false;
    }

    private boolean hasPermission(GitLabUserDetails a, Permission permission) {
        if(a.getGitLabUser().isAdmin()) {
            return true;
        }
        return this.job != null && hasPermissionForJob(a, permission);
    }

    private boolean hasPermissionForJob(GitLabUserDetails a, Permission permission) {
        return false;
    }
}
