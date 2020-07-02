#!/bin/sh
set -o errexit
sh build.sh
mkdir -p /misc/babylon/Scratch/IBEX/Linux
( cd ../base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64; tar czf /misc/babylon/Scratch/IBEX/Linux/ibex_client.tgz . )
echo Client deployed to  babylon/Scratch/IBEX/Linux
