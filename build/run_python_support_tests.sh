pushd `dirname $0`
SCRIPTDIR=`pwd`
popd
pushd $SCRIPTDIR/../base/uk.ac.stfc.isis.ibex.scriptgenerator/python_support
./run_tests.sh
popd
