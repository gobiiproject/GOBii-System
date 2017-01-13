#!/usr/bin/perl

#Usuage: parse_hmp.pl <instruction_file_JSON>

#Required packages : JSON::XS, Switch. These can be installed by cpan command.


use strict;
use JSON::XS;
use Switch;


my $inst_file = $ARGV[0]; #storing JSON instruction file in variable

#reading JSON instruction file
my $json_text = do {
   open(my $json_fh, "<:encoding(UTF-8)", $inst_file) or die("Can't open $inst_file: $!");
   local $/;
   <$json_fh>
};

#convert JSON instruction file into perl data str
my $instruction = decode_json($json_text);

my @inst = @{ $instruction }; 
my @files;
foreach my $i (@inst)
{
    my @files=();
    my $source = $i->{gobiiFile}->{source};
    #print $source."\n";
    if(-d $source) {@files = glob "$source/*.hmp.txt";}
    elsif(-f $source){@files = glob $source;}
    else{die("source does not exit: $!");}

    my $dest = $i->{gobiiFile}->{destination};
    if(!-d "$dest"){die("destination dir does not exit: $!");}
    
    my $del = $i->{gobiiFile}->{delimiter};
    if($del eq "\\t" || $del eq "\t"){$del = "\t";}
    
    my $fileType = $i->{gobiiFile}->{gobiiFileType};
    if($fileType ne "HAPMAP"){die("Wrong gobiiFileType: $!");}
    
    my $table = $i->{table};
    my $outfile = $dest."/table\.".$table;
    
    my @columns = @{ $i->{gobiiFileColumns} };
    my $tmp_out = $dest."/tempFile_out.tmp";
    my $fileCount = 1;
    foreach my $file (@files)
    {
        my $tmp1 = $dest."/tempFile_1.tmp"; 
        my $tmp2 = $dest."/tempFile_2.tmp";my $tmp3 = $dest."/tempFile_3.tmp";
        my $del_out = "\t";
        my $conFlag = 0; my $const = undef; my $colCount = 0; my $conHead = undef;
        
        foreach my $key (@columns)
        {
            my $cc; my $rc; my $out; 
            my $header = $key->{name};
            #my $find = $key->{findText}; 
            #my $replace = $key->{replaceText};
            
            
                        
            switch($key->{gobiiColumnType})
            {
                case "CSV_COLUMN"
                {
                    my $out;
                    if($key->{cCoord} ne undef){$cc = ($key->{cCoord} + 1);} 
                    else{die("Column coordinate cannot be null: $!");}
                    if($key->{rCoord} ne undef){$rc = ($key->{rCoord} + 1);}
                    else{die("Row coordinate cannot be null: $!");}
                    
                    if($key->{subcolumn})
                    { $del_out = $key->{subcolumnDelimiter}; }
                    
                    my $cmd1 = "awk -F\'$del\' \'{if(NR>=$rc) print \$$cc}\' $file > $tmp1";  #awk cmd for extracting the column
                    my $c1 = `$cmd1`; if(${^CHILD_ERROR_NATIVE} != 0){die("${^CHILD_ERROR_NATIVE}\n");} #in case of non zero exit
                    
                    if($key->{filterFrom} ne undef )
                    {
                        my $from = $key->{filterFrom};
                        filtFrom($tmp1,$tmp2,$from);
                    }
                    else 
                    { rename $tmp1, $tmp2; }
                    
                    if($key->{filterTo} ne undef )  #if(exists $key->{filterTo} )
                    {
                        my $to = $key->{filterTo}; 
                        filtTo($tmp2,$tmp1,$to);                
                    }
                    
                    if($key->{findText} ne undef && $key->{replaceText} ne undef)
                    {
                        my $find = $key->{findText}; 
                        my $replace = $key->{replaceText};
                        findReplace($tmp2,$tmp1,$find,$replace);  
                    }
                    if($fileCount == 1){
                    `sed -i \'1i$header\' $tmp2`;} #adding header
                    
                    #saving the extracted column in file. Checks if file already exists
                    if(-f $tmp_out){$out="-d\'$del_out\' $tmp3 $tmp2 > $tmp_out; cat $tmp_out > $tmp3\;";}
                    else{$out="-d\'$del_out\' $tmp2 > $tmp_out\; cat $tmp_out > $tmp3\; ";}
                    my $cmd2 = "paste ".$out;
                    my $c=`$cmd2`;
                    if(${^CHILD_ERROR_NATIVE} != 0){die("${^CHILD_ERROR_NATIVE}\n");} #check for non zero exit
                    
                }
                case "CSV_ROW"
                {
                    my $out;
                    if($key->{cCoord} ne undef){$cc = ($key->{cCoord} + 1);} 
                    else{die("Column coordinate cannot be null: $!");}
                    if($key->{rCoord} ne undef){$rc = ($key->{rCoord} + 1);}
                    else{die("Row coordinate cannot be null: $!");}
                    
                    if($key->{subcolumn})
                    { $del_out = $key->{subcolumnDelimiter}; }
                    
                    my $cmd1 = "awk -F\'$del\' \'{if(NR==$rc) for(i=$cc; i<=NF; i++) {printf \"\%s\\t\", \$i}; printf \"\\n\" }\' $file | xargs -n1 > $tmp1";
                    my $c1 = `$cmd1`; 
                    if(${^CHILD_ERROR_NATIVE} != 0){die("${^CHILD_ERROR_NATIVE}\n");} #check for non zero exit
                    
                    if($key->{filterFrom} ne undef )
                    {
                        my $from = $key->{filterFrom};
                        filtFrom($tmp1,$tmp2,$from);
                    }
                    else 
                    { rename $tmp1, $tmp2; }
                    
                    if($key->{filterTo} ne undef )  #if(exists $key->{filterTo} )
                    {
                        my $to = $key->{filterTo}; 
                        filtTo($tmp2,$tmp1,$to);                
                    }
                    
                    if($key->{findText} ne undef && $key->{replaceText} ne undef)
                    {
                        my $find = $key->{findText}; 
                        my $replace = $key->{replaceText};
                        findReplace($tmp2,$tmp1,$find,$replace);  
                    }
                    if($fileCount == 1){
                    `sed -i \'1i$header\' $tmp2`;} #adding header
                    
                    #saving the extracted column in file. Checks if file already exists
                    if(-f $tmp_out){$out="-d\'$del_out\' $tmp3 $tmp2 > $tmp_out; cat $tmp_out > $tmp3\;";}
                    else{$out="-d\'$del_out\' $tmp2 > $tmp_out\; cat $tmp_out > $tmp3\; ";}
                    my $cmd2 = "paste ".$out;
                    my $c=`$cmd2`;
                    if(${^CHILD_ERROR_NATIVE} != 0){die("${^CHILD_ERROR_NATIVE}\n");} #check for non zero exit
                }
                
                case "CSV_BOTH"
                {
                    my $out;
                    if($key->{cCoord} ne undef){$cc = ($key->{cCoord});} 
                    else{die("Column coordinate cannot be null: $!");}
                    if($key->{rCoord} ne undef){$rc = ($key->{rCoord});}
                    else{die("Row coordinate cannot be null: $!");}
                    #my $line_count = `wc -l < $file`; 
                    open(F1, $file); open(F2, "+>$tmp1");
                    my $row = 0;
                    while(<F1>)
                    {
                        chomp;
                        if($row >= $rc)
                        {
                            my @c2 = split "\t", $_;
                            if($cc > 0)
                            {splice @c2, 0, $cc;}
                            for(my $u = 0; $u <= $#c2; $u++)
                            {
                                if($key->{filterFrom} ne undef)
                                {
                                    my $from = $key->{filterFrom};
                                    my @ar = (split "$from", $c2[$u]); shift @ar; my $l = join("$from", @ar);
                                    $c2[$u] = $l;
                                }
                                if($key->{filterTo} ne undef)
                                {
                                    my $to = $key->{filterTo};
                                    my @ar = (split "$to", $c2[$u]); pop @ar; my $l = join("$to", @ar);
                                    $c2[$u] = $l;
                                }
                                if($key->{findText} ne undef && $key->{replaceText} ne undef)
                                {
                                    my $find = $key->{findText}; 
                                    my $replace = $key->{replaceText};
                                    $c2[$u] =~ s/$find/$replace/g;
                                }
                            }
                            my $cols = join "$del_out", @c2;
                            print F2 $cols."\n";
                        }
                        $row++;
                    }
                    if(-f $tmp_out){rename $tmp_out, $tmp2; `paste -d\'$del_out\' $tmp2 $tmp1 > $tmp_out`; unlink $tmp2,$tmp3;}
                    else{rename $tmp1, $tmp_out;}
                    
                }
                case "CONSTANT"
                {
                    $const=$key->{constantValue};
                    if($colCount == 0)
                    {$conFlag = 1; $conHead = $header}
                    else
                    {
                        if($fileCount == 1){
                        my $c = `awk \'{print \"$const\"}\' $tmp3 | sed \'1 s/\^\.\*\$\/$header\/g\' | paste -d\'$del_out\' $tmp3 - > $tmp_out; cat $tmp_out > $tmp3\;`;}
                        else
                        {my $c = `awk \'{print \"$const\"}\' $tmp3 | paste -d\'$del_out\' $tmp3 - > $tmp_out; cat $tmp_out > $tmp3\;`;}
                        if(${^CHILD_ERROR_NATIVE} != 0){die("${^CHILD_ERROR_NATIVE}\n");}
                    }
                }
            }
            $colCount++;
        }
        unlink $tmp3, $tmp2;
        if($conFlag)
        {
            if($fileCount == 1){
            my $c = `awk \'{print \"$const\"}\' $tmp_out | sed \'1 s/\^\.\*\$\/$conHead\/g\' | paste -d\'$del_out\' - $tmp_out > $tmp3;`;}
            else {my $c = `awk \'{print \"$const\"}\' $tmp_out | paste -d\'$del_out\' - $tmp_out > $tmp3;`;}
            if(${^CHILD_ERROR_NATIVE} != 0){nulldie("${^CHILD_ERROR_NATIVE}\n");}
            unlink $tmp_out;
            rename $tmp3, $tmp_out; unlink $tmp3;
        }
        
        if(-f $outfile){`cat $tmp_out >> $outfile`; unlink $tmp_out;}
        else{rename $tmp_out, $outfile;}
        $fileCount++;
    }

}

sub filtFrom{
    open(F1, $_[0]); open(F2, "+>$_[1]");
    my $reg = $_[2];
    while(my $l = <F1>)
    { 
        chomp($l); 
        if(index($l,$reg) != -1){
            my @arr = (split $reg, $l); shift @arr; my $line = join("$reg", @arr);
            print F2 $line,"\n";} 
        else
        {print F2 $l."\n";}
    }
    close F1; close F2; unlink $_[0];
}

sub filtTo{
    open(F1, $_[0]); open(F2, "+>$_[1]");
    my $reg = $_[2];
    while(my $l = <F1>)
    {   
        chomp($l); 
        if(index($l,$reg) != -1){
            my @arr = split /$reg/, $l; pop @arr; my $line = join("$reg", @arr);
            print F2 $line,"\n";}
        else
        {print F2 $l."\n";}
    }
    close F1; close F2; unlink $_[0]; rename $_[1], $_[0];
}

sub findReplace{

    open(F1, $_[0]); open(F2, "+>$_[1]");
    my $reg1 = $_[2]; my $reg2 = $_[3];
    while(my $l = <F1>)
    {   
        chomp($l); 
        if(index($l,$reg1) != -1){
            my $line = $l;
            $line =~ s/$reg1/$reg2/g;
            print F2 $line,"\n";}
        else
        {print F2 $l."\n";}
    }
    close F1; close F2; unlink $_[0]; rename $_[1], $_[0]; 
    
}
