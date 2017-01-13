#!/usr/bin/perl

#Usuage: transpose.pl <input_file_delimiter> <input_file>
#delimter:
#   tab for tab
#   , for comma
#output : tsv file named outFile
#
#
#Required packages : Text::CSV, Text::CSV_XS (in absence of this package Text::CSV will use Text::CSV_PP which is slow compared to Text::CSV_XS) and File::Temp.
#These can be installed by cpan command.


use strict;
use Text::CSV;
use File::Temp qw(tempfile tempdir);

my $sep; #input delimiter 
if($ARGV[0] eq "tab") {$sep = "\t";}
else {$sep = $ARGV[0];}

my $file = $ARGV[1]; #input file
my $line = `wc -l < $file`; #total number of line is file
my $dir = "tmp_dir"; mkdir $dir; #temp directory
my $outfile = "outfile"; #name of output file

open (F1, $file) or die "$file: $!";  #open input file

 
my $csv = Text::CSV->new({binary => 1, sep_char => $sep}) 
    or die "Cannot use CSV: ".Text::CSV->error_diag ();

my @table; my @tmp_file; my $fh; my $filename;
my $col = 0; my $ll =0;


#reading file line by line
while(<F1>) 
{

    if(not $csv->parse($_)) {  #checing format 
	die "Unexpected format";
    }

    my $row = 0;
    
    #transposion
    for($csv->fields()) {
	$table[$row][$col] = defined($_) ? $_ : '';
	$row++;
    }
    $col++; $ll++;
    #if read lines equal to 10000 or total no. of line
    #print transposed rows in temp file
    if($col == 10000 || $ll == $line){
        ($fh, $filename) = tempfile(UNLINK => 0, DIR => $dir); #temp file
        binmode( $fh, ":utf8" );    #forcing utf8. precautionary
        print $fh map { join("\t",@$_),"\n" } @table; #print in temp file
        push @tmp_file, $filename; #keeping track of tempfiles
        @table = (); $row=$col=0;
        #merging multiple tempfiles file their no. increases 50 or if complete file is read
        if($#tmp_file == 50 || $ll == $line)
        {
            if(-f $outfile)
            {
                unshift @tmp_file, $outfile;
                `paste @tmp_file > tmpFile`;
                unlink(@tmp_file); rename "tmpFile", $outfile; @tmp_file=();
            }
            else
            {
                `paste @tmp_file > $outfile`;
                unlink(@tmp_file); @tmp_file=();
            }

        }
    }
}
close(F1);
rmdir $dir; 
 


