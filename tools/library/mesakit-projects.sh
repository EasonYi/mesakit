
declare -a MESAKIT_PROJECT_HOMES
MESAKIT_PROJECT_HOMES[0]="$MESAKIT_HOME/superpom"
MESAKIT_PROJECT_HOMES[1]="$MESAKIT_HOME"
MESAKIT_PROJECT_HOMES[2]="$MESAKIT_EXTENSIONS_HOME"
MESAKIT_PROJECT_HOMES[3]="$MESAKIT_EXAMPLES_HOME"
export MESAKIT_PROJECT_HOMES

declare -a MESAKIT_REPOSITORY_HOMES
MESAKIT_REPOSITORY_HOMES[0]="$MESAKIT_HOME"
MESAKIT_REPOSITORY_HOMES[1]="$MESAKIT_EXTENSIONS_HOME"
MESAKIT_REPOSITORY_HOMES[2]="$MESAKIT_EXAMPLES_HOME"
export MESAKIT_REPOSITORY_HOMES
