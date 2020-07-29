#!/bin/sh
set -o errexit
sh build.sh
sh build_rpm.sh
mkdir -p /misc/babylon/Scratch/IBEX/Linux
cp -f ~/rpmbuild/RPMS/x86_64/ibex-client-7.1-1.el7.x86_64.rpm /misc/babylon/Scratch/IBEX/Linux
( cd ../base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64; tar czf /misc/babylon/Scratch/IBEX/Linux/ibex_client.tgz . )
echo Client deployed to  babylon/Scratch/IBEX/Linux
