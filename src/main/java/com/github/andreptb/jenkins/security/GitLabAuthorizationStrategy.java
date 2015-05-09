package com.github.andreptb.jenkins.security;

import com.github.andreptb.jenkins.security.model.GitLabACL;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.security.ACL;
import hudson.security.AuthorizationStrategy;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

public class GitLabAuthorizationStrategy extends AuthorizationStrategy {

    @DataBoundConstructor
    public GitLabAuthorizationStrategy() {

    }

    @Nonnull
    @Override
    public ACL getACL(@Nonnull Job<?, ?> project) {
        return new GitLabACL(project);
    }

    @Nonnull
    @Override
    public ACL getRootACL() {
        return new GitLabACL(null);
    }

    @Nonnull
    @Override
    public Collection<String> getGroups() {
        return Collections.emptyList();
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
