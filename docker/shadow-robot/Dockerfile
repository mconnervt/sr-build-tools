FROM ros:indigo

# using bash instead of sh to be able to source
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && \
    apt-get install -y curl && \
    curl -L bit.ly/dev-machine | bash -s -- -w /workspace/shadow_robot/base

# install gzweb
RUN sh -c 'echo "deb http://packages.osrfoundation.org/gazebo/ubuntu trusty main" > /etc/apt/sources.list.d/gazebo-latest.list' && \
    wget http://packages.osrfoundation.org/gazebo.key -O - |  apt-key add - && \
    apt-get update && \
    apt-get dist-upgrade -y && \
    apt-get install -y ros-indigo-gazebo5-ros-pkgs ros-indigo-gazebo5-ros-control libjansson-dev npm nodejs libboost-dev imagemagick libtinyxml-dev mercurial cmake build-essential python-gts libgts-dev && \
    wget https://raw.githubusercontent.com/osrf/osrf-rosdep/master/gazebo5/00-gazebo5.list -O /etc/ros/rosdep/sources.list.d/00-gazebo5.list && \
    rosdep update && \
    ln -s /usr/bin/nodejs /usr/bin/node && \
    mkdir /gzweb && \
    cd /gzweb && \
    hg clone https://bitbucket.org/osrf/gzweb && \
    cd gzweb && \
    hg up gzweb_1.2.0 && \
    source /opt/ros/indigo/setup.bash && \
    ./deploy.sh -m -c

# Cleaning up, deleting all sources
RUN curl -L bit.ly/dev-machine | bash -s -- -w /workspace/shadow_robot/base && \
    cd /workspace/shadow_robot/base_deps && \
    source /opt/ros/indigo/setup.bash && \
    rospack profile && \
    catkin_make -DCMAKE_INSTALL_PREFIX=/installed_workspace install && \
    source /installed_workspace/setup.bash && \
    rospack profile && \
    cd ../base && \
    rm -rf {build,devel} && \
    catkin_make -DCMAKE_INSTALL_PREFIX=/installed_workspace && \
    catkin_make -DCMAKE_INSTALL_PREFIX=/installed_workspace install && \
    rm -rf  /workspace/shadow_robot

# setup entrypoint
COPY ./entrypoint.sh /
COPY ./start_gzweb.sh /

ENTRYPOINT ["/entrypoint.sh"]
CMD ["bash"]
