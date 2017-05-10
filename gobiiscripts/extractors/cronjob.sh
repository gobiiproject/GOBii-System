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

#Old way
#root="/shared_data/gobii/"

crop=$1
#Moves files from /instructions to /inprogress then calls Extractor on them.
if [ -z $2 ]
then
  time="+2"
else
  time=$2
fi

for entry in `ls $root"crops/"$crop"/extractor/instructions"`
do
	if [[ $entry = *.json ]]
	then
	  extractorname=$root"crops/"$crop"/extractor"
	  mv $extractorname"/instructions/"$entry $extractorname"/inprogress/"$entry
	  java -jar $root"core/Extractor.jar" -r $root $extractorname"/inprogress/"$entry
	fi
done

