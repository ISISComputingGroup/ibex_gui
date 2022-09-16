Name:       ibex-client
Version:    %{ibexversion}
Release:    %{ibexrelease}%{?dist}
Summary:    STFC ISIS Facility IBEX Instrument Control Client

Group:      Applications/Engineering        
License:    EPL-1.0
URL:        https://github.com/ISISComputingGroup/
Prefix:     /usr/local
# Turn off automatic rpm dependency generation
Autoreq:    0

## stops all post install ops
#%global __os_install_post %{nil}

## Turn off the brp-python-bytecompile script
## which doesn't like jython
%global __os_install_post %(echo '%{__os_install_post}' | sed -e 's!/usr/lib[^[:space:]]*/brp-python-bytecompile[[:space:]].*$!!g')

## Turn off brp-java-repack-jars as it takes forever
%global __os_install_post %(echo '%{__os_install_post}' | sed -e 's!/usr/lib[^[:space:]]*/brp-java-repack-jars[[:space:]].*$!!g')

## Turn off brp-strip as it takes forever
%global __os_install_post %(echo '%{__os_install_post}' | sed -e 's!/usr/lib[^[:space:]]*/brp-strip[[:space:]].*$!!g')

## stripping symbols breaks some numpy libraries
%define debug_package %{nil}
%define __strip /bin/true

#BuildRequires:    
#Requires:    

%description
IBEX is the EPICS based control system used at the ISIS Pulsed Neutron and
Muon source. The IBEX client is used to connect to the IBEX server running
on an ISIS instrument and control/monitor data acquisition.

%prep

%build

%install
rm -fr "$RPM_BUILD_ROOT"
mkdir -p %{buildroot}/usr/local/ibex/client/
mkdir -p %{buildroot}/usr/local/ibex/etc/
mkdir -p %{buildroot}/usr/local/bin/
( cd /isis/data/jenkins/workspace/ibex_gui_linux_pipeline/base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64; cp -r . %{buildroot}/usr/local/ibex/client/ )
( cd %{buildroot}/usr/local/bin; ln -s ../ibex/client/ibex-client . )

echo CS:INSTLIST:NONE>%{buildroot}/usr/local/ibex/etc/instpv.txt

%clean
rm -fr "$RPM_BUILD_ROOT"

%files
/usr/local/ibex/client/
/usr/local/bin/
%config(noreplace) /usr/local/ibex/etc/instpv.txt

%changelog
* Wed Jul 29 2020 ISISExperimentControls  7.1-1.el7
- Initial RPM
