#!/usr/bin/perl
# 2016.06.10 vulat: run this script against dos2unix to
#                   convert the line endings to unix

#Usuage: hmp_2_bi.pl.pl <input_hampmap_file> <output_file>
#
##Description : Converts hapmap to bi-allelic/diploid variant matrix (without separator)
#e.g. A => AA
#     G => GG  etc
#
#Required packages : Text::CSV, Text::CSV_XS (in absence of Text::CSV_XS package Text::CSV will use Text::CSV_PP which is slow compared to Text::CSV_XS) and File::Temp.
#These can be installed by cpan command.

use strict;
use Text::CSV;
use File::Temp qw(tempfile tempdir);

my $file = $ARGV[0];    #input file
my $line = `wc -l < $file`;   #total number of line is file
my $dir = "tmp_dir"; 
my $outfile = $ARGV[1];   #name of output file

#check if file with same name as output file exist
if(-f $outfile)
{die "$outfile file already exist: $!"; } 
    

#open ($fl, $file) or die "$file: $!";  #open input file
open my $hmp, "<:encoding(utf8)", $file or die "$file: $!";  #open input file

 
my $csv = Text::CSV->new({binary => 1, sep_char => "\t"}) 
    or die "Cannot use CSV: ".Text::CSV->error_diag ();


my @table; my @tmp_file; my $fh; my $filename;
my $row = 0; my $ll =0; 

mkdir $dir; #creating a temp directory

while(<$hmp>) 
{
    $ll++;
    
    next unless ($ll > 1); #skip header line
     
    if(not $csv->parse($_)){  #checking format 
	die "Unexpected format : $!";
    }
    
    
    my @fields = $csv->fields();
    my $ref = (split /\//, $fields[1])[0];
    #my @alleles = split /\,/, $al[1];
    #unshift @alleles, $al[0];
    my %biAllele = ();  %biAllele = convert($ref); 
    
    splice  @fields, 0, 11;
    
    my $col = 0; my $variant;
    
    for(@fields) {
        #$var = (split /\:/, $_)[0];
        #$var =~ s/\/|\|//g;
        $variant = $biAllele{$_};
        
	$table[$row][$col] = defined($variant) ? $variant : "NN";
	$col++;
    }
    $row++;
    
    if($row == 10000 || $ll >= $line){
    ($fh, $filename) = tempfile(UNLINK => 0, DIR => $dir); #temp file
        binmode( $fh, ":utf8" );    #forcing utf8. precautionary
        print $fh map { join("\t",@$_),"\n" } @table; #print in temp file
        push @tmp_file, $filename; #keeping track of tempfiles
        @table = (); $row=$col=0;
        #merging multiple tempfiles file no. increases 50 or if complete file is read
        if($#tmp_file == 49 || $ll >= $line)
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

    my $ref = $_[0];
    my %data = ();

    if(length($ref) == 1)
    {
        if($ref eq "A")
        {
            %data = ('A' => "AA", 
                'T' => "TT",
                'G' => "GG",
                'C' => "CC",
                'M' => "AC",
                'R' => "AG",
                'W' => "AT");
        }
        elsif($ref eq "T")
        {
            %data = ('A' => "AA", 
                'T' => "TT",
                'G' => "GG",
                'C' => "CC",
                'K' => "TG",
                'W' => "TA",
                'Y' => "TC");
        }
        elsif($ref eq "C")
        {
            %data = ('A' => "AA", 
                'T' => "TT",
                'G' => "GG",
                'C' => "CC",
                'M' => "CA",
                'S' => "CG",
                'Y' => "CT");
        }
        elsif($ref eq "G")
        {
            %data = ('A' => "AA", 
                'T' => "TT",
                'G' => "GG",
                'C' => "CC",
                'K' => "GT",
                'R' => "GA",
                'S' => "GC");
        }
        else
        {die "Unknown Ref Allele $ref: $!";}
        
    }
    else
    {
        die "Unknown Ref Allele $ref: $!";
    }
    $data{'.'} = $data{'-'} = $data{'N'} = "NN";

    return %data;

}

