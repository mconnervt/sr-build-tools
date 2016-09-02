FROM moveit/moveit:kinetic-experimental

RUN cd ~/ws_moveit/src && \
    wstool rm moveit && \
    rm -rf moveit && \
    wstool set -yu moveit -v F_new_benchmarking_PR_WMD --git https://github.com/shadow-robot/moveit && \
    cd ~/ws_moveit && \
    source ~/ws_moveit/devel/setup.bash && \
    catkin build

RUN mkdir -p ~/ws_ur/src && \
    source ~/ws_moveit/devel/setup.bash && \
    cd ~/ws_ur/src && \
    wstool init . && \
    wstool set -yu universal_robot -v F_fix_build_kinetic --git https://github.com/shadow-robot/universal_robot && \
    cd ~/ws_ur && \
    catkin init && \
    catkin build
