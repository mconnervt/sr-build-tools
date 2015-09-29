#!/usr/bin/env bash

export toolset_branch=$1
export server_type=$2
export tags_list=$3

export docker_image=${docker_image_name:-"shadowrobot/ubuntu-ros-indigo-build-tools"}

# Do not install all libraries for circle and local run because we are using docker container directly
if  [ "circle" != $server_type ] && [ "semaphore_docker" != $server_type ] && [ "local" != $server_type ]; then

  export build_tools_folder="$HOME/sr-build-tools"

  sudo apt-get update
  sudo apt-get install python-dev libxml2-dev libxslt-dev python-pip lcov wget git -y
  sudo pip install ansible gcovr

  git config --global user.email "build.tools@example.com"
  git config --global user.name "Build Tools"

  # Check in case of cached file system
  if [ -d $build_tools_folder ]; then
    # Cached
    cd $build_tools_folder
    git pull origin "$toolset_branch"
    cd ./ansible
  else
    # No caching
    git clone https://github.com/shadow-robot/sr-build-tools.git -b "$toolset_branch" $build_tools_folder
    cd $build_tools_folder/ansible
  fi
fi

case $server_type in

"shippable") echo "Shippable server"
  export extra_variables="shippable_repo_dir=$SHIPPABLE_REPO_DIR  shippable_is_pull_request=$PULL_REQUEST codecov_secure=$CODECOV_TOKEN"
  sudo PYTHONUNBUFFERED=1 ansible-playbook -v -i "localhost," -c local docker_site.yml --tags "shippable,$tags_list" -e "$extra_variables"
  ;;

"semaphore") echo "Semaphore server"
  mkdir -p ~/workspace/src
  sudo apt-get remove mongodb-* -y
  sudo apt-get remove rabbitmq-* -y
  sudo apt-get remove redis-* -y
  sudo apt-get remove mysql-* -y
  sudo apt-get remove cassandra-* -y
  export extra_variables="semaphore_repo_dir=$SEMAPHORE_PROJECT_DIR  semaphore_is_pull_request=$PULL_REQUEST_NUMBER codecov_secure=$CODECOV_TOKEN"
  sudo PYTHONUNBUFFERED=1 ansible-playbook -v -i "localhost," -c local docker_site.yml --tags "semaphore,$tags_list" -e "$extra_variables"
  ;;

"semaphore_docker") echo "Semaphore server with Docker support"

  sudo docker pull $docker_image
  export extra_variables="semaphore_repo_dir=/host$SEMAPHORE_PROJECT_DIR semaphore_is_pull_request=$PULL_REQUEST_NUMBER codecov_secure=$CODECOV_TOKEN"
  sudo docker run -w "/root/sr-build-tools/ansible" -v /:/host:rw $docker_image  bash -c "git pull && git checkout $toolset_branch && sudo PYTHONUNBUFFERED=1 ansible-playbook -v -i \"localhost,\" -c local docker_site.yml --tags \"semaphore,$tags_list\" -e \"$extra_variables\" "
  ;;

"circle") echo "Circle CI server"
  export CIRCLE_REPO_DIR=$HOME/$CIRCLE_PROJECT_REPONAME
  docker pull $docker_image
  export extra_variables="circle_repo_dir=/host$CIRCLE_REPO_DIR  circle_is_pull_request=$CI_PULL_REQUEST circle_test_dir=/host$CI_REPORTS circle_code_coverage_dir=/host$CIRCLE_ARTIFACTS codecov_secure=$CODECOV_TOKEN"
  docker run -w "/root/sr-build-tools/ansible" -v /:/host:rw $docker_image  bash -c "git pull && git checkout $toolset_branch && sudo PYTHONUNBUFFERED=1 ansible-playbook -v -i \"localhost,\" -c local docker_site.yml --tags \"circle,$tags_list\" -e \"$extra_variables\" "
  ;;

"docker_hub") echo "Docker Hub"
  sudo PYTHONUNBUFFERED=1 ansible-playbook -v -i "localhost," -c local docker_site.yml --tags "docker_hub,$tags_list"
  ;;

"local") echo "Local run"
  export local_repo_dir=$4
  export image_home="/root"

  if [ -z "$unit_tests_result_dir" ]
  then
    export unit_tests_dir=$image_home"/workspace/test_results"
  else
    export unit_tests_dir="/host"$unit_tests_result_dir
  fi
  if [ -z "$coverage_tests_result_dir" ]
  then
    export coverage_tests_dir=$image_home"/workspace/coverage_results"
  else
    export coverage_tests_dir="/host"coverage_tests_result_dir
  fi
  docker pull $docker_image
  export extra_variables="local_repo_dir=/host$local_repo_dir local_test_dir=$unit_tests_dir local_code_coverage_dir=$coverage_tests_dir codecov_secure=$CODECOV_TOKEN"
  docker run -w "$image_home/sr-build-tools/ansible" --rm=true -v $HOME:/host:rw $docker_image  bash -c "export HOME=$image_home && git pull && git checkout $toolset_branch && git pull && sudo PYTHONUNBUFFERED=1 ansible-playbook -v -i \"localhost,\" -c local docker_site.yml --tags \"local,$tags_list\" -e \"$extra_variables\" "
  ;;

*) echo "Not supported server type $server_type"
  ;;
esac
