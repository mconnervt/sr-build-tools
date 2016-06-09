#!/bin/bash

/opt/startup.sh &

aptly repo create -distribution=trusty -component=main shadow-private
aptly repo add shadow-private /debs
aptly snapshot create shadow-private from repo shadow-private

aptly publish snapshot -passphrase="${GPG_PASSWORD}" shadow-private
aptly serve
