#!/bin/sh
set -o errexit
if test ! -d ~/rpmbuild; then
    rpmdev-setuptree
fi
VERSION=7.1
BUILDVER=1
rpmlint ibex-client.spec
## set QA_RPATHS as the embedded python shared libraries gives some RPATH errors
env QA_RPATHS=2 rpmbuild -bb --define "ibexversion ${VERSION}" --define "ibexbuild ${BUILDVER}" ibex-client.spec
#rpmlint ~/rpmbuild/RPMS/x86_64/ibex-client-1-1.el7.x86_64.rpm
