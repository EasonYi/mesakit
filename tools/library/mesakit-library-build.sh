#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

usage() {

    SCRIPT=$1

    echo " "
    echo "Usage: $SCRIPT [build-type] [build-modifiers]*"
    echo " "
    echo "  Build types:"
    echo " "
    echo "       [default] - compile, shade and run all tests"
    echo " "
    echo "             all - clean-all, compile, shade, run tests, build tools and javadoc"
    echo " "
    echo "         compile - compile and shade (no tests)"
    echo " "
    echo "    deploy-ossrh - clean-sparkling, compile, run tests, attach jars, build javadoc, sign artifacts and deploy to OSSRH"
    echo " "
    echo "    deploy-local - clean-sparkling, compile, run tests, attach jars, build javadoc, sign artifacts and deploy to local Maven repository"
    echo " "
    echo "           tools - compile, shade, run tests, build tools"
    echo " "
    echo "             dmg - compile, shade, run tests, build tools, build dmg"
    echo " "
    echo "         javadoc - compile and build javadoc"
    echo " "
    echo "  Build modifiers:"
    echo " "
    echo "     attach-jars - attach source and javadoc jars to maven artifacts"
    echo " "
    echo "           clean - prompt to remove cached and temporary files"
    echo " "
    echo "       clean-all - prompt to remove cached and temporary files and mesakit artifacts from ~/.m2"
    echo " "
    echo " clean-sparkling - prompt to remove entire .m2 repository and all cached and temporary files"
    echo " "
    echo "           debug - turn maven debug mode on"
    echo " "
    echo "     debug-tests - stop in debugger on surefire tests"
    echo " "
    echo "         dry-run - show maven command line but don't build"
    echo " "
    echo "      no-javadoc - do not build javadoc"
    echo " "
    echo "        no-tests - do not run tests"
    echo " "
    echo "     quick-tests - run only quick tests"
    echo " "
    echo "           quiet - build with minimal output"
    echo " "
    echo " single-threaded - build with only one thread"
    echo " "
    echo "           tests - run all tests"
    echo " "
    exit 1
}

if [[ "$1" == "help" ]]; then

    SCRIPT=$(basename -- "$0")
    usage "$SCRIPT"
fi

addSwitch() {

    SWITCH="$1"

    if [ -z "$SWITCHES" ]; then
        SWITCHES=$SWITCH
    else
        SWITCHES="$SWITCHES $SWITCH"
    fi
}

build() {

    PROJECT=$1
    PROJECT_NAME=$(basename "$PROJECT")
    BUILD_TYPE=$2
    SWITCHES=""
    BUILD_ARGUMENTS=""

    case "${BUILD_TYPE}" in

    "all")
        JAVADOC=true
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded clean-all tests shade tools ${@:3})
        ;;

    "compile")
        BUILD_ARGUMENTS="clean compile"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded no-tests shade no-javadoc quiet ${@:3})
        ;;

    "deploy-ossrh")
        BUILD_ARGUMENTS="clean deploy"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded clean-sparkling tests attach-jars sign-artifacts ${@:3})
        export BUILD_DOCUMENTATION=true
        ;;

    "deploy-local")
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded no-javadoc clean-sparkling tests attach-jars sign-artifacts ${@:3})
        export BUILD_DOCUMENTATION=true
        ;;

    "deploy-local-release")
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded no-javadoc tests attach-jars sign-artifacts ${@:3})
        export BUILD_DOCUMENTATION=true
        ;;

    "javadoc")
        JAVADOC="true"
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded no-tests javadoc ${@:3})
        ;;

    "setup")
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded tests shade tools ${@:3})
        ;;

    "test")
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(single-threaded tests no-javadoc ${@:3})
        ;;

    "tools")
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded tests shade tools no-javadoc ${@:3})
        ;;

    "dmg")
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded tests shade tools dmg no-javadoc ${@:3})
        ;;

    *)
        BUILD_TYPE="default"
        BUILD_ARGUMENTS="clean install"
        # shellcheck disable=SC2206
        BUILD_MODIFIERS=(multi-threaded tests shade no-javadoc ${@:2})
        ;;

    esac

    BUILD_MODIFIERS_STRING=""
    DELIMITER=""

    # shellcheck disable=SC2068
    for MODIFIER in ${BUILD_MODIFIERS[@]}; do

        BUILD_MODIFIERS_STRING="$BUILD_MODIFIERS_STRING$DELIMITER$MODIFIER"
        DELIMITER=" "

        case "$MODIFIER" in

        "attach-jars")
            # To attach jars, we have to build the javadoc for the jars
            SWITCHES="${SWITCHES//-Dmaven.javadoc.skip=true/}"
            BUILD_ARGUMENTS="$BUILD_ARGUMENTS -Pattach-jars"
            ;;

        "clean")
            CLEAN_SCRIPT="mesakit-clean.sh"
            ;;

        "clean-all")
            CLEAN_SCRIPT="mesakit-clean-all.sh"
            ;;

        "clean-sparkling")
            CLEAN_SCRIPT="mesakit-clean-sparkling.sh"
            ;;

        "debug")
            addSwitch "--debug"
            ;;

        "debug-tests")
            addSwitch "-Dmaven.surefire.debug"
            ;;

        "dmg")
            addSwitch "-P dmg"
            ;;

        "docker")
            addSwitch "-P docker"
            ;;

        "javadoc")
            if [ -n "$JAVADOC" ]; then

                BUILD_ARGUMENTS="$BUILD_ARGUMENTS javadoc:aggregate"

            fi
            ;;

        "multi-threaded")
            THREADS=12
            ;;

        "no-javadoc")
            addSwitch "-Dmaven.javadoc.skip=true"
            ;;

        "no-tests")
            addSwitch "-Dmaven.test.skip=true"
            ;;

        "quick-tests")
            addSwitch "-P test-quick"
            ;;

        "quiet")
            addSwitch "-q -Dsurefire.printSummary=false -DKIVAKIT_LOG_LEVEL=Warning"
            ;;

        "shade")
            addSwitch "-P shade"
            ;;

        "dry-run")
            DRY_RUN="true"
            ;;

        "sign-artifacts")
            BUILD_ARGUMENTS="$BUILD_ARGUMENTS -P sign-artifacts"
            ;;

        "single-threaded")
            THREADS=1
            ;;

        "tests") ;;

        "tools")
            addSwitch "-P tools"
            ;;

        *)
            echo " "
            echo "Build modifier '$MODIFIER' is not recognized"
            usage "$SCRIPT"
            ;;

        esac
        shift

    done

    addSwitch "--threads $THREADS"

    if [ -z "$KIVAKIT_DEBUG" ]; then
        KIVAKIT_DEBUG="!Debug"
    fi

    BUILD_FOLDER="$PROJECT"

    if [ -f "$MESAKIT_HOME/build.properties" ]; then

        # shellcheck disable=SC2002
        MESAKIT_BUILD_NAME=$(cat "$MESAKIT_HOME"/build.properties | grep "build-name" | cut -d'=' -f2 | xargs echo)
        # shellcheck disable=SC2002
        MESAKIT_BUILD_DATE=$(cat "$MESAKIT_HOME"/build.properties | grep "build-date" | cut -d'=' -f2 | xargs echo)

    fi

    if [ -n "$MESAKIT_BUILD_NAME" ]; then

        MESAKIT_BUILD_NAME=" ($MESAKIT_BUILD_DATE $MESAKIT_BUILD_NAME)"

    fi

    if [ -e "$BUILD_FOLDER" ]; then

        echo " "
        echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Building '$PROJECT_NAME'$MESAKIT_BUILD_NAME ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
        echo "┋"
        echo "┋         Build-Folder: $BUILD_FOLDER"
        echo "┋           Build-Type: $BUILD_TYPE"
        echo "┋      Build-Modifiers: $BUILD_MODIFIERS_STRING"
        echo "┋   Maven Command Line: mvn -DKIVAKIT_DEBUG=\"$KIVAKIT_DEBUG\" $SWITCHES $BUILD_ARGUMENTS"
        echo "┋"

        if [ -z "$DRY_RUN" ]; then

            cd "$BUILD_FOLDER" || exit

            if [ -z "$CLEANED" ] && [ -n "$CLEAN_SCRIPT" ]; then

                bash "$CLEAN_SCRIPT"

                CLEANED=true

            fi

            if [ -z "$PREBUILT" ] && [ -n "$PREBUILD_SCRIPT" ]; then

                bash "$PREBUILD_SCRIPT"

                PREBUILT=true

            fi

            # shellcheck disable=SC2086
            "$M2_HOME/bin/mvn" --no-transfer-progress -DKIVAKIT_DEBUG=$KIVAKIT_DEBUG $SWITCHES $BUILD_ARGUMENTS 2>&1

            if [ "${PIPESTATUS[0]}" -ne "0" ]; then

                echo "Unable to build $PROJECT_NAME."
                exit 1

            fi

        fi

        # shellcheck disable=SC2002
        MESAKIT_BUILD_NAME=$(cat "$MESAKIT_HOME"/build.properties | grep "build-name" | cut -d'=' -f2 | xargs echo)
        # shellcheck disable=SC2002
        MESAKIT_BUILD_DATE=$(cat "$MESAKIT_HOME"/build.properties | grep "build-date" | cut -d'=' -f2 | xargs echo)

        if [ -n "$MESAKIT_BUILD_NAME" ]; then

            MESAKIT_BUILD_NAME=" ($MESAKIT_BUILD_DATE $MESAKIT_BUILD_NAME)"

        fi

        echo "┋"
        echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ Built '$PROJECT'$KIVAKIT_BUILD_NAME ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
        echo " "

    else

        echo "$PROJECT not found"
        exit 1

    fi
}
