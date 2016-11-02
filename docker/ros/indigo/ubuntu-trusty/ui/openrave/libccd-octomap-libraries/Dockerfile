FROM ros:indigo

MAINTAINER "Shadow Robot's Software Team <software@shadowrobot.com>"

LABEL Description="ROS Indigo, libccd and octomap for Ubuntu Trusty" Vendor="Shadow Robot" Version="1.0"

ENV DEBIAN_FRONTEND noninteractive
ENV LIBS /root/libs

RUN echo "Installing needed libraries" && \
    apt-get update && \
    apt-get install -y \
    cmake g++ git ipython octave \
    python-dev python-h5py python-numpy \
    python-pip python-scipy python-sympy \
    qt4-dev-tools zlib-bin && \

    echo "Downgrading sympy " && \
    pip install --upgrade --user sympy==0.7.1 && \

    echo "Installing collada-dom-dev" && \
    apt-get install -y collada-dom-dev && \

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

    echo "Removing cache" && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
