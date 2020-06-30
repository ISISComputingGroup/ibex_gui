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
    
cp -r '/misc/inst/Kits$/CompGroup/ICP/ibex_client_jre_linux' "$TOPPATH/base/uk.ac.stfc.isis.ibex.client.product/target/products/ibex.product/linux/gtk/x86_64/jre"
# cp -r /etc/alternatives/jre_11_openjdk "$TOPPATH/base/uk.ac.stfc.isis.ibex.client.product/target/products/ibex.product/linux/gtk/x86_64/jre"

echo "Client built in $TOPPATH/base/uk.ac.stfc.isis.ibex.client.product/target/products/ibex.product/linux/gtk/x86_64"
