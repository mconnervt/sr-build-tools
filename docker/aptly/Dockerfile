FROM bryanhong/aptly

# creating the aptly repo
RUN mkdir /debs

# may be install some ui for automating
# -> vpn or ssh key generation / add
# -> add new deb files
# -> snapshot aptly version

COPY entrypoint.sh /opt/

ENTRYPOINT ["/opt/entrypoint.sh"]
CMD ["bash"]


#docker run --detach=true --log-driver=syslog --name="aptly" --env FULL_NAME="Ugo Cupcic" --env EMAIL_ADDRESS="ugo@shadowrobot.com" --env GPG_PASSWORD="shadow" --env HOSTNAME=locahost -v /projects/data/aptly/data:/opt/aptly:rw -v /projects/data/aptly/debs:/debs:ro -p 80:80 shadowrobot/aptly
