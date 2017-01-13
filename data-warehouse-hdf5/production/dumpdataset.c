/* dumpdataset.c, 9jun16, from fetchsample.c, 7may106, from: samplestest.c, 26jan2015
   Fetch an entire dataset, alleles for all markers for all samples, in standard sites-fast orientation. */
/* 14jun16: Read the datatype from the file, instead of requiring a command-line argument. */

#include "hdf5.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>

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
  char *h5dataset;
  int datumsize, i, j, k;

  if (argc < 4) {
    printf("Usage: %s <orientation> <HDF5 file> <output file>\n", argv[0]);
    printf("E.g. %s samples-fast /shared_data/HDF5/Rice/PhasedSNPs.h5 /tmp/dumpdataset.out\n", argv[0]);
    printf("E.g. %s markers-fast /shared_data/HDF5/Maize/SeeD_unimputed.h5 /tmp/dumpdataset.out\n", argv[0]);
    printf("Fetch alleles for all markers, all samples.\n");
    printf("<orientation> is either 'samples-fast' or 'markers-fast'.\n");
    return 0;
  }
  /* Read the arguments. */
  if (strcmp(argv[1], "samples-fast") == 0)
    h5dataset = "/allelematrix_samples-fast";
  else
    h5dataset = "/allelematrix";
  char *h5filename = argv[2];
  char *outfilename = argv[3];

  /* Open the HDF5 file and dataset. */
  file_id = H5Fopen (h5filename, H5F_ACC_RDONLY, H5P_DEFAULT);
  if (file_id < 0) {
    printf("Failure opening input file %s\n", h5filename);
    return 1;
  }
  dataset_id = H5Dopen2 (file_id, h5dataset, H5P_DEFAULT);
  dataspace_id = H5Dget_space (dataset_id);

  /* Find the dimensions of the HDF5 file dataset. */
  H5Sget_simple_extent_dims(dataspace_id, filedims, NULL);
  int SampleTotal = filedims[0];
  int MarkerTotal = filedims[1];

  /* Determine the datatype and the size of an individual element. */
  datumtype = H5Dget_type(dataset_id);
  datumsize = H5Tget_size(datumtype);

  /* Create memory space with size of the dataset. */
  dimsm[0] = 1;
  dimsm[1] = MarkerTotal; 
  memspace_id = H5Screate_simple (RANK, dimsm, NULL); 
  char rdata[MarkerTotal * datumsize];  /* buffer for read */
  /* Select subset from file dataspace. */
  start[1] = 0;
  stride[0] = 1; stride[1] = 1;
  count[0] = 1; count[1] = 1;
  block[0] = 1; block[1] = MarkerTotal;
  
  outfile = fopen (outfilename, "w");
  for (j = 0; j < SampleTotal; j++) {
    start[0] = j;
    status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, start, stride, count, block);
    /* Read the hyperslab. */
    status = H5Dread (dataset_id, datumtype, memspace_id, dataspace_id, H5P_DEFAULT, rdata);
    /* Write the results to the output file, as a tab-delimited string for each sample. */
    for (i = 0; i < MarkerTotal * datumsize; i = i + datumsize) {
      for (k = 0; k < datumsize; k++) 
	fprintf(outfile, "%c", rdata[i + k]);
      /* No trailing <Tab> at end of line. */
      if (i < (MarkerTotal - 1) * datumsize)
	fprintf(outfile, "\t");
    }
    fprintf(outfile, "\n");
  }
  fclose(outfile);

  status = H5Tclose (datumtype);
  status = H5Sclose (memspace_id);
  status = H5Sclose (dataspace_id);
  status = H5Dclose (dataset_id);
  status = H5Fclose (file_id);
  if (status >= 0) return 0;
  else return 1;
}
