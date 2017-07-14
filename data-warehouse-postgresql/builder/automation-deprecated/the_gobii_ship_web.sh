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
### WEB NODE ###
#--------------------------------------------------#
echo "Installing the WEB node..."
#Stop and remove the web docker container if it exists -- this will not throw an error if the dockers are not there, to enable it to work on fresh installs
docker stop $DOCKER_WEB_NAME || true && docker rm $DOCKER_WEB_NAME || true
#Pull and start the WEB docker image
docker pull $DOCKER_HUB_USERNAME/$DOCKER_HUB_WEB_NAME:$GOBII_RELEASE_VERSION;
docker run -i --detach --name $DOCKER_WEB_NAME  -v $BUNDLE_PARENT_PATH:/data -p $DOCKER_WEB_PORT:8080 $DOCKER_HUB_USERNAME/$DOCKER_HUB_WEB_NAME:$GOBII_RELEASE_VERSION;
docker start $DOCKER_WEB_NAME;

#set the proper UID and GID and chown the hell out of everything (within the docker, of course)
echo "Matching the docker gadm account to that of the host's and changing file ownerships..."
DOCKER_CMD="usermod -u $GOBII_UID gadm;";
eval docker exec $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="groupmod -g $GOBII_GID gobii;";
eval docker exec $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="find / -user 1000 -exec chown -h $GOBII_UID {} \; || :";
eval docker exec $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";
DOCKER_CMD="find / -group 1001 -exec chgrp -h $GOBII_GID {} \; || :";
eval docker exec $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";

echo "Updating gobii-web.xml..."
#Update the gobii-web.xml file with installation params. The (not-so) fun part.
DOCKER_CMD="cd $DOCKER_BUNDLE_NAME/config; bash gobiiconfig_wrapper.sh $CONFIGURATOR_PARAM_FILE;";
eval docker exec $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";

echo "Restarting tomcat under user gadm..."
#Restart tomcat with the proper ownership
#Stop tomcat and start with the gadm user
DOCKER_CMD="cd /usr/local/tomcat/bin/; sh shutdown.sh;";
eval docker exec $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";

DOCKER_CMD="cd /usr/local/tomcat/bin/; sh startup.sh;";
eval docker exec --user gadm $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";