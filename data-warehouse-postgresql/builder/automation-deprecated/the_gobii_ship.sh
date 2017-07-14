#!/usr/bin/env bash
#WARNING: Do not run this script as SUDO! There are explicit sudo commands which will prompt you for password. But not everything should run as sudo.
#usage: bash.sh <path-of-parms-file> <dockerhubpassw> <gobii_release_version>
#This a stand-alone equivalent of my THE_GOBII_SHIP Bamboo plan
#Requirements:
#1. The user that will run this script needs to be a sudoer and under the gobii and docker groups. So preferably the user 'gadm'.
#2. The working directory needs to be where the gobiiconfig_wrapper.sh is as well, typically <gobii_bundle>/conf/
#3. Run this on a server that has access on all 3 nodes, if this is not possible, break up the script into the 3 main nodes and run individually
#NOTE: In case you need to break up this script in 3 nodes, you may need to restart Tomcat again at the end of the installation process.
#NOTE2: The order of execution is important.
#NOTE3: If weird things start happening on your dockers, try removing the images as well by running 'docker rmi' on each of the 3 nodes.
# If you want a delete-all-images command, run this: sudo docker stop $(sudo docker ps -qa) || true && sudo docker rm $(sudo docker ps -aq) || true && sudo docker rmi $(sudo docker images -aq) || true || docker volume rm $(docker volume list -q) || true
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

#--------------------------------------------------#
### ANY NODE ###
#--------------------------------------------------#
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

echo "Copying the param file and the config wrapper first..."
docker cp $CONFIGURATOR_PARAM_FILE $DOCKER_WEB_NAME:/data/$DOCKER_BUNDLE_NAME/config/$CONFIGURATOR_PARAM_FILE;
docker cp gobiiconfig_wrapper.sh $DOCKER_WEB_NAME:/data/$DOCKER_BUNDLE_NAME/config/gobiiconfig_wrapper.sh;

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
#TODO: COPY THE PARAM FILE PASSED TO THIS DIRECTORY FIRST!
DOCKER_CMD="cd $DOCKER_BUNDLE_NAME/config; bash gobiiconfig_wrapper.sh $CONFIGURATOR_PARAM_FILE;";
eval docker exec $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";

echo "Restarting tomcat under user gadm..."
#Restart tomcat with the proper ownership
#Stop tomcat and start with the gadm user
DOCKER_CMD="cd /usr/local/tomcat/bin/; sh shutdown.sh;";
eval docker exec $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";

DOCKER_CMD="cd /usr/local/tomcat/bin/; sh startup.sh;";
eval docker exec --user gadm $DOCKER_WEB_NAME bash -c \"${DOCKER_CMD}\";

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

