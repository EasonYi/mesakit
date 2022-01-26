#!/bin/bash

source library-functions.sh

help="[host|container]"

workspace=$1

require_variable workspace "$help"

if [ "$workspace" = "host" ]; then
    workspace="/host/workspace"
elif [ "$workspace" = "container" ]; then
    workspace="/root/workspace"
else
    usage "$help"
fi

export MESAKIT_WORKSPACE="$workspace"

source ~/.profile
