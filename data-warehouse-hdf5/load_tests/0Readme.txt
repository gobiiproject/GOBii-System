These C programs have been used to load allele matrix datasets from
various tab- or comma-delimited input files into HDF5 files.  Each loads
one input file (dataset) into one HDF5 file.  There are three HDF5
"datasets" per file: the allele matrix itself, the list of sample names,
and the list of marker names.  The lists are in the same order as the
indices of the two-dimensional matrix, samples on the horizontal axis
(dimension 1) and markers vertical (dimension 0).  The names of the HDF5
datasets can be seen with Unix h5ls(), and the contents with h5dump().
The base name for the HDF5 datasets is stored along with the HDF5 file
path in the PostgresQL record for that GOBII dataset in table
dataset.data_file.

The C programs should be compiled with "h5cc -O".  Compiled executables
are in the bin/ directory.  Executing with no arguments will print a
Usage message that includes a successful example, e.g.
Example: loadHapmap /home/matthews/Data/Maize/SeeD/All_SeeD_2.7_chr1-10_no_filter.unimputed.hmp.txt /shared_data/HDF5/Maize/SeeD_unimputed.h5 SeeD

Some programs require unlimiting the system stacksize to avoid a
segmentation fault. In csh, say "limit stacksize unlimited.
