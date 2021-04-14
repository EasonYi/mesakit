#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

if [ -z "$1" ]; then

    echo "Usage: aonia-git-feature-start.sh [feature-name]"
    exit 0

else

    git-flow feature start Aonia-$1

fi
