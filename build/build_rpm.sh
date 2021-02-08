#!/bin/sh
set -o errexit
if test ! -d ~/rpmbuild; then
    rpmdev-setuptree
fi
VERSION=$1
RELEASE=$2
rm -fr ~/rpmbuild/BUILDROOT/ibex-client-*-*.el7.x86_64
rpmlint ibex-client.spec
## set QA_RPATHS as the embedded python shared libraries gives some RPATH errors
env QA_RPATHS=2 rpmbuild -bb --define "ibexversion ${VERSION}" --define "ibexrelease ${RELEASE}" ibex-client.spec
#rpmlint ~/rpmbuild/RPMS/x86_64/ibex-client-1-1.el7.x86_64.rpm
#rm -fr ~/rpmbuild/BUILDROOT/ibex-client-$(VERSION}${RELEASE}.el7.x86_64
