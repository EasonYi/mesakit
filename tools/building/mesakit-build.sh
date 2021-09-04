#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source mesakit-library-functions.sh
source mesakit-library-build.sh
source mesakit-projects.sh

cd "$MESAKIT_HOME"/superpom
mvn clean install

export ALLOW_CLEANING=true

for project_home in "${MESAKIT_PROJECT_HOMES[@]}"; do

    build "$project_home" $@

    export ALLOW_CLEANING=false

done
