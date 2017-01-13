#!/usr/bin/perl -w
use strict;

# 2016.05.25 vulat
# Converts DArT 2 row format into 1 row
# If input is "," separated, it will
#   convert to tab delimitted.

# DArT convention:
# REF   1      1        0       -
# ALT   1      0        1       -
# DEF  Het  Hom(REF) Hom(ALT) Missng

if (@ARGV<2){
  print "\n\tUsage: $0 <DArT 2 Rows> <Output> ".
        "<Data Start>\n\n";
  print "\t i.e.: $0 DArT-2R.csv DArT-1R.csv 20\n\n";
  print "\tData Start = column where genotype data ".
        "starts, if no Data". 
        " Start given, it assumes \n\t the full DArT ".
	"output, 32.\n\n";
  exit();
}

open (FILE, $ARGV[0]) or 
     die "Cannot open $ARGV[0]: $!";
open (OUTPUT, ">$ARGV[1]") or 
     die "Cannot open $ARGV[1]: $!";

my $startCol=$ARGV[3]||'32';
my $fndDlmtr=0;
my $spltr="\t";

while (<FILE>){
  my $row1=$_;
  my $row2=<FILE>;
  my $dlmtr="\t";


  if ($fndDlmtr==0){
    $fndDlmtr=()=$row1=~/\,/gi;
    if ($fndDlmtr>20){
      $spltr="\,";
    }
  }

  if ($row1=~/^\*/){
    $row1=~s/$spltr/$dlmtr/g;
    $row2=~s/$spltr/$dlmtr/g;
    print OUTPUT $row1;
    print OUTPUT $row2;
  } else {
    chomp $row1;
    chomp $row2;
    my @row1=split(/$spltr/, $row1);
    my @row2=split(/$spltr/, $row2);
    my $colNum=scalar @row1;
    print OUTPUT $row2[0], $dlmtr;
    my $i=1;
    my $refRes='';
    my $altRes='';
    while ($i<$startCol){
      if ($i==7){
        my($refName,$altName)=split(/\-/, $row2[0]);
	$altName=~s/.+\://g;
	($refRes, $altRes)=split(/\>/, $altName);
        print OUTPUT $altName, $dlmtr;
      } else {
        print OUTPUT $row1[$i], $dlmtr;
      }
      $i++;
    }
    while ($i<$colNum){
      if ($i==($colNum-1)){
        $dlmtr='';
      }
      if (($row1[$i] eq $row2[$i])){
         if ($row1[$i] eq '-'){
           print OUTPUT "-", $dlmtr;
	 }
	 if ($row1[$i] eq '0'){
           print  "Error -- 00\n";
	   exit();
         }
         if ($row1[$i] eq '1'){
           print OUTPUT "2", $dlmtr;
         }
      } else {
         if ($row1[$i] eq "1") {
           if ($row2[$i] eq "0"){
	     # Homozygouz REF;
             print OUTPUT "0", $dlmtr;
           }
           if ($row2[$i] eq "-"){
             print "Error -- 1-\n";
	     exit();
           }
         } else {
           if ($row2[$i] eq "1"){
             if ($row1[$i] eq "0"){
	       # Homozygouz ALT;
               print OUTPUT "1", $dlmtr;
             }
             if ($row1[$i] eq "-"){
               print "Error -- -1\n";
               exit();
             }
           }
         }
      }
      $i++;
    }
    print OUTPUT "\n";
  } 
} 
