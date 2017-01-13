#!/usr/bin/perl

#Usuage: vcf_2_bi_v1.pl.pl <input_vcf_file> <output_file>
#
##Description : Converts vcf to bi-allelic/diploid format variant matrix (without separator)
#Note : This version replace INDELs with NN
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
open my $vcf, "<:encoding(utf8)", $file or die "$file: $!";  #open input file

 
my $csv = Text::CSV->new({binary => 1, sep_char => "\t"}) 
    or die "Cannot use CSV: ".Text::CSV->error_diag ();


my @table; my @tmp_file; my $fh; my $filename;
my $row = 0; my $ll =0; 

mkdir $dir; #creating a temp directory

while(<$vcf>) 
{
    $ll++;
    
    next unless $_!~/^#/; #skip meta-information and header line
     
    if(not $csv->parse($_)){  #checking format 
	die "Unexpected format : $!";
    }
    
    
    my @fields = $csv->fields();
    #my $ref = $fields[3];
    my @alleles = split /\,/, $fields[4];
    unshift @alleles, $fields[3];
    my %biAllele = ();  %biAllele = convert(@alleles); 
    
    splice  @fields, 0, 9;
    
    my $col = 0; my $var; my $variant;
    
    for(@fields) {
        $var = (split /\:/, $_)[0];
        $var =~ s/\/|\|//g;
        $variant = $biAllele{$var};
        
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

    my @allele = @_;
    my $ref = shift @allele;
    my $flag = 0; my $i; my $e;
    my %v = ();

    if(length($ref) == 1)
    {
        $i=1; 
        foreach my $alt (@allele)
        {
            if(length($alt) == 1)
            {
            $v{'00'} = $ref.$ref;
            $e = "0".$i; $v{$e} = $ref.$alt;
            $e = $i.$i; $v{$e} = $alt.$alt;
            $flag = 1; $i++;
            }
            else
            {
                $e = "0".$i; $v{$e} = "NN";
                $e = $i.$i; $v{$e} = "NN";
                $v{'00'} = "NN" unless ($flag == 1);
                $i++;
            }
        }    
    }
    else
    {
        $i=1; 
        for(@allele)
        {
            $v{'00'} = "NN";
            $e = "0".$i; $v{$e} = "NN";
            $e = $i.$i; $v{$e} = "NN";
            $i++;
        }
        
    }
    $v{'..'} = "NN";

    return %v;

}


