#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source mesakit-library-functions.sh
source mesakit-projects.sh

version="$1"

require_variable version "[version]"

snapshot_version="${1%-SNAPSHOT}-SNAPSHOT"

update_version $MESAKIT_HOME $snapshot_version
update_version $MESAKIT_EXAMPLES_HOME $snapshot_version
update_version $MESAKIT_EXTENSIONS_HOME $snapshot_version
