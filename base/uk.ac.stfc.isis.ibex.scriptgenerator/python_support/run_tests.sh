#!/bin/sh
set -o errexit
/usr/local/genie_python/bin/python3 -m unittest discover .
