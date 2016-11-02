FROM ubuntu:14.04

MAINTAINER "Shadow Robot's Software Team <software@shadowrobot.com>"

LABEL Description="Simple UI Docker image for Ubuntu Trusty" Vendor="Shadow Robot" Version="1.0"

ENV DEBIAN_FRONTEND noninteractive
ENV USERNAME dev
ENV PULSE_SERVER /run/pulse/native

RUN echo "Installing packages" && \
    apt-get update && \
    apt-get install -y \
        curl \
        screen \
        byobu \
        fish \
        git \
        nano \
        glances && \

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

	echo "Installing browser" && \
	apt-get install -y firefox && \

    echo "Add user" && \
    useradd -m $USERNAME && \
	echo "$USERNAME:$USERNAME" | chpasswd && \
	usermod --shell /bin/bash $USERNAME && \
	usermod -aG sudo $USERNAME && \
	echo "$USERNAME ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers.d/$USERNAME && \
	chmod 0440 /etc/sudoers.d/$USERNAME

USER dev