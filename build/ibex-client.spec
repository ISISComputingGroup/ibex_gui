Name:       ibex-client
Version:    %{ibexversion}
Release:    %{ibexbuild}%{?dist}
Summary:    Ibex Client

#Group:        
License:    EPL-1.0
URL:        https://github.com/ISISComputingGroup/
Prefix:     /usr/local

## Turn off the brp-python-bytecompile script
## which doesn't like jython
%global __os_install_post %(echo '%{__os_install_post}' | sed -e 's!/usr/lib[^[:space:]]*/brp-python-bytecompile[[:space:]].*$!!g')
## Turn off brp-java-repack-jars as it takes forever
%global __os_install_post %(echo '%{__os_install_post}' | sed -e 's!/usr/lib[^[:space:]]*/brp-java-repack-jars[[:space:]].*$!!g')

#BuildRequires:    
#Requires:    

%description
ibex client

%prep

%build

%install
rm -fr "$RPM_BUILD_ROOT"
mkdir -p %{buildroot}/usr/local/ibex/client/
mkdir -p %{buildroot}/usr/local/bin/
( cd /isis/data/jenkins/workspace/ibex_gui_linux_pipeline/base/uk.ac.stfc.isis.ibex.e4.client.product/target/products/ibex.product/linux/gtk/x86_64; cp -r . %{buildroot}/usr/local/ibex/client/ )
( cd %{buildroot}/usr/local/bin; ln -s ../ibex/client/ibex-client . )

%clean
rm -fr "$RPM_BUILD_ROOT"

%files
/usr/local/ibex/
/usr/local/bin/
%doc

%changelog
