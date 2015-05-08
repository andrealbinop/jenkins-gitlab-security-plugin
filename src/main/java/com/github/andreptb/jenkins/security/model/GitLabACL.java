package com.github.andreptb.jenkins.security.model;

import hudson.model.Job;
import hudson.model.Project;
import hudson.security.ACL;
import hudson.security.Permission;
import jenkins.model.Jenkins;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

/**
 * Created by Andre on 03/05/2015.
 */
public class GitLabACL extends ACL {

    private Project<?, ?> project;

    public GitLabACL(Job<?, ?> job) {
        if(job instanceof Project) {
            this.project = (Project<?, ?>) job;
        }
    }

    @Override
    public boolean hasPermission(Authentication a, Permission permission) {
        GrantedAuthority[] authorities = a.getAuthorities();
        if(ArrayUtils.isEmpty(authorities)) {
            return false;
        }
        for(GrantedAuthority authority : authorities) {
            if(StringUtils.endsWith(authority.getAuthority(), GitLabGrantedAuthority.GITLAB_ADMIN_SUFFIX)) {
                return true;
            }
            if(authority instanceof GitLabGrantedAuthority) {
                if(hasPermissionForJob((GitLabGrantedAuthority) authority, permission)) {
                    return true;
                }
            }
        }
        return this.project == null && Jenkins.READ == permission;
    }

    private boolean hasPermissionForJob(GitLabGrantedAuthority authority, Permission permission) {
        if(this.project == null) {
            return false;
        }
        String project = StringUtils.removeEnd(StringUtils.substringAfterLast(this.project.getScm().getKey(), "/"), ".git");
        if(!StringUtils.equals(authority.getProject(), project)) {
            return false;
        }
        Permission specificPermission = permission;
        while(specificPermission != null) {
            if(authority.getPermissions().contains(specificPermission)) {
                return true;
            }
            specificPermission = specificPermission.impliedBy;
        }
        return false;
    }
}
