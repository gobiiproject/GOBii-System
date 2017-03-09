#!/bin/bash

cronloadpath="/loaders/cronjob.sh"
cronextractpath="/extractors/cronjob.sh"
#Test if required argument exists
if [ -z "$1" ]
  then
    echo "Usage: addCron.sh <rootdir> <cropname> [<time>] [<fileAge>]"
    echo "Adds 2 cronjobs, one for extractor and one for digester"
    echo "to the cron of the user executing this script."
    echo "Only execute this as the CRON JOB'S INTENDED USER"
    exit
fi

if [ -z "$2" ]
  then
    echo "Usage: addCron.sh <rootdir> <cropname> [<time>]"
    echo "Adds 2 cronjobs, one for extractor and one for digester"
    echo "to the cron of the user executing this script."
    echo "Only execute this as the CRON JOB'S INTENDED USER"
    exit
fi

if [ -z "$3" ]
    then
    time="5"
else
    time=$3
fi

if [ -z "$4" ]
    then
    fileAge="+5"
else
    fileAge=$4
fi

crontab -l > tmpcron
echo "*/"$time" * * * * "$1$cronloadpath" "$2 $fileAge >> tmpcron
echo "*/"$time" * * * * "$1$cronextractpath" "$2 $fileAge >> tmpcron
crontab tmpcron
rm tmpcron
