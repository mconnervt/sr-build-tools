FROM shadowrobot/checkui:ubuntu-trusty-ros-indigo-libccd-octomap

MAINTAINER "Shadow Robot's Software Team <software@shadowrobot.com>"

LABEL Description="ROS Indigo and OpenRAVE Docker image for Ubuntu Trusty" Vendor="Shadow Robot" Version="1.0"

ENV LIBS /root/libs

RUN echo "Install and compile OpenSceneGraph" && \
    cd $LIBS && \
    git clone --depth 1 -b 'OpenSceneGraph-3.4' https://github.com/openscenegraph/OpenSceneGraph.git && \
    cd ./OpenSceneGraph && \
    mkdir build && \
    cd build && \
    cmake .. && \
    make && \
    make install && \

    echo "Install and compile fcl" && \
    cd $LIBS && \
    git clone --depth 1 -b '0.5.0' https://github.com/flexible-collision-library/fcl.git && \
    cd ./fcl && \
    mkdir build && \
    cd build && \
    cmake .. && \
    make && \
    make install && \

    echo "Install and compile OpenRAVE" && \
    cd $LIBS && \
    git clone --depth 1 -b 'latest_stable' https://github.com/rdiankov/openrave.git && \
    cd ./openrave && \
    mkdir build && \
    cd build && \
    cmake .. && \
    make && \
    make install && \

    echo "Removing cache" && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
