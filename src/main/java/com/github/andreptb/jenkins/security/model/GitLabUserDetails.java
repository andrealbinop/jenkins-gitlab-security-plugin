package com.github.andreptb.jenkins.security.model;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.ObjectUtils;
import org.gitlab.api.models.GitlabUser;

public class GitLabUserDetails implements UserDetails {

    private static final String USER_STATE_ACTIVE = "active";
    public static final String GITLAB_USER = "gitlab_user";

    private GitlabUser user;
    private String gitLabSourceUrl;

    public GitLabUserDetails(GitlabUser user, String gitLabSourceUrl) {
        this.user = user;
        this.gitLabSourceUrl = gitLabSourceUrl;
    }

    public GitlabUser getGitLabUser() {
        return user;
    }

    public String getGitLabSourceUrl() {
        return gitLabSourceUrl;
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        return new GrantedAuthority[]{ new GrantedAuthorityImpl(GitLabUserDetails.GITLAB_USER) };
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ObjectUtils.equals(this.user.getState(), GitLabUserDetails.USER_STATE_ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return !user.isBlocked();
    }
}
