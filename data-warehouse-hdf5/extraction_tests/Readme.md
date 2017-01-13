## Extraction from Ed Buckler's maize NAM dataset, chromosome 1+2 (20M markers)

The C programs are compiled with `h5cc()`, the HDF5 C compiler.  The
data were loaded with `loadNAM.c`. The NAM dataset is stored in a single
HDF5 "dataset" within the HDF5 file _gobii.h5_.  Only the marker variant
calls are stored, not the names of the samples or markers.  The storage
format is a two-dimensional array of char (1 byte), with samples in
dimension 1 ("horizontal") and markers in dimension 0 ("vertical").
That is, each sample is a column and each marker is a row.  The names of
the samples and markers and their indexes in the HDF5 array, as well as
the HDF5 filename and dataset name, should be stored as metadata in
PostgreSQL.

### Extraction types

1. All markers for N samples  
`bin/samplestest` takes three arguments: the number of samples, whether they
are distributed randomly or as a contiguous block, and the total number
of markers in the HDF5 dataset.  The results are stored in file
"samplestest.out", and the elapsed time in "samplestest.log".

2. All samples for N markers  
`bin/markerstest` takes three arguments: the number of markers, whether they
are distributed randomly or as a contiguous block, and the total number
of samples in the HDF5 dataset.  The results are stored in file
"markerstest.out", and the elapsed time in "markerstest.log".

3. Arbitrary sample/marker pairs
  * `bin/haplarray` takes two arguments: the number of markers and the number
of samples, both assumed non-contiguous.  The results are stored in file
"haplarray.csv", and the elapsed time in "haplarray.log".
  * `bin/h5fetchbatch` takes as arguments up to 60 pairs of  
\<Sample number\> \<Marker number\> [\<Sample number\> \<Marker number\>] ...  
The results are printed to stdout as a blank-delimited string.

**Note:**  
For the bigger test cases it's necessary to increase the system stacksize
to avoid running out of memory.  In csh, "limit stacksize unlimited".
