/* fetchsample.c, 7may106, from: samplestest.c, 26jan2015, from h5fetchsample.c, 25jan2015
   Fetch an entire row, alleles for all markers for the specified sample. */
/* 1sep16: Handle different datatypes. */

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
  hid_t       dataspace_id, memspace_id, datumtype; 
  herr_t      status;                   

  FILE *outfile;
  int datumsize, i, k;

  if (argc < 3) {
    printf("Usage: %s <HDF5 file> <sample number> <output file>\n", argv[0]);
    printf("E.g. %s /shared_data/HDF5/Maize/SeeD_unimputed.h5 4844 /tmp/fetchsample.out\n", argv[0]);
    printf("E.g. %s /shared_data/HDF5/Rice/PhasedSNPs.h5 100 /tmp/fetchsample.out\n", argv[0]);
    printf("Fetch alleles for all markers for the specified sample.\n");
    return 0;
  }
  /* Read the arguments. */
  char *h5filename = argv[1];
  int sample = atoi(argv[2]);
  char *outfilename = argv[3];

  /* Open the HDF5 file and dataset. */
  file_id = H5Fopen (h5filename, H5F_ACC_RDONLY, H5P_DEFAULT);
  dataset_id = H5Dopen2 (file_id, DATASETNAME, H5P_DEFAULT);
  dataspace_id = H5Dget_space (dataset_id);

  /* Find the dimensions of the HDF5 file dataset. */
  H5Sget_simple_extent_dims(dataspace_id, filedims, NULL);
  if (sample >= filedims[0]) {
    printf("Sample number %i out of range.\n", sample);
    return 1;
  }
  int MarkerTotal = filedims[1];

  /* Determine the datatype and the size of an individual element. */
  datumtype = H5Dget_type(dataset_id);
  datumsize = H5Tget_size(datumtype);

  /* Create memory space with size of subset. Get file dataspace. */
  dimsm[0] = 1;
  dimsm[1] = MarkerTotal; 
  char rdata[MarkerTotal * datumsize];  /* buffer for read */
  memspace_id = H5Screate_simple (RANK, dimsm, NULL); 
  /* Select subset from file dataspace. */
  start[0] = sample; start[1] = 0;
  stride[0] = 1; stride[1] = 1;
  count[0] = 1; count[1] = 1;
  block[0] = 1; block[1] = MarkerTotal;
  status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, start, stride, count, block);
  /* Read the hyperslab. */
  status = H5Dread (dataset_id, datumtype, memspace_id, dataspace_id, H5P_DEFAULT, rdata);

  /* Write the results to the output file, as a tab-delimited string for each marker. */
  outfile = fopen (outfilename, "w");
  for (i = 0; i < MarkerTotal * datumsize; i = i + datumsize) {
    for (k = 0; k < datumsize; k++) 
      fprintf(outfile, "%c", rdata[i + k]);
    /* No trailing <Tab> at end of line. */
    if (i < (MarkerTotal - 1) * datumsize)
      fprintf(outfile, "\t");
  }
  fclose(outfile);

  status = H5Sclose (memspace_id);
  status = H5Sclose (dataspace_id);
  status = H5Dclose (dataset_id);
  status = H5Fclose (file_id);
  if (status >= 0) return 0;
  else return 1;
}
