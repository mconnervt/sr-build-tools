FROM ros:indigo

# using bash instead of sh to be able to source
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

ENV DEBIAN_FRONTEND noninteractive

# installing object recognition
RUN mkdir /{code,data,results} && \
    apt-get update && \
    apt-get install -y ros-indigo-object-recognition-* && \
    mkdir /data/objects

COPY objects /data/objects/
COPY upload_objects.sh /

# create the db
RUN apt-get install -y couchdb python-pip && \
    sed -i 's/;port = 5984/port = 5984/g' /etc/couchdb/local.ini && \
    sed -i 's/;bind_address = 127.0.0.1/bind_address = 0.0.0.0/g' /etc/couchdb/local.ini && \
    cat /etc/couchdb/local.ini && \
    mkdir -p /var/run/couchdb && \
    couchdb start -b && \
    pip install -U couchapp && \
    source /opt/ros/indigo/setup.bash && \
    rosrun object_recognition_core push.sh && \
    chmod -R a+rw /var/lib/couchdb && \
    /upload_objects.sh

EXPOSE 5984

CMD ["couchdb", "start"]
