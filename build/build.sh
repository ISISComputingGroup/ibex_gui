#!/bin/bash
set -e

SCRIPT=$(readlink -f "${BASH_SOURCE[0]}")
SCRIPTPATH="`dirname \"$SCRIPT\"`"
TOPPATH="`dirname \"$SCRIPTPATH\"`"

#sed -i -e 's%<os>win32</os>%<os>linux</os>%' -e 's%<ws>win32</ws>%<ws>gtk</ws>%'  $TOPPATH/base/uk.ac.stfc.isis.ibex.client.tycho.parent/pom.xml

mvn --settings="$TOPPATH/mvn_user_settings.xml" \
    -f "$TOPPATH/base/uk.ac.stfc.isis.ibex.client.tycho.parent/pom.xml" \
    -DforceContextQualifier="$BUILD_NUMBER" \
    clean verify

echo "Client built in $TOPPATH/base/uk.ac.stfc.isis.ibex.client.product/target/products/ibex.product/"

