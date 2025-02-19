#!/usr/bin/perl

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

use strict;
use warnings FATAL => 'all';

#
# Include build script from cactus-build
#

if (!-d "cactus-build")
{
    system("git clone --branch develop --quiet https://github.com/Telenav/cactus-build.git");
}

require "./cactus-build/.github/scripts/build-include.pl";

#
# Clone repositories and build
#

my ($build_type) = @ARGV;
check_build_type($build_type);
my $github = "https://github.com/Telenav";

clone("$github/kivakit", "dependency");
clone("$github/kivakit-extensions", "dependency");
clone("$github/mesakit", "build");

build_kivakit($build_type);

system("mvn -DgroupId=com.telenav.kivakit -DartifactId=kivakit-merged-grpc -Dfile=./kivakit-extensions/kivakit-merged/lib/kivakit-merged-grpc-1.1.0.jar -Dversion=1.1.0 -Dpackaging=jar install:install-file");
system("mvn -DgroupId=com.telenav.kivakit -DartifactId=kivakit-merged-protostuff -Dfile=./kivakit-extensions/kivakit-merged/lib/kivakit-merged-protostuff-1.1.0.jar -Dversion=1.1.0 -Dpackaging=jar install:install-file");
system("mvn -DgroupId=com.telenav.kivakit -DartifactId=kivakit-merged-prometheus -Dfile=./kivakit-extensions/kivakit-merged/lib/kivakit-merged-prometheus-1.1.0.jar -Dversion=1.1.0 -Dpackaging=jar install:install-file");

build_kivakit_extensions($build_type);
build_mesakit($build_type);


