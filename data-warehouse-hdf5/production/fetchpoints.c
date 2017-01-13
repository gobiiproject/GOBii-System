/* fetchpoints.c, 7may16, from: h5fetchbatch.c, DEM 30dec2015, from h5fetch.c
   Fetch multiple single cells (subset) from a two-dimensional HDF5 file. 
   60 in a batch seems to be the limit.  70 seg-faults. */
/* 13jun16, Detect and handle different datatypes. */

#include "hdf5.h"
#include <stdlib.h>

#define RANK  2
#define DIM0  60                      /* subset dimensions */ 
#define DIM1  1                         

int main (int argc, char *argv[]) {

  /* HDF5 variables */
  hsize_t     dimsm[RANK];   
  hid_t       file_id, dataset_id;    /* handles */
  hid_t       dataspace_id, memspace_id; 
  hid_t       datumtype;
  herr_t      status;                   
  int coorddim = DIM0 * RANK;
  hsize_t     coord[coorddim];
  size_t      num_elements;

  char *h5datasetname = "allelematrix_samples-fast";
  FILE *out;

  int datumsize, i, k;

  if (argc < 5) {
    printf("Usage: %s <HDF5 file> <output file> <Sample number> <Marker number> [<Sample number> <Marker number>] ...\n", argv[0]);
    printf("Example: %s /shared_data/HDF5/Maize/SeeD_unimputed.h5 /tmp/fetchpoints.out 0 0 4844 955100\n", argv[0]);
    printf("Example: %s /shared_data/HDF5/Rice/PhasedSNPs.h5 /tmp/fetchpoints.out 0 0 566 4605\n", argv[0]);
    printf("Up to 60 Sample/Marker pairs\n");
    return 0;
  }
  char *h5file = argv[1];
  char *outfile = argv[2];
  /* Serialize the remaining arguments into coord[]. */
  for (i = 3; i < argc; i++) 
    coord[i - 3] = atoi(argv[i]);
  num_elements = (argc - 3) / 2;

  /* Open the HDF5 file and dataset. */
  file_id = H5Fopen (h5file, H5F_ACC_RDONLY, H5P_DEFAULT);
  dataset_id = H5Dopen2 (file_id, h5datasetname, H5P_DEFAULT);

  /* Determine the datatype and the size of an individual element. */
  datumtype = H5Dget_type(dataset_id);
  datumsize = H5Tget_size(datumtype);

  /* Create memory space with size of subset. */
  dimsm[0] = num_elements;
  dimsm[1] = DIM1;
  memspace_id = H5Screate_simple (RANK, dimsm, NULL); 
  /* Get file dataspace and select subset from file dataspace. */
  dataspace_id = H5Dget_space (dataset_id);
  status = H5Sselect_elements (dataspace_id, H5S_SELECT_SET, num_elements, coord); 

  /* Read the cells. */
  /* char rdata[num_elements][DIM1];  /\* buffer for read *\/ */
  char rdata[num_elements * datumsize][DIM1];  /* buffer for read */
  /* status = H5Dread (dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, rdata); */
  status = H5Dread (dataset_id, datumtype, memspace_id, dataspace_id, H5P_DEFAULT, rdata);

  /* Output the results as a tab-delimited string. */
  out = fopen(outfile, "w");
  for (i = 0; i < num_elements * datumsize; i = i + datumsize) {
      for (k = 0; k < datumsize; k++) 
	fprintf(out, "%c", rdata[i + k][0]);
      /* No trailing <Tab> at end of line. */
      if (i < (num_elements - 1) * datumsize)
	fprintf(out, "\t");
    }
    fprintf(out, "\n");

  status = H5Sclose (memspace_id);
  status = H5Sclose (dataspace_id);
  status = H5Dclose (dataset_id);
  status = H5Fclose (file_id);
  if (status >= 0) return 0;
  else return 1;

}
