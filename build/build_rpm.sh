#!/bin/sh
set -o errexit
if test ! -d ~/rpmbuild; then
    rpmdev-setuptree
fi
VERSION=$1
RELEASE=$2
rm -fr ~/rpmbuild/BUILDROOT/ibex-client-*-*.el*.x86_64
rm -f ~/rpmbuild/RPMS/x86_64/ibex-client-*-*.el*.x86_64.rpm
rpmlint ibex-client.spec
## set QA_RPATHS as the embedded python shared libraries gives some RPATH errors
env QA_RPATHS=$(( 0x0002|0x0008 )) rpmbuild -bb --define "ibexversion ${VERSION}" --define "ibexrelease ${RELEASE}" ibex-client.spec
#rpmlint ~/rpmbuild/RPMS/x86_64/ibex-client-1-1.el*.x86_64.rpm
#rm -fr ~/rpmbuild/BUILDROOT/ibex-client-$(VERSION}${RELEASE}.el*.x86_64
