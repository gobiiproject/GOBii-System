#!/usr/bin/perl

#Usuage: IUPACmatric_to_bo.pl <input_file_delimiter> <input_file> <output_file>
#delimter:
#   tab for tab
#   , for comma
#
##Description : Converts variant matrix (without header or marker IDs) in IUPAC to bi-allelic/diploid format (without separator)
#e.g. A => AA
#     G => GG  etc
#
#Required packages : Text::CSV, Text::CSV_XS (in absence of Text::CSV_XS package Text::CSV will use Text::CSV_PP which is slow compared to Text::CSV_XS) and File::Temp.
#These can be installed by cpan command.

use strict;
use Text::CSV;
use File::Temp qw(tempfile tempdir);

#input delimiter 
my $sep;
if($ARGV[0] eq "tab") {$sep = "\t";}
else {$sep = $ARGV[0];}


my $file = $ARGV[1];    #input file
my $line = `wc -l < $file`;   #total number of line is file
my $dir = "tmp_dir"; 
my $outfile = $ARGV[2];   #name of output file

#check if file with same name as output file exist
if(-f $outfile)
{die "$outfile file already exist: $!"; } 
    
open (F1, $file) or die "$file: $!";  #open input file

 
my $csv = Text::CSV->new({binary => 1, sep_char => $sep}) 
    or die "Cannot use CSV: ".Text::CSV->error_diag ();

my @table; my @tmp_file; my $fh; my $filename;
my $row = 0; my $ll =0; 

mkdir $dir; #creating a temp directory

while(<F1>) 
{

    if(not $csv->parse($_)) {  #checing format 
	die "Unexpected format : $!";
    }

    my $col = 0; my $al;
    for($csv->fields()) {
        $al = convert($_); 
	$table[$row][$col] = defined($al) ? $al : '';
	$col++;
    }
    $row++; $ll++;
    #if read lines equal to 10000 or total no. of line
    #print rows in temp file
    if($row == 10000 || $ll == $line){
    ($fh, $filename) = tempfile(UNLINK => 0, DIR => $dir); #temp file
        binmode( $fh, ":utf8" );    #forcing utf8. precautionary
        print $fh map { join("\t",@$_),"\n" } @table; #print in temp file
        push @tmp_file, $filename; #keeping track of tempfiles
        @table = (); $row=$col=0;
        #merging multiple tempfiles file no. increases 50 or if complete file is read
        if($#tmp_file == 49 || $ll == $line)
        {
            if(-f $outfile)
            {
                unshift @tmp_file, $outfile;
                `cat @tmp_file > tmpFile`;
                unlink(@tmp_file); rename "tmpFile", $outfile; 
                @tmp_file=();
            }
            else
            {
                `cat @tmp_file > $outfile`;
                unlink(@tmp_file); @tmp_file=();
            }

        }
    }
}
close(F1);
rmdir $dir; 
    

 sub convert{
 
 my $allele = $_[0];
 my %data = ('A' => "AA", 
        'T' => "TT",
        'G' => "GG",
        'C' => "CC",
        'M' => "AC",
        'R' => "AG",
        'W' => "AT",
        'S' => "CG",
        'Y' => "CT",
        'K' => "GT",
        'N' => "NN");
        
 return $data{$allele};
 
 }
    
