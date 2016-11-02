FROM shadowrobot/checkui:ubuntu-trusty-ros-indigo-libccd-octomap

MAINTAINER "Shadow Robot's Software Team <software@shadowrobot.com>"

LABEL Description="ROS Indigo and OpenRAVE Docker image for Ubuntu Trusty" Vendor="Shadow Robot" Version="1.0"

ENV USERNAME dev
ENV PULSE_SERVER /run/pulse/native
ENV LIBS /root/libs

RUN echo "Install and compile fcl" && \
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

    echo "GUI and sound" && \
    apt-get install -y \
		xz-utils file locales dbus-x11 pulseaudio dmz-cursor-theme \
		fonts-dejavu fonts-liberation hicolor-icon-theme \
		libcanberra-gtk3-0 libcanberra-gtk-module libcanberra-gtk3-module \
		libasound2 libgtk2.0-0 libdbus-glib-1-2 libxt6 libexif12 \
		libgl1-mesa-glx libgl1-mesa-dri && \
 	update-locale LANG=C.UTF-8 LC_MESSAGES=POSIX && \

    echo "Install GUI tools" && \
    apt-get install -y \
	    terminator \
	    cmake-qt-gui \
	    gedit && \

    echo "Add user" && \
    useradd -m $USERNAME && \
	echo "$USERNAME:$USERNAME" | chpasswd && \
	usermod --shell /bin/bash $USERNAME && \
	usermod -aG sudo $USERNAME && \
	echo "$USERNAME ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers.d/$USERNAME && \
	chmod 0440 /etc/sudoers.d/$USERNAME && \

    echo "Removing cache" && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

USER dev