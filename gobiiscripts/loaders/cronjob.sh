#!/bin/bash
#Test if required argument exists
if [ -z "$1" ]
  then
    echo "Usage: cronjob.sh <cropname>"
    echo "Cropname: name of the crop for purposes of where in the directory"
    echo "structure the job will look to find instructions. Example:"
    echo "> ./cronjob.sh maize"
    exit
fi

#new way
relativeroot=`dirname $0`"/.."
root=`realpath $relativeroot`"/"

#old way
#root="/shared_data/gobii/"

crop=$1
#Moves files from /instructions to /inprogress then calls Digester on them.
time="+5"
for entry in `ls $root"crops/"$crop"/loader/instructions"`
do
    if test `find $root"crops/"$crop"/loader/instructions/"$entry -mmin $time`
    then
	loadername=$root"crops/"$crop"/loader"
	mv $loadername"/instructions/"$entry $loadername"/inprogress/"$entry
	java -jar $root"core/Digester.jar" -r $root $loadername"/inprogress/"$entry
    fi
done

