#!/bin/sh
mkdir ~/testrpmdb
mkdir ~/testinstall
rpm -Uvh --nodeps --dbpath ~/testrpmdb --prefix ~/testinstall ~/rpmbuild/RPMS/x86_64/ibex-client-7.1-1.el7.x86_64.rpm
echo To uninstall use  rpm -e --dbpath ~/testrpmdb ibex-client-7.1-1.el7.x86_64
