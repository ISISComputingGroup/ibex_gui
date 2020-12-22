#!/bin/sh
set -o errexit
version=0.0 # dummy version, will have git in release field
gitver=`git rev-parse --short HEAD`
release=${BUILD_NUMBER}.`date +%Y%m%d`git${gitver}
sh build.sh
sh build_rpm.sh ${version} ${release}
mkdir -p /misc/babylon/Scratch/IBEX/Linux
rm -f /misc/babylon/Scratch/IBEX/Linux/ibex-client-*-*.el7.x86_64.rpm
cp -f ~/rpmbuild/RPMS/x86_64/ibex-client-*-*.el7.x86_64.rpm /misc/babylon/Scratch/IBEX/Linux
rm -f ~/rpmbuild/RPMS/x86_64/ibex-client-*-*.el7.x86_64.rpm
( cd ../base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64; tar czf /misc/babylon/Scratch/IBEX/Linux/ibex_client.tgz . )
echo Client deployed to  babylon/Scratch/IBEX/Linux
