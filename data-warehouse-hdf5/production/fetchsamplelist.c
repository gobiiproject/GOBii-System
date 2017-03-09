/* fetchsamplelist.c, 8jun2016, from fetchsample.c, 7may106, from: samplestest.c, 26jan2015, from h5fetchsample.c, 25jan2015
   Fetch a set of entire rows, alleles for all markers for the specified samples. */
/* 12jun16, Detect and handle different datatypes. */

#include "hdf5.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>

#define DATASETNAME "/allelematrix_samples-fast" 
#define RANK  2                           /* number of dimensions */

int main (int argc, char *argv[]) {

  /* HDF5 variables */
  hsize_t     filedims[RANK], dimsm[RANK];   
  hsize_t     start[RANK], stride[RANK], count[RANK], block[RANK];
  hid_t       file_id, dataset_id;        /* handles */
  hid_t       dataspace_id, memspace_id; 
  hid_t       datumtype;
  herr_t      status;                   

  FILE *outfile;
  int datumsize, i, j, k;

  if (argc < 3) {
    printf("Usage: %s <HDF5 file> <output file> <sample number> [<sample number] ...\n", argv[0]);
    printf("E.g. %s /shared_data/HDF5/Maize/SeeD_unimputed.h5 /tmp/fetchsamples.out 0 679 4844\n", argv[0]);
    printf("E.g. %s /shared_data/HDF5/Rice/PhasedSNPs.h5 /tmp/fetchsamples.out 0 63 567\n", argv[0]);
    printf("Fetch alleles for up to 100 specified samples, for all markers.\n");
    return 0;
  }
  /* Read the arguments. */
  char *h5filename = argv[1];
  char *outfilename = argv[2];
  outfile = fopen (outfilename, "w");
  int samplecount = argc - 3;
  int samples[samplecount];
  for (i = 3; i < argc; i++) 
    samples[i - 3] = atoi(argv[i]);

  /* Open the HDF5 file and dataset. */
  file_id = H5Fopen (h5filename, H5F_ACC_RDONLY, H5P_DEFAULT);
  dataset_id = H5Dopen2 (file_id, DATASETNAME, H5P_DEFAULT);
  dataspace_id = H5Dget_space (dataset_id);

  /* Find the dimensions of the HDF5 file dataset. */
  H5Sget_simple_extent_dims(dataspace_id, filedims, NULL);
  int SampleTotal = filedims[0];
  int MarkerTotal = filedims[1];

  /* Determine the datatype and the size of an individual element. */
  datumtype = H5Dget_type(dataset_id);
  datumsize = H5Tget_size(datumtype);
  /* printf("datumsize = %i\n", datumsize); */
  /* return 0; */

  /* Create memory space with size of subset. Get file dataspace. */
  dimsm[0] = 1;
  dimsm[1] = MarkerTotal; 
  char rdata[MarkerTotal * datumsize];  /* buffer for read */
  memspace_id = H5Screate_simple (RANK, dimsm, NULL); 
  /* Select subset from file dataspace. */
  start[1] = 0;
  stride[0] = 1; stride[1] = 1;
  count[0] = 1; count[1] = 1;
  block[0] = 1; block[1] = MarkerTotal;
  /* Open the output file. */

  for (i = 0; i < samplecount; i++) {
    if (samples[i] >= SampleTotal) {
      printf("Sample number %i out of range.\n", samples[i]);
      return 1;
    }
    start[0] = samples[i];
    status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, start, stride, count, block);
    /* Read the hyperslab. */
    status = H5Dread (dataset_id, datumtype, memspace_id, dataspace_id, H5P_DEFAULT, rdata);
    /* Write the results to the output file, as a tab-delimited string for each sample. */
    for (j = 0; j < MarkerTotal * datumsize; j = j + datumsize) {
      for (k = 0; k < datumsize; k++) 
	fprintf(outfile, "%c", rdata[j + k]);
      /* No trailing <Tab> at end of line. */
      if (j < (MarkerTotal - 1) * datumsize)
	fprintf(outfile, "\t");
    }
    fprintf(outfile, "\n");
  }
  fclose(outfile);

  status = H5Tclose(datumtype);
  status = H5Sclose (memspace_id);
  status = H5Sclose (dataspace_id);
  status = H5Dclose (dataset_id);
  status = H5Fclose (file_id);
  if (status >= 0) return 0;
  else return 1;
}
