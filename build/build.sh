#!/bin/bash
set -e

#export JAVA_HOME=/opt/jdk-17
export PATH=${JAVA_HOME}/bin:${PATH}
export JAVA_TOOL_OPTIONS="-Djdk.util.zip.disableZip64ExtraFieldValidation=true"

SCRIPT=$(readlink -f "${BASH_SOURCE[0]}")
SCRIPTPATH="`dirname \"$SCRIPT\"`"
TOPPATH="`dirname \"$SCRIPTPATH\"`"

sh ./run_python_support_tests.sh

mvn --settings="$TOPPATH/mvn_user_settings.xml" \
    -f "$TOPPATH/base/uk.ac.stfc.isis.ibex.client.tycho.parent/pom.xml" \
    -DforceContextQualifier="$BUILD_NUMBER" \
    -DskipTests \
    clean verify
    
#cp -r '/misc/inst/Kits$/CompGroup/ICP/ibex_client_jre_linux' "$TOPPATH/base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64/jre"
cp --dereference -r ${JAVA_HOME} "$TOPPATH/base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64/jre"

clientbuild="$TOPPATH/base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64"
echo "Client built in $clientbuild"

pydir=`python get_python_write_dir.py $clientbuild`

mkdir -p $pydir
( cd $pydir; tar -xzf /misc/babylon/Scratch/IBEX/Linux/genie_python.tgz )
## link caRepeater so in path
( cd $pydir/lib/python3.*/site-packages; ln -sf ../../../bin/caRepeater . )
