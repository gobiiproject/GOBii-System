#!/bin/bash
#Test if required argument exists
if [ -z "$1" ]
  then
    echo "Usage: cronjob.sh <cropname> <time>"
    echo "Cropname: name of the crop for purposes of where in the directory"
    echo "time: find 'time' parameter. "
    echo "structure the job will look to find instructions. Example:"
    echo "> ./cronjob.sh maize"
    echo "> ./cronjob.sh wheat +2"
    exit
fi

relativeroot=`dirname $0`"/.."
root=`realpath $relativeroot`"/"

#Time
if [ -z $2 ]
then
  time="+5"
else
  time=$2
fi

crop=$1
#Moves files from /instructions to /inprogress then calls Digester on them.
for entry in `ls $root"crops/"$crop"/loader/instructions"`
do
    if test `find $root"crops/"$crop"/loader/instructions/"$entry -mmin $time`
    then
	loadername=$root"crops/"$crop"/loader"
	mv $loadername"/instructions/"$entry $loadername"/inprogress/"$entry
	java -jar $root"core/Digester.jar" -r $root $loadername"/inprogress/"$entry
    fi
done

