#!/bin/nawk -f
#Remove <tab>#/# or <tab>#|#
{
    gsub("\t\\+\t","\t\.\t");

    print;
}
