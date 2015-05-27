package com.github.andreptb.jenkins.security.com.github.andreptb.gitlab;

import org.codehaus.jackson.annotate.JsonProperty;
import org.gitlab.api.models.GitlabAccessLevel;

public class GitLabProjectPermission {

    @JsonProperty("access_level")
    private int _accessLevel;
    @JsonProperty("notification_level")
    private int _notificationLevel;


    public GitlabAccessLevel getAccessLevel() {
        return GitlabAccessLevel.fromAccessValue(this._accessLevel);
    }

    public void setAccessLevel(GitlabAccessLevel _accessLevel) {
        this._accessLevel = _accessLevel.accessValue;
    }

    public int getNotificationLevel() {
        return _notificationLevel;
    }

    public void setNotificationLevel(int _notificationLevel) {
        this._notificationLevel = _notificationLevel;
    }
}
