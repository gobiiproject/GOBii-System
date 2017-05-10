"""
Description:Python script to remove SNP separators and convert missing data to NN
Author: Kelly Robbins 5/20/16
"""

import csv
import sys
 
def readFile(inFileName,missingFileName,outFileName,):
#inFileName is the name of the file containing allele calls
#missingFileName is the name of the file containing a customizable list of calls to be converted to N
#outFile name is the name of the file for outputting the converted calls
    of=open(outFileName,'w')
# setting dictionaries of acceptable allele representations   
    Alleles=dict([('A',1),('C',2),('G',3),('T',4),('N',5),('+',6),('-',7),('?',8),('0',9),('',10)])
# used for conversions of missing data to N
    MissingAlts=dict([('?',8),('0',9),('',10)])
# storing missing calls from missingFile  
    MissingFromFile=dict()
 
#reading through file with missing calls to populate the dictionary Missing from file 
    with open(missingFileName) as csvfileMissing:
        readCSVMissing=csv.reader(csvfileMissing,delimiter='\t')
        for rowMissing in readCSVMissing:
            countMissing=1
            MissingFromFile[rowMissing[0]]=countMissing           

#    if('Uncallable' in MissingFromFile):
#        sys.stderr.write('captured'+'\n')
    errCount=1
#using CSV reader to read in the SSR file to convert
    with open(inFileName) as csvfile:
        readCSV = csv.reader(csvfile, delimiter='\t')
 
#looping through the rows to convert the data
        count=1
        for row in readCSV:
            
            if count>1:
                if count==2:
                    nCol=len(row) #identifying how many columns in the dataset
            
                call=row[0]
                nChar=len(call)
                if nChar<1:
                    allele1='N'
                    allele2='N'
                    outString=allele1 + allele2
#Taking the first and last character in the string call as the alleles
#checking to see if they are in the dictionary            
                elif call in  MissingFromFile:
                    allele1='N'
                    allele2='N'
                    outString=allele1 + allele2
                else:
                    if call[0] in Alleles and call[nChar-1] in Alleles:
                        allele1=call[0]
                        allele2=call[nChar-1]
                        if allele1 in MissingAlts:
                            allele1='N'
                        if allele2 in MissingAlts:
                            allele2='N'    
               
                        outString=allele1 + allele2

#If not in the dictionary errors are generated                
                    else:
                        if errCount<20:
                            sys.stderr.write(' Unsupported Allele Call ' +call[0] + ' ' + call[nChar-1] + ' row '+ str((count-1)) + ' column '+ '1'+'\n')          
                            outString='N' + 'N'
                            errCount=errCount+1
                        else:
                            sys.stderr.write('File processing stopped due to excessive format errors'+'\n')
                            sys.exit(2)
                         
            
                i=2
                while i <=nCol:
                    call=row[i-1]
                    nChar=len(call)
                    if nChar<1:
                        allele1='N'
                        allele2='N'
                        outString=outString+ '\t'+allele1 + allele2
                    elif call in  MissingFromFile:
                        allele1='N'
                        allele2='N'
                        outString=outString+ '\t' + allele1 + allele2
                    else:
                        if call[0] in Alleles and call[nChar-1] in Alleles:
                            allele1=call[0]
                            allele2=call[nChar-1]
                    
                            if allele1 in MissingAlts:
                                allele1='N'
                    
                            if allele2 in MissingAlts:
                                allele2='N'    
                    
                            outString=outString + '\t' + allele1 + allele2
               
                        else:
                    
                            if errCount<20:
                                sys.stderr.write('Unsupported Allele Call ' +call[0] + ' ' + call[nChar-1] + ' row '+ str((count-1)) + ' column '+ str(i)+'\n')          
                                outString= outString + '\t' + 'N' + 'N'
                                errCount=errCount+1
                    
                            else:
                                sys.stderr.write('File processing stopped due to excessive format errors'+'\n')
                                sys.exit(2)
                            
                    i=i+1        
                outString=outString + '\n'
                of.write(outString)
            count = count+1    
    of.close()
    csvfile.close()
    csvfileMissing.close()        
readFile(sys.argv[1],sys.argv[2],sys.argv[3])