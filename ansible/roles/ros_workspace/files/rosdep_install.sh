#!/bin/bash

source ${1}/devel${2}/setup.bash
rosdep update
rosdep install --default-yes --all --ignore-src --skip-keys sr_ur_msgs --skip-keys cereal_port
