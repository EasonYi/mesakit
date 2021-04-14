## Releasing Aonia &nbsp; ![](../images/rocket-40.png)

### Step-by-Step Instructions &nbsp; ![](../images/footprints-40.png)

This section documents how to release a new version of Aonia, step by step.

In the text below *\[aonia-version\]* refers to a [semantic versioning](https://semver.org) identifier, such  
as 2.1.7 or 1.0.0-beta.

Aonia adheres to the standard [Git Flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) branching model.

### 1. Creating the Release Branch ![](../images/branch-40.png)

Start a new release branch with the following command:

    aonia-release-start.sh [aonia-version]

This script does the following:

1. Creates the release branch *release/[aonia-version\]* using git flow
2. Updates *$AONIA_HOME/project.properties* file
3. Updates the version of all pom.xml files to *[aonia-version]*

Restart your terminal window to ensure all environment variables are updated.

### 2. Preparing the Release &nbsp; ![](../images/box-40.png)

Once the release branch has been created, several steps need to be performed manually to prepare  
the branch for publication.

#### 2.1 Building the Release

In order to ensure that the build will work on the build server, it is a good idea to completely  
clean out your maven repository and cache folders by building the project completely from scratch:

    aonia-build.sh all sparkling

This will remove (after prompting) the following before building:

1. Maven repository *~/.m2*
2. Aonia cache folder *~/.aonia/\[aonia-version\]*
3. Temporary files, logs, etc. in the source tree

#### 2.2 Building the Documentation

The following command will build Javadoc, UML diagrams and update project README.md indexes.

    aonia-build-documentation.sh

#### 2.3 Updating Code Flowers

To publish code flowers for the build:

1. Run *aonia-build-codeflowers.sh* to build codeflowers for Aonia
2. Insert in *$AONIA\_HOME/tools/codeflowers/site/index.html* the HTML code output by the build process   
   This will change the options in the dropdown to include any new projects
3. Run *aonia-upload-codeflowers.sh* to install the codeflowers on their web server

#### 2.4 Commit Changes

Commit any changes to the release branch.

### 3. Finishing and Publishing the Release Branch &nbsp;  ![](../images/stars-32.png)

The release is finished and merged into master with another script that uses git flow:

    aonia-release-finish.sh [aonia-version]

