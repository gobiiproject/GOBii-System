# Using loadHDF5

The application takes three arguments: the datasize for each
allele/datacell in the input, the name of the input Intermediate Format
file, and the name of the destination HDF5 file. The valid values for
datasize are between 1-10.  The input file should contain nothing but
a tab-delimited matrix of allele calls.  Each sample should be a
column and each marker should be a row.

The data is stored in two HDF5 *datasets* in the output HDF5 file, named
"allelematrix" (original orientation) and "allelematrix_samples-fast"
(transposed).

The HDF5 file can be browsed with `h5dump()`.

# Dumping a dataset

`bin/dumpdataset` outputs the specified HDF5 file to the specified
output file as a tab-delimited line for each sample.  The first argument
is the orientation desired for the output, "samples-fast" or
"markers-fast".

# Using the "fetch" programs

`bin/fetchsample` takes three arguments: the name of the source HDF5
file, the index position of the desired sample, and the name of the
output file.  Output is a one-line file with the alleles of all markers
for that sample, tab-delimited.

`bin/fetchmarker` takes three arguments: the name of the source HDF5
file, the index position of the desired marker, and the name of the
output file.  Output is a one-line file with the alleles of all samples
for that marker, tab-delimited.

`bin/fetchmarkerlist` takes four arguments: the orientation of the
output (samples as rows or markers as rows), the name of the source HDF5
file, the name of a file containing a list of index positions of the
desired markers, and the name of the output file.  Output is a
tab-delimited file in the orientation specified.

`bin/fetchpoints` takes at least four arguments: the name of the source
HDF5 file, the name of the output file, and at least one pair of (index
position of sample, index position of marker), all punctuated only by
blanks.  Up to sixty sample/marker pairs can be requested, which is
faster than running the program sixty times.


All programs output a Usage message and a working example if they are
invoked with no arguments.

# Compiling the C programs

Use "h5cc -O".  (That's an oh, not a zero.)
