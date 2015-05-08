package com.github.andreptb.jenkins.security.model;

import hudson.security.Permission;
import org.acegisecurity.GrantedAuthority;

import java.util.Collection;

/**
 * Created by andre on 06/05/15.
 */
public class GitLabGrantedAuthority implements GrantedAuthority {

    public static final String GITLAB_ADMIN_SUFFIX = "- GitLab Admin";

    private String namespace;
    private String project;
    private Collection<Permission> permissions;

    public GitLabGrantedAuthority(String namespace, String project, Collection<Permission> permissions) {
        this.namespace = namespace;
        this.project = project;
        this.permissions = permissions;
    }

    @Override
    public String getAuthority() {
        return String.format("%s_%s", this.namespace, this.project);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getProject() {
        return project;
    }

    public Collection<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return getAuthority();
    }
}
