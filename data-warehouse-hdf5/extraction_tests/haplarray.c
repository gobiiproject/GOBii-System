/* haplarray.c, 4mar16, from markersstest.c, 27jan2015
   Test performance of looking up alleles for an arbitrary set of markers 
   for an arbitrary set of samples, non-contiguous. */

#include "hdf5.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>

#define H5FILE      "/home/matthews/HDF5testing/NAM/gobii.h5"
#define DATASETNAME "/maizenam_c1+2" 
#define RANK  2                           /* number of dimensions */

int main (int argc, char *argv[]) {

  /* HDF5 variables */
  hsize_t     dimsm[RANK];   
  hsize_t     start[RANK], stride[RANK], count[RANK], block[RANK];
  hid_t       file_id, dataset_id;        /* handles */
  hid_t       dataspace_id, memspace_id; 
  herr_t      status;                   

  FILE *outfile;
  FILE *logfile;
  int i, j, s;
  int row;
  time_t starttime, stoptime;
  int elapsed;

  if (argc < 3) {
    printf("Usage: %s <number of markers> <number of samples>\n", argv[0]);
    printf("E.g. %s 1000 1000\n", argv[0]);
    printf("Fetch alleles for the specified numbers of random markers and samples.\n");
    printf("Output is in haplarray.csv and haplarray.log.\n");
    return 0;
  }
  /* Read the arguments. */
  int markertotal = atoi(argv[1]);
  int sampletotal = atoi(argv[2]);

  int dim1 = 5258;                        /* number of samples in the dataset */
  int samples[sampletotal];              /* random sample numbers */

  /* Write a log of the results. */
  logfile = fopen ("haplarray.log", "a");
  fprintf(logfile, "%i markers, %i samples\n", markertotal, sampletotal);
  fprintf(logfile, "Mode: %s\n", "random");

  starttime = time(NULL);
  /* Create the list of random sample numbers. */
  for (s = 0; s < sampletotal; ++s) {
    samples[s] = rand() % dim1;
  }

  /* Open the HDF5 file and dataset. */
  file_id = H5Fopen (H5FILE, H5F_ACC_RDONLY, H5P_DEFAULT);
  dataset_id = H5Dopen2 (file_id, DATASETNAME, H5P_DEFAULT);

  outfile = fopen ("haplarray.csv", "w");
  for (s = 0; s < sampletotal; ++s)
    fprintf(outfile, "%i,", samples[s]);
  fprintf(outfile, "\n");
  /* Create memory space with size of the subset, one row. */
  dimsm[0] = 1;
  dimsm[1] = dim1;
  memspace_id = H5Screate_simple (RANK, dimsm, NULL); 
  /* Get file dataspace. */
  dataspace_id = H5Dget_space (dataset_id);
  char rdata[1][dim1];  /* buffer for read */
  for (i = 0; i < markertotal; ++i) {
    row = rand() % 1000000;
    /* Select subset from file dataspace. */
    start[0] = row; start[1] = 0;
    stride[0] = 1; stride[1] = 1;
    count[0] = 1; count[1] = 1;
    block[0] = 1; block[1] = dim1;
    status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, start, stride, count, block);
    /* Read the hyperslab. */
    status = H5Dread (dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, rdata);
    /* Output the marker number. */
    fprintf(outfile, "%i ", row);
    /* Write the results to the output file, as a comma-delimited string for each marker. */
    for (s = 0; s < sampletotal; ++s) {
      j = samples[s];
      fprintf(outfile, "%c,", rdata[0][j]);
    }
    fprintf(outfile, "\n");
  }
  fclose(outfile);
  status = H5Sclose (memspace_id);
  status = H5Sclose (dataspace_id);

  stoptime = time(NULL);
  elapsed = stoptime - starttime;
  fprintf(logfile, "Elapsed time: %i seconds\n\n", elapsed);
  fclose(logfile);

  status = H5Dclose (dataset_id);
  status = H5Fclose (file_id);
  if (status >= 0) return 0;
  else return 1;
}
