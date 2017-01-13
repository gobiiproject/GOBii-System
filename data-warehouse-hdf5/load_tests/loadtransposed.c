/* loadtransposed.c, 29apr16, from:*/
/* loadNAM-t-CIMMYT.c, debug version. Load CIMMYT dataset instead. Load time 29 hours. */
/* , from loadNAM-transpose.c, DEM 26feb16
   , from example code h5_crtdat.c etc.  */

/* Load marker allele data SIDEWAYS!  I.e., one column (dimension 1) per marker */
/* Batch the load, 10000 markers at a time, ca 50 MB transposed in memory. */

#include "hdf5.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>

int main (int argc, char *argv[]) {

  /* HDF5 variables */
  hid_t       file_id, dataset_id, dataspace_id, memspace_id;  /* identifiers */
  hsize_t     dims[2], dimsmem[2];;
  herr_t      status;
  /* Hyperslab dimensions */
  hsize_t    offset[2], stride[2], count[2], blocksize[2];

  FILE *infile;
  char *row;   
  char *token;
  int rownum, outndx;
  char *infilename;
  char *h5file;
  char *h5dsname;
  int batchsize = 10000;
  /* infilename = "./cimmyt-head.txt"; */
  infilename = "/home/matthews/HDF5testing/CIMMYT/AllZeaGBSv2.7_SEED_Beagle4_ALL.hmp.txt";

  if (argc != 3) {
    printf("Usage: %s <HDF5 file> <dataset name>\n", argv[0]);
    printf("Example: %s gobii_transpose.h5 /CIMMYT\n", argv[0]);
    printf("Input file is %s\n", infilename);
    return 0;
  }
  /* Read in the command arguments */
  h5file = argv[1];
  h5dsname = argv[2];
  int SampleCount = 4845;
  int MarkerCount = 1000000;

  /* Create a new HDF5 file using default properties. */
  file_id = H5Fcreate(h5file, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);

  /* Create the data space for the dataset. 5000 samples x 1M markers*/
  dims[0] = SampleCount; 
  dims[1] = MarkerCount; 
  dataspace_id = H5Screate_simple(2, dims, NULL);

  /* Create the dataset. Each element is type CHAR. */
  dataset_id = H5Dcreate2(file_id, h5dsname, H5T_NATIVE_CHAR, dataspace_id, 
                          H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

  /* Open the file.  */
  file_id = H5Fopen(h5file, H5F_ACC_RDWR, H5P_DEFAULT);

  /* Create a memory buffer space. */
  dimsmem[0] = SampleCount;
  dimsmem[1] = batchsize; 
  memspace_id = H5Screate_simple(2, dimsmem, NULL);

  /* Create the initial hyperslab dimensions */
  offset[0] = 0; offset[1] = 0;
  stride[0] = 1; stride[1] = 1; 
  count[0] = 1; count[1] = 1;
  blocksize[0] = SampleCount; blocksize[1] = batchsize; 

  /* Declare the 2D array for reading the input file into. */
  char dset_data[SampleCount][batchsize];

  row = (char *) malloc(1000000);
  infile = fopen (infilename, "r");
  rownum = 0;
  int batchcounter = 0;
  time_t starttime;
  starttime = time(NULL);
  int elapsed;
  char *temp = (char *) malloc(64);
  /* Read in the input file. */
  while (fgets (row, 1000000, infile)) {
    token = strtok(row, "\t");
    /* Omit the header rows. */
    if (strstr(token, "rs#") == NULL ) {
      outndx = 1;
      while ((token = strtok(NULL, "\t"))) {
	/* Omit the leading columns before the data (rs#, alleles, chrom etc.) */
	if (outndx > 10) {
	  /* Read the rest of the input line into dset_data[]. */
	  /* Store the value of token rather than using the pointer to it. */
	  /* dset_data[batchcounter][outndx - 11] = token[0]; */
	  strcpy(temp, token);
	  /* dset_data[outndx - 11][batchcounter] = temp[0]; */
	  dset_data[outndx - 11][batchcounter] = token[0];
	}
	++outndx;
      }
      if (batchcounter == batchsize -1) {
	elapsed = (time(NULL) - starttime);
	printf("marker %i, %i sec\n", rownum, elapsed);

	/* Adjust the hyperslab column. */
	offset[1] = rownum - (batchsize-1);

	status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
	/* Write to the dataset. */
	status = H5Dwrite(dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, dset_data);
	printf("H5Dwrite status = %i\n", status);

	/* For debugging. Echo this data row to stdout. */
	int i;
	for (i = 0; i < SampleCount; ++i) 
	  printf("%c", dset_data[i][batchcounter]);
	printf("\n");

	batchcounter = 0;
      }
      rownum++;
      batchcounter++;
    }
  }
  /* At end of file. Now write out the remaining fraction of a batch. */
  dimsmem[1] = batchcounter; 
  hid_t rmemspace_id;
  rmemspace_id = H5Screate_simple(2, dimsmem, NULL);
  offset[1] = rownum - batchcounter;
  blocksize[1] = batchcounter;
  status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
  status = H5Dwrite(dataset_id, H5T_NATIVE_CHAR, rmemspace_id, dataspace_id, H5P_DEFAULT, dset_data);

  fclose (infile);
  free (row);

  /* End access to the dataset and release resources used by it. */
  status = H5Dclose(dataset_id);
  /* End access to the data space. */ 
  status = H5Sclose(dataspace_id);
  /* Close the file. */
  status = H5Fclose(file_id);

  if (status >= 0) return 0;
  else return 1;
}
