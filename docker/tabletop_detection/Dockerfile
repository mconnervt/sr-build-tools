FROM ros:indigo

# using bash instead of sh to be able to source
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

ENV DEBIAN_FRONTEND noninteractive

RUN mkdir /{code,data,results} && \
    apt-get update && \
    apt-get install -y python-wstool python-catkin-tools && \
    cd /code && \
    mkdir ws && \
    cd ws && \
    wstool init src https://raw.github.com/wg-perception/object_recognition_core/master/doc/source/ork.rosinstall && \
    cd src && \
    wstool update -j8 && \
    cd .. && \
    rosdep install --from-paths src -i -y && \
    source /opt/ros/indigo/setup.bash && \
    catkin init && \
    catkin build && \
    source devel/setup.bash
