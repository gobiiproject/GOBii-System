#!/usr/bin/env bash
#WARNING: Do not run this script as SUDO! There are explicit sudo commands which will prompt you for password. But not everything should run as sudo.
#usage: bash.sh <path-of-parms-file> <dockerhubpassw> <gobii_release_version>
#This a stand-alone equivalent of my THE_GOBII_SHIP Bamboo plan for the DB NODE
#Requirements:
#1. The user that will run this script needs to be a sudoer and under the gobii and docker groups. So preferably the user 'gadm'.
#2. The working directory needs to be where the gobiiconfig_wrapper.sh is as well, typically <gobii_bundle>/conf/
#NOTE: The order of execution is important.
#NOTE: If weird things start happening on your dockers, try removing the images as well by running 'docker rmi' on each of the 3 nodes.
# If you want a delete-all-images command, run this: sudo docker stop $(sudo docker ps -qa) || true && sudo docker rm $(sudo docker ps -aq) || true && sudo docker rmi $(sudo docker images -aq) || true
#@author: (palace) kdp44@cornell.edu

if [[ $EUID -eq 0 ]]; then
    error "This script should not be run using sudo or as the root user. Dangerous times, my friend."
    exit 1
fi

#--------------------------------------------------#
### ALL NODES ###
#--------------------------------------------------#
set -u
set -e
set -x
#load parameters
source $1
DOCKER_HUB_PASSWORD=$2
GOBII_RELEASE_VERSION=$3
#GOBII_RELEASE_VERSION="release-0.3-73" #FOR TESTS
echo "The GOBII Ship is sailing..."

#create a symlink for the loader UI to work
sudo ln -sfn $BUNDLE_PARENT_PATH /data

#--------------------------------------------------#
### DB NODE ###
#--------------------------------------------------#
echo "Installing the DB node..."
#Stop and remove DB docker container [DISABLED IN PRODUCTION SYSTEMS - ONLY ENABLE IF DOING A FRESH INSTALL]
#WARNING: THIS WILL REPLACE YOUR DATABASE DOCKER NODE
docker stop $DOCKER_DB_NAME || true && docker rm $DOCKER_DB_NAME || true
#Pull and start the DB docker image
docker login -u $DOCKER_HUB_USERNAME -p $DOCKER_HUB_PASSWORD;
docker pull $DOCKER_HUB_USERNAME/$DOCKER_HUB_DB_NAME:$GOBII_RELEASE_VERSION;
docker run -i --detach --name $DOCKER_DB_NAME -e "gobiiuid=$GOBII_UID" -e "gobiigid=$GOBII_GID" -e "gobiiuserpassword=${DOCKER_GOBII_ADMIN_PASSWORD}"  -v ${BUNDLE_PARENT_PATH}:/data -v gobiipostgresetcubuntu:/etc/postgresql -v gobiipostgreslogubuntu:/var/log/postgresql -v gobiipostgreslibubuntu:/var/lib/postgresql -p $DOCKER_DB_PORT:5432 $DOCKER_HUB_USERNAME/$DOCKER_HUB_DB_NAME:$GOBII_RELEASE_VERSION;
docker start $DOCKER_DB_NAME;

#set the proper UID and GID and chown the hell out of everything (within the docker, of course)
echo "Matching the docker gadm account to that of the host and changing file ownerships..."
DOCKER_CMD="usermod -u $GOBII_UID gadm;";
eval docker exec $DOCKER_DB_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="groupmod -g $GOBII_GID gobii;";
eval docker exec $DOCKER_DB_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="find / -user 1000 -exec chown -h $GOBII_UID {} \; || :";
eval docker exec $DOCKER_DB_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="find / -group 1001 -exec chgrp -h $GOBII_GID {} \; || :";
eval docker exec $DOCKER_DB_NAME bash -c \"${DOCKER_CMD}\";

#clear the target directory of any old gobii_bundle
echo "Copying the GOBII_BUNDLE to the shared directory/volume..."
DOCKER_CMD="rm -rf /data/$DOCKER_BUNDLE_NAME";
eval docker exec $DOCKER_DB_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="cd /var; cp -R $DOCKER_BUNDLE_NAME /data/$DOCKER_BUNDLE_NAME";
eval docker exec $DOCKER_DB_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="chown -R gadm:gobii /data/$DOCKER_BUNDLE_NAME";
eval docker exec $DOCKER_DB_NAME bash -c \"${DOCKER_CMD}\";
