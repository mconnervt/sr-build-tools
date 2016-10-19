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
    curl -sL https://deb.nodesource.com/setup_6.x | sudo bash - && \
    apt-get install -y ros-indigo-gazebo7-ros-pkgs ros-indigo-gazebo7-ros-control libjansson-dev nodejs libboost-dev imagemagick libtinyxml-dev mercurial cmake build-essential python-gts libgts-dev && \
    wget https://raw.githubusercontent.com/osrf/osrf-rosdep/master/gazebo7/00-gazebo7.list -O /etc/ros/rosdep/sources.list.d/00-gazebo7.list && \
    rosdep update && \
    mkdir /gzweb && \
    cd /gzweb && \
    hg clone https://bitbucket.org/osrf/gzweb && \
    cd gzweb && \
    hg up gzweb_2.0.0 && \
    source /opt/ros/indigo/setup.bash && \
    ./deploy.sh -m -c

# setup entrypoint
COPY ./entrypoint.sh /
COPY ./start_gzweb.sh /

ENTRYPOINT ["/entrypoint.sh"]
CMD ["bash"]
