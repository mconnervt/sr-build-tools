FROM shadowrobot/kinetic:latest

RUN curl -L https://raw.githubusercontent.com/shadow-robot/sr-build-tools/F_indigo_to_kinetic/bin/setup_dev_machine \
    | bash -s -- -w /home/user/shadow_robot/base -b F_kinetic -c F_kinetic \
    -r shadow_robot-kinetic.rosinstall

# Cleaning up, deleting all sources
RUN cd /workspace/shadow_robot/base_deps && \
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

ENTRYPOINT ["/entrypoint.sh"]
CMD ["bash"]
