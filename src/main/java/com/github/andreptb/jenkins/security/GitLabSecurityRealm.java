package com.github.andreptb.jenkins.security;

import com.github.andreptb.jenkins.security.model.GitLabUserDetails;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.security.AbstractPasswordBasedSecurityRealm;
import hudson.security.GroupDetails;
import hudson.security.SecurityRealm;
import hudson.util.FormValidation;
import hudson.util.Messages;
import hudson.util.Secret;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabSession;
import org.gitlab.api.models.GitlabUser;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.servlet.ServletException;
import java.io.IOException;

public class GitLabSecurityRealm extends AbstractPasswordBasedSecurityRealm {

    private String gitLabUrl;
    private String apiToken;

    @DataBoundConstructor
    public GitLabSecurityRealm(String gitLabUrl, String apiToken) {
        this.gitLabUrl = gitLabUrl;
        this.apiToken = apiToken;
    }

    @Override
    protected UserDetails authenticate(String username, String password) throws AuthenticationException {
        try {
            GitlabSession session = GitlabAPI.connect(this.gitLabUrl, username, password);
            return new GitLabUserDetails(session, this.gitLabUrl);
        } catch (IOException e) {
            throw new AuthenticationServiceException("HTTP request to establish session with GitLab failed", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        GitlabAPI api = GitlabAPI.connect(this.gitLabUrl, this.apiToken);
        try {
            GitlabUser user = api.retrieve().with("search", username).to(GitlabUser.URL, GitlabUser.class);
            return new GitLabUserDetails(user, this.gitLabUrl);
        } catch (IOException e) {
            throw new UsernameNotFoundException("Couldn't find user: " + username, e);
        }
    }

    @Override
    public GroupDetails loadGroupByGroupname(String groupName) throws UsernameNotFoundException, DataAccessException {
        GitlabAPI api = GitlabAPI.connect(this.gitLabUrl, this.apiToken);
        try {
            final GitlabGroup gitlabGroup = api.retrieve().with("search", groupName).to(GitlabGroup.URL, GitlabGroup.class);
            if (gitlabGroup == null) {
                throw new UsernameNotFoundException("Group not found", groupName);
            }
            return new GroupDetails() {
                @Override
                public String getName() {
                    return gitlabGroup.getName();
                }
            };
        } catch (IOException e) {
            throw new InvalidDataAccessApiUsageException("HTTP request to load group '" + groupName + "' failed", e);
        }
    }

    public String getGitLabUrl() {
        return gitLabUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {

        public FormValidation doCheckGitLabUrl(@QueryParameter String value) throws IOException, ServletException {
            return FormValidation.validateRequired(value);
        }

        public FormValidation doCheckApiToken(@QueryParameter Secret value, @QueryParameter String gitLabUrl) throws IOException, ServletException {
            String apiToken = value.getPlainText();
            if (StringUtils.isBlank(apiToken)) {
                return FormValidation.error(Messages.FormValidation_ValidateRequired());
            }
            if (StringUtils.isBlank(gitLabUrl)) {
                return FormValidation.error("Please inform GitLab's Server URL");
            }
            try {
                GitlabAPI api = GitlabAPI.connect(gitLabUrl, apiToken);
                GitlabUser userFromToken = api.getCurrentSession();
                String username = userFromToken.getName();
                if (!userFromToken.isAdmin()) {
                    return FormValidation.errorWithMarkup("API token owner <b>" + username + "</b> must have administrative privileges");
                }
                return FormValidation.okWithMarkup("Connection established succesfully (API token from owner <b>" + username + "</b>)");
            } catch (JsonParseException e) {
                return FormValidation.error(e, "Unexpected response from server, please confirm if GitLab is responding properly");
            } catch (Exception e) {
                return FormValidation.error(e, "Connection with GitLab failed");
            }
        }

        /**
         * Gives the name to be displayed by the Jenkins view in the security configuration page.
         *
         * @return the display name
         */
        public String getDisplayName() {
            return "GitLab";
        }
    }
}