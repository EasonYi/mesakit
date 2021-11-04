#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source mesakit-library-functions.sh
source mesakit-projects.sh

help="[feature-name]"

feature_name=$1

require_variable feature_name "$help"

for project_home in "${MESAKIT_PROJECT_HOMES[@]}"; do

    git_flow_feature_finish "$project_home" "$feature_name"

done
