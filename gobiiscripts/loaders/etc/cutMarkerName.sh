#!/bin/bash

#Test if required argument exists
if [ -z "$1" ]
  then
    echo "Usage: cutMarkerName.sh <tpedfile> <markerfile>"
    exit
fi

if [ -z "$2" ]
  then
    echo "Usage: cutMarkerName.sh <tpedfile> <markerfile>"
    exit
fi

cut -f2 -d' ' $1 > $2
