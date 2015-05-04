package com.github.andreptb.jenkins.security;

import com.github.andreptb.jenkins.security.model.GitLabUserDetails;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.security.ACL;
import hudson.security.AuthorizationStrategy;
import hudson.security.Permission;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import java.util.Collection;

public class GitLabAuthorizationStrategy extends AuthorizationStrategy {

    @Nonnull
    @Override
    public ACL getACL(@Nonnull Job<?, ?> project) {
        return new ACL() {
            @Override
            public boolean hasPermission(@Nonnull Authentication a, @Nonnull Permission permission) {
                return false;
            }
        };
    }

    @Nonnull
    @Override
    public ACL getRootACL() {
        return new ACL() {
            @Override
            public boolean hasPermission(@Nonnull Authentication a, @Nonnull Permission permission) {
                GrantedAuthority[] authorities = a.getAuthorities();
                for(GrantedAuthority authority : authorities) {
                    if(StringUtils.equals(authority.getAuthority(), GitLabUserDetails.GITLAB_ADMIN)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Nonnull
    @Override
    public Collection<String> getGroups() {
        return null;
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<AuthorizationStrategy> {

        /**
         * Gives the name to be displayed by the Jenkins view in the security configuration page.
         *
         * @return the display name
         */
        public String getDisplayName() {
            return "GitLab permissions";
        }
    }
}
