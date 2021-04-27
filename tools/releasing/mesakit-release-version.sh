#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source mesakit-projects.sh

help="[version]"

version=$1

require_variable version "$help"

for project_home in "${MESAKIT_ALL_HOMES[@]}"; do

    update_version $project_home $version

done
