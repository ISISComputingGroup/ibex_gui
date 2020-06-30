#!/bin/sh
set -o errexit
sh build.sh
mkdir -p /misc/babylon/Scratch/IBEX/
rm -fr /misc/babylon/Scratch/IBEX/Client
cp -r ../base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64  /misc/babylon/Scratch/IBEX/Client
