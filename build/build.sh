#!/bin/bash
set -e

SCRIPT=$(readlink -f "${BASH_SOURCE[0]}")
SCRIPTPATH="`dirname \"$SCRIPT\"`"
TOPPATH="`dirname \"$SCRIPTPATH\"`"

mvn --settings="$TOPPATH/mvn_user_settings.xml" \
    -f "$TOPPATH/base/uk.ac.stfc.isis.ibex.client.tycho.parent/pom.xml" \
    -DforceContextQualifier="$BUILD_NUMBER" \
    -DskipTests \
    clean verify
    
cp -r "\\isis\inst$\Kits$\CompGroup\ICP\ibex_client_jre_linux" "Client built in $TOPPATH/base/uk.ac.stfc.isis.ibex.client.product/target/products/ibex.product/linux/gtk/x86_64/jre"

echo "Client built in $TOPPATH/base/uk.ac.stfc.isis.ibex.client.product/target/products/ibex.product/linux/gtk/x86_64"

