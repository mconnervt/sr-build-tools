#!/bin/bash
set -e

# setup ros environment
sleep 30

source "/installed_workspace/setup.bash"

rostopic list

rostopic echo /joint_states

cd /gzweb/gzweb
./start_gzweb.sh
