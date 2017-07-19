#!/bin/bash

cd /marv
virtualenv -p python2.7 --system-site-packages venv
source /marv/venv/bin/activate
pip install -U pip setuptools
pip install -U marv-robotics
pip install -U uwsgi

echo ". /marv_env.sh" >> /etc/bash.bashrc
marv init --scanroot /bags --approot /marv/site /marv/site
