/* samplestest.c, 26jan2015, from h5fetchsample.c, 25jan2015
   Fetch a set of entire columns from a two-dimensional HDF5 file. */

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
  int i, j;
  int column;
  time_t starttime, stoptime;
  int elapsed;

  if (argc < 3) {
    printf("Usage: %s <number of samples> <contiguous|random> <total markers in dataset>\n", argv[0]);
    printf("E.g. %s 1000 random 21923079\n", argv[0]);
    printf("Fetch alleles for all markers for the specified number of samples\n");
    printf("Output is in samplestest.out and samplestest.log.\n");
    return 0;
  }
  /* Read the arguments. */
  int sampletotal = atoi(argv[1]);
  char *mode = argv[2];
  int dim0 = atoi(argv[3]);

  int dim0_sub = dim0;        /* size of subset */
  char results[dim0_sub][sampletotal]; /* indexed by [marker][sample] */
  int header[sampletotal];

  /* Write a log of the results. */
  logfile = fopen ("samplestest.log", "a");
  fprintf(logfile, "Number of samples: %i\n", sampletotal);
  fprintf(logfile, "Mode: %s\n", mode);

  starttime = time(NULL);
  /* Open the HDF5 file and dataset. */
  file_id = H5Fopen (H5FILE, H5F_ACC_RDONLY, H5P_DEFAULT);
  dataset_id = H5Dopen2 (file_id, DATASETNAME, H5P_DEFAULT);

  if (strcmp(mode, "random") == 0) {
    /* Create memory space with size of subset. Get file dataspace. */
    dimsm[0] = dim0_sub;
    dimsm[1] = 1;   /* one column for each pass of the for-loop */
    char rdata[dim0_sub][1];  /* buffer for read */
    memspace_id = H5Screate_simple (RANK, dimsm, NULL); 
    dataspace_id = H5Dget_space (dataset_id);
    for (i = 0; i < sampletotal; ++i) {
      column = rand() % sampletotal;
      header[i] = column;
      /* Select subset from file dataspace. */
      start[0] = 0; start[1] = column;
      stride[0] = 1; stride[1] = 1;
      count[0] = 1; count[1] = 1;
      block[0] = dim0_sub; block[1] = 1;
      status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, start, stride, count, block);
      /* Read the hyperslab. */
      status = H5Dread (dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, rdata);
      /* Copy this column from rdata[] to results[][]. */
      for (j = 0; j < dim0_sub; ++j) {
	results[j][i] = rdata[j][0];
      }
    }
    /* Write the results to the output file, as a comma-delimited string for each marker. */
    outfile = fopen ("samplestest.out", "w");
    /* Header is the sample numbers. */
    for (i = 0; i < sampletotal; ++i) {
      fprintf(outfile, "%i,", header[i]);
    }
    fprintf(outfile, "\n");
    for (j = 0; j < dim0_sub; ++j) {
      for (i = 0; i < sampletotal; ++i) {
	fprintf(outfile, "%c,", results[j][i]);
      }
      fprintf(outfile, "\n");
    }
    fclose(outfile);
    status = H5Sclose (memspace_id);
    status = H5Sclose (dataspace_id);
  }

  else if (strcmp(mode, "contiguous") == 0) {
    /* First column of the region, at most 1000 to leave room for the region. */
    column = rand() % 1000;
    int firstsample = column + 1;
    /* Create memory space with size of subset. Get file dataspace. */
    dimsm[0] = dim0_sub;
    dimsm[1] = sampletotal;
    memspace_id = H5Screate_simple (RANK, dimsm, NULL); 
    dataspace_id = H5Dget_space (dataset_id);
    char rdata[dim0_sub][sampletotal];  /* buffer for read */
    /* Select subset from file dataspace. */
    start[0] = 0; start[1] = column;
    stride[0] = 1; stride[1] = 1;
    count[0] = 1; count[1] = 1;
    block[0] = dim0_sub; block[1] = sampletotal;
    status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, start, stride, count, block);
    /* Read the hyperslab. */
    status = H5Dread (dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, rdata);
    /* Write the results to the output file, as a comma-delimited string for each marker. */
    outfile = fopen ("samplestest.out", "w");
    fprintf(outfile, "First sample: %i\n", firstsample);
    for (j = 0; j < dim0_sub; ++j) {
      for (i = 0; i < sampletotal; ++i) {
	fprintf(outfile, "%c,", rdata[j][i]);
      }
      fprintf(outfile, "\n");
    }
    fclose(outfile);
    status = H5Sclose (memspace_id);
    status = H5Sclose (dataspace_id);
  }
  else {
    printf("Unrecognized argument: %s\n", argv[2]);
  }

  stoptime = time(NULL);
  elapsed = stoptime - starttime;
  fprintf(logfile, "Elapsed time: %i seconds\n\n", elapsed);
  fclose(logfile);

  status = H5Dclose (dataset_id);
  status = H5Fclose (file_id);
  if (status >= 0) return 0;
  else return 1;
}
