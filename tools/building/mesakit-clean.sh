#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source "$MESAKIT_WORKSPACE"/mesakit/tools/library/mesakit-library-functions.sh

clean_cache "$MESAKIT_CACHE_HOME"
clean_temporary_files "$MESAKIT_WORKSPACE"

bash mesakit-maven-setup.sh
