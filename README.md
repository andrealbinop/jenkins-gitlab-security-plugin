jenkins-gitlab-security-plugin [![Build Status](https://travis-ci.org/andreptb/jenkins-gitlab-security-plugin.svg)](https://travis-ci.org/andreptb/jenkins-gitlab-security-plugin)
==============

[Jenkins](https://jenkins-ci.org/) plugin to manage authentication and authorization through [GitLab API](https://github.com/gitlabhq/gitlabhq/tree/master/doc/api).

Since [gitlab-auth-plugin](https://github.com/enil/gitlab-auth-plugin) don't seem to [get a stable release any time soon](https://github.com/enil/gitlab-auth-plugin/issues/1), I've decided to make a new one using on [this](https://github.com/timols/java-gitlab-api) API.

Though some features will be similar, this plugin have differences from [gitlab-auth-plugin](https://github.com/enil/gitlab-auth-plugin).

### Development

Start the local Jenkins instance:

```shell
mvn hpi:run
```

### How to install

Run to create the plugin .hpi file:

```shell
mvn clean package
```

To install:

1. copy the resulting ./target/gitlab-security-plugin.hpi file to the $JENKINS_HOME/plugins directory. Don't forget to restart Jenkins afterwards.

2. or use the plugin management console (http://example.com:8080/pluginManager/advanced) to upload the hpi file. You have to restart Jenkins in order to find the pluing in the installed plugins list.


### Features

At this moment, the plugin have to main features:

- **Security Realm**: Delegates user's credentials validation to GitLab's service. Any existing GitLab account will be allowed to authenticate on Jenkins.
- **Authorization Strategy**: An opinionated approach on Jenkins authorization model. Requires **GitLab's security realm** to be enabled. Leverages GitLab's user general and repository specific permissions to determine Jenkins' user access state.

### How Authorization Strategy works

The authorization strategy provided by this plugin tries to follow as close as possible [GitLab's authorization model](https://gitlab.com/gitlab-org/gitlab-ce/blob/master/doc/permissions/permissions.md).  Note that authorization also occurs in a per job basis, and this plugin does that by [binding Jenkins' jobs with GitLab's repositories](#How GitLab's Git repositories and Jenkins jobs association works).

To do this the following group of rules are enforced when this strategy is enabled:

* Any active GitLab registered user will be allowed to authenticate on Jenkins.
* An user with administrative privileges will be considered as a Jenkins administrator.
* A GitLab group member with at least **Guest** or **Reporter** permission will be allowed to view any Jenkins' jobs associated with any repositories owned by the group.
* A GitLab group member with at least **Developer** permission will be allowed to run and configure any Jenkins' jobs associated with any repositories owned by the group.
* A GitLab group member with **Master** or **Owner** permission will be allowed to administer any Jenkins' jobs associated with any repositories owned by the group.
* A GitLab repository member with at least **Guest** or **Reporter** permission will be allowed to view any Jenkins' jobs associated with this repository.
* A GitLab repository member with at least **Developer** permission will be allowed to run and configure any Jenkins' jobs associated with this repository.
* A GitLab repository member with **Master** or **Owner** permission will be allowed to administer any Jenkins' jobs associated with this repository.

### How GitLab's Git repositories and Jenkins jobs association works

To leverage user's authorizations of a Jenkins' job this plugins tries to find a related GitLab repository and [enforces it's access rules](#How Authorization Strategy works).
Any job with **Source Code Management** configured with [Git](https://wiki.jenkins-ci.org/display/JENKINS/Git+Plugin) and a remote repository URL that matches a GitLab repository will be associated.
