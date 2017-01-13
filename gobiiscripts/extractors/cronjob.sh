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

relativeroot=`dirname $0`"/.."
root=`realpath $relativeroot`"/"

#Old way
#root="/shared_data/gobii/"

crop=$1
#Moves files from /instructions to /inprogress then calls Extractor on them.
time="+2"
for entry in `ls $root"crops/"$crop"/extractor/instructions"`
do
	extractorname=$root"crops/"$crop"/extractor"
	mv $extractorname"/instructions/"$entry $extractorname"/inprogress/"$entry
	java -jar $root"core/Extractor.jar" -r $root $extractorname"/inprogress/"$entry
done

