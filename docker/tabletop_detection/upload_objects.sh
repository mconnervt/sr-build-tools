#!/bin/bash
set -e

source "/opt/ros/indigo/setup.bash"

for OBJECT in `ls /data/objects`; do
  echo ""
  echo "------"
  echo " -> adding new object: ${OBJECT%.*}"
  OBJECT_ID=$(rosrun object_recognition_core object_add.py -n ${OBJECT%.*} -d "${OBJECT%.*}" --commit)
  echo " -> adding stl and obj to ${OBJECT_ID##* }"
  rosrun object_recognition_core mesh_add.py ${OBJECT_ID##* } /data/objects/${OBJECT} --commit
  rosrun object_recognition_core mesh_add.py ${OBJECT_ID##* } /data/objects/${OBJECT} --commit
  echo "+++++++++"
done
