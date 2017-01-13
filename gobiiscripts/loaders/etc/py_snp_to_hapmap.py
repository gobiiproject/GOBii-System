#!/usr/bin/env python
#
# testing with itertools
import sys
import csv
import itertools


infile = sys.argv[1]
outfile = sys.argv[2]

start_row = 6
start_col = 32

# TODO fileouput from argv
outFile = open(outfile, 'w')
wr = csv.writer(outFile, lineterminator='\n', delimiter='\t')
hmap_header = ['rs#', 'alleles', 'chrom', 'pos', 'strand', 'assembly', 'center', 'protLSID', 'assayLSID', 'panelLSID', 'QCcode']

# Begin funtion to convert heterosigosity


def line_heteroz(count, marker, allele):
    res_row  = []
    for t, b in itertools.izip_longest(topline[start_col:], bottomline[start_col:]):
        dat = str(t)+str(b)
        if dat == '--' or dat == '0-' or dat == '-0' or dat == '00':
            res_row.append('N')
        elif dat == '11':
            hete = str(allele[0])+str(allele[-1])
            if hete == 'AG' or hete == 'GA':
                res_row.append('R')
            elif hete == 'CT' or hete == 'TC':
                res_row.append('Y')
            elif hete == 'GC' or hete == 'CG':
                res_row.append('S')
            elif hete == 'AT' or hete == 'TA':
                res_row.append('W')
            elif hete == 'GT' or hete == 'TG':
                res_row.append('K')
            elif hete == 'AC' or hete == 'CA':
                res_row.append('M')
            else:
                res_row.append('N')
        elif dat == '10' or dat == '1-':
            res_row.append(allele[0])
        elif dat == '01' or dat == '-1':
            res_row.append(allele[-1])
        else:
            print ('Error in marker file')
    return hapcols(count, marker, allele, res_row)


def  hapcols(count, marker, allele, res_row):
    rs = marker
    alleles = allele
    chrom = 0
    pos = count
    strand = '+'
    assembly = 'NA'
    center = 'NA'
    protLSID = 'NA'
    assayLSID = 'NA'
    panelLSID = 'NA'
    QCcode = 'NA'
    hap_row = [rs, alleles, chrom, pos, strand, assembly, center, protLSID, assayLSID, panelLSID, QCcode]
    return hap_row + res_row


# Main Function

with open(infile, 'r') as f:
# Skip the first 4 lines starting 0
    for i in range (0, start_row - 1):
        next(f)
# Recall line 5 starting 0 to header
    header_line = next(f).strip().split(',')
    count = 0
    wr.writerow(hmap_header+header_line[start_col:])

    for line1, line2 in itertools.izip_longest(*[f]*2):
        topline = line1.strip().split(',')
        bottomline = line2.strip().split(',')
        if len(topline) != len(bottomline):
            print('Error, different long line')
        else:
            topmarkername = topline[0].strip().split('-')
            botmarkername = bottomline[0].strip().split('-')
            if topmarkername[0] != botmarkername[0]:
                print('Error, markers do not match')
            else:
                toppolym = topmarkername[-1]
                botpolym = botmarkername[-1]
                if toppolym != botpolym:
                    print('Error, polyms do not match')
                else:
                    base = toppolym.strip().split(':')[1]
                    allele = base.replace('>', '/')
                    print 'Processing line # %s with allele: %s' % (count, allele)
#                    print line_heteroz(count,topline[0], allele)
                    wr.writerow(line_heteroz(count, topline[0], allele))
                    count += 1
