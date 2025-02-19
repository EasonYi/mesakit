#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source mesakit-library-functions.sh
source mesakit-projects.sh

help="[version]"

version=$1

require_variable version "$help"

for project_home in "${MESAKIT_REPOSITORY_HOMES[@]}"; do

    if ! git_flow_init "$project_home"; then

        exit 1

    fi

done

for project_home in "${MESAKIT_REPOSITORY_HOMES[@]}"; do

    git_flow_release_start "$project_home" "$version"

done
