#!/usr/bin/env bash
#WARNING: Do not run this script as SUDO! There are explicit sudo commands which will prompt you for password. But not everything should run as sudo.
#usage: bash.sh <path-of-parms-file> <dockerhubpassw> <gobii_release_version>
#This a stand-alone equivalent of my THE_GOBII_SHIP Bamboo plan for the WEB NODE
#Requirements:
#1. The user that will run this script needs to be a sudoer and under the gobii and docker groups. So preferably the user 'gadm'.
#2. The working directory needs to be where the gobiiconfig_wrapper.sh is as well, typically <gobii_bundle>/conf/
#NOTE: The order of execution is important.
#NOTE: If weird things start happening on your dockers, try removing the images as well by running 'docker rmi' on each of the 3 nodes.
# If you want a delete-all-images command, run this: sudo docker stop $(sudo docker ps -qa) || true && sudo docker rm $(sudo docker ps -aq) || true && sudo docker rmi $(sudo docker images -aq) || true
#@author: (palace) kdp44@cornell.edu

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

#--------------------------------------------------#
### COMPUTE NODE ###
#--------------------------------------------------#
echo "Installing the COMPUTE node..."
#Stop and remove COMPUTE docker container
docker stop $DOCKER_COMPUTE_NAME || true && docker rm $DOCKER_COMPUTE_NAME || true
#Pull and start the COMPUTE docker image
docker pull $DOCKER_HUB_USERNAME/$DOCKER_HUB_COMPUTE_NAME:$GOBII_RELEASE_VERSION;
docker run -i --detach --name $DOCKER_COMPUTE_NAME  -v $BUNDLE_PARENT_PATH:/data -p $DOCKER_COMPUTE_SSH_PORT:22 $DOCKER_HUB_USERNAME/$DOCKER_HUB_COMPUTE_NAME:$GOBII_RELEASE_VERSION;
docker start $DOCKER_COMPUTE_NAME;

#set the proper UID and GID and chown the hell out of everything (within the docker, of course)
echo "Matching the docker gadm account to that of the host's and changing file ownerships..."
DOCKER_CMD="usermod -u $GOBII_UID gadm;";
eval docker exec $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="groupmod -g $GOBII_GID gobii;";
eval docker exec $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="find / -user 1000 -exec chown -h $GOBII_UID {} \; || :";
eval docker exec $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="find / -group 1001 -exec chgrp -h $GOBII_GID {} \; || :";
eval docker exec $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";


#Grant permissions and set cronjobs
echo "Granting permissions..."
DOCKER_CMD="chmod -R +rx /data/$DOCKER_BUNDLE_NAME/loaders/;";
eval docker exec $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="chmod -R +rx /data/$DOCKER_BUNDLE_NAME/extractors/;";
eval docker exec $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="chmod -R g+rwx /data/$DOCKER_BUNDLE_NAME/crops/*/files;";
eval docker exec $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";


#Sets the cron jobs for 2 crops, if there is no 2nd crop, no error will be thrown. This allows the script to be useful for CGs with 2 crops too, without any modifications.
echo "Setting cron jobs..."
DOCKER_CMD="cd /data/$DOCKER_BUNDLE_NAME/loaders/etc; crontab -r;";
eval docker exec --user gadm $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="cd /data/$DOCKER_BUNDLE_NAME/loaders/etc; sh addCron.sh /data/$DOCKER_BUNDLE_NAME $DOCKER_CROP1_NAME $DOCKER_CRON_INTERVAL $DOCKER_CRON_FILE_AGE;";
eval docker exec --user gadm $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="cd /data/$DOCKER_BUNDLE_NAME/loaders/etc; sh addCron.sh /data/$DOCKER_BUNDLE_NAME $DOCKER_CROP2_NAME $DOCKER_CRON_INTERVAL $DOCKER_CRON_FILE_AGE || true;";
eval docker exec --user gadm $DOCKER_COMPUTE_NAME bash -c \"${DOCKER_CMD}\";
