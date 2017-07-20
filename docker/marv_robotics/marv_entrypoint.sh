#!/bin/bash

set -e

source "/opt/ros/kinetic/setup.bash"
source /marv/venv/bin/activate

cd /marv/site

if [ ! -f /marv/site/uwsgi.conf ]; then
    echo "Can't find marvs config file - initialising Marv"
    marv init --scanroot /bags --approot /marv/site /marv/site
fi

uwsgi --ini /marv/site/uwsgi.conf
set +e
