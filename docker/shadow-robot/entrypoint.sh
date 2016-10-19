#!/bin/bash
set -e

# setup ros environment
source "/workspace/shadow_robot/base/devel/setup.bash"
/start_gzweb.sh &

roslaunch  sr_robot_launch sr_right_ur10arm_hand.launch gui:=false
