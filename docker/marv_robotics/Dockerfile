FROM ros:kinetic-ros-base

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y curl ros-kinetic-ros-base python-cv-bridge python2.7-dev python-opencv python-virtualenv libjpeg-dev libz-dev && \
    mkdir -p /marv/site && \
    mkdir /bags

COPY marv_entrypoint.sh /
COPY marv_env.sh /
COPY install_marv.sh /
RUN echo ". /marv_env.sh" >> /etc/bash.bashrc
RUN /install_marv.sh

WORKDIR /marv/site
USER root
ENTRYPOINT ["/marv_entrypoint.sh"]
CMD ["uwsgi", "--ini", "/marv/site/uwsgi.conf"]
