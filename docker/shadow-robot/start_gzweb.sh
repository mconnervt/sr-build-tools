#!/bin/bash
set -e

# setup ros environment
sleep 30

source "/workspace/shadow_robot/base/devel/setup.bash"

cd /gzweb/gzweb
./start_gzweb.sh
