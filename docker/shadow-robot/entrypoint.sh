#!/bin/bash
set -e

# setup ros environment
source "/installed_workspace/setup.bash"
/start_gzweb.sh &

roslaunch sr_hand gazebo_arm_and_hand.launch gui:=false
