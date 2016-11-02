FROM ros:indigo

MAINTAINER "Shadow Robot's Software Team <software@shadowrobot.com>"

LABEL Description="ROS Indigo and OpenRAVE Docker image for Ubuntu Trusty" Vendor="Shadow Robot" Version="1.0"

ENV LIBS /root/libs

RUN echo "Installing needed libraries" && \
    apt-get update && \
    apt-get install \
    cmake g++ git ipython octave \
    python-dev python-h5py python-numpy \
    python-pip python-scipy python-sympy \
    qt4-dev-tools zlib-bin && \

    echo "Downgrading sympy " && \
    pip install --upgrade --user sympy==0.7.1 && \

    echo "Installing collada-dom-dev" && \
    apt-get install collada-dom-dev && \

    echo "Compile code" && \
    mkdir -p $LIBS && \

    echo "Install and compile libccd" && \
    cd $LIBS && \
    git clone --depth 1 https://github.com/danfis/libccd.git && \
    cd ./libccd && \
    mkdir build && \
    cd build && \
    cmake -G "Unix Makefiles" -DENABLE_DOUBLE_PRECISION=OFF .. && \
    make && \
    make install && \

    echo "Install and compile octomap" && \
    cd $LIBS && \
    git clone --depth 1 https://github.com/OctoMap/octomap.git && \
    cd ./octomap && \
    mkdir build && \
    cd build && \
    cmake .. && \
    make && \
    make install && \

    echo "Install and compile OpenSceneGraph" && \
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
