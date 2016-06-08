FROM ubuntu:16.04

ENV DEBIAN_FRONTEND noninteractive

# installing
RUN apt-key adv --keyserver keys.gnupg.net --recv-keys 9E3E53F19C7DE460 && \
    echo "deb http://repo.aptly.info/ squeeze main" > /etc/apt/sources.list.d/aptly.list && \
    apt-get update && \
    apt-get dist-upgrade && \
    apt-get install -y aptly

# may be install rundeck for automating vpn key generation
