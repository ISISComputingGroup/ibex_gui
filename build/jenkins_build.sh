#!/bin/sh
set -o errexit
sh build.sh
sh build_rpm.sh ${BUILD_NUMBER}
mkdir -p /misc/babylon/Scratch/IBEX/Linux
rm -f /misc/babylon/Scratch/IBEX/Linux/ibex-client-7.1-*.el7.x86_64.rpm
cp -f ~/rpmbuild/RPMS/x86_64/ibex-client-7.1-${BUILD_NUMBER}.el7.x86_64.rpm /misc/babylon/Scratch/IBEX/Linux
rm -f ~/rpmbuild/RPMS/x86_64/ibex-client-7.1-${BUILD_NUMBER}.el7.x86_64.rpm
( cd ../base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64; tar czf /misc/babylon/Scratch/IBEX/Linux/ibex_client.tgz . )
echo Client deployed to  babylon/Scratch/IBEX/Linux
