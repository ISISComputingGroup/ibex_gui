#!/bin/sh
set -o errexit
pushd `dirname $0`
SCRIPTDIR=`pwd`
popd
pushd $SCRIPTDIR/../base/uk.ac.stfc.isis.ibex.scriptgenerator/python_support
sh ./run_tests.sh
popd
