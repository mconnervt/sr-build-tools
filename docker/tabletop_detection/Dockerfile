FROM ros:indigo

# using bash instead of sh to be able to source
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

ENV DEBIAN_FRONTEND noninteractive

RUN mkdir /{code,data,results} && \
    apt-get update && \
    apt-get install -y python-wstool python-catkin-tools && \
    cd /code && \
    mkdir -p ws/src && \
    cd ws/src && \
    wstool init . https://raw.github.com/wg-perception/object_recognition_core/master/doc/source/ork.rosinstall && \
    wstool update -j8 && \
    cd ecto_opencv && git checkout indigo && \
    cd ../.. && \
    rosdep install --from-paths src -i -y && \
    source /opt/ros/indigo/setup.bash && \
    catkin init 

RUN source /opt/ros/indigo/setup.bash && \
    cd /code/ws && \
    TERM=xterm catkin build && \
    source devel/setup.bash
