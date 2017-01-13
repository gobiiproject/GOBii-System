#!/bin/nawk -f
#First four characters are outliers
{
   # gsub("(A|C|G|T)/\1","\1");
    gsub("A/A","A");
    gsub("A/N","A");
    gsub("N/A","A");
    gsub("A/\\.","A");
    gsub("\\./A","A");
    gsub("A/\\+","A");
    gsub("\\+/A","A");
    gsub("A/0","A");
    gsub("0/A","A");


    gsub("C/C","C");
    gsub("C/N","C");
    gsub("N/C","C");
    gsub("C/\\.","C");
    gsub("\\./C","C");
    gsub("C/\\+","C");
    gsub("\\+/C","C");
    gsub("C/0","C");
    gsub("0/C","C");



    gsub("G/G","G");
    gsub("G/N","G");
    gsub("N/G","G");
    gsub("G/\\.","G");
    gsub("G/\\+","G");
    gsub("\\+/G","G");
    gsub("G/0","G");
    gsub("0/G","G");


    gsub("\\./T","T");
    gsub("T/T","T");
    gsub("T/N","T");
    gsub("N/T","T");
    gsub("T/\\.","T");
    gsub("\\./T","T");
    gsub("T/\\+","T");
    gsub("\\+/T","T");
    gsub("T/0","T");
    gsub("0/T","T");


    gsub("A/G","R");
    gsub("G/A","R");

    gsub("C/T","Y");
    gsub("T/C","Y");

    gsub("G/C","S");
    gsub("C/G","S");

    gsub("A/T","W");
    gsub("T/A","W");

    gsub("T/G","K");
    gsub("G/T","K");


    gsub("A/C","M");
    gsub("C/A","M");


    gsub("N/N","N");
    gsub("N/\\.","N");
    gsub("\\./N","N");

    gsub("\\./\\.",".");

    gsub("\\+/\\.",".");
    gsub("\\./\\+",".");

    gsub("N/\\+","N");
    gsub("\\+/N","N");

    gsub("\\+/\\+","+");

    gsub("\\+/\\.","0");
    gsub("\\./\\+","0");

    gsub("N/\\+","N");
    gsub("\\+/N","N");

    gsub("0/0","0");





    print;
}
