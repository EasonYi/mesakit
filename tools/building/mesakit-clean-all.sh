#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source mesakit-library-functions.sh
source mesakit-projects.sh

source mesakit-projects.sh

for project_home in "${MESAKIT_PROJECT_HOMES[@]}"; do

    clean_maven_repository $project_home

done

bash mesakit-clean.sh
