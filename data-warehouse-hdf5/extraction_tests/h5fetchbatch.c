/* h5fetchbatch.c, DEM 30dec2015, from h5fetch.c
   Fetch multiple single cells (subset) from a two-dimensional HDF5 file. 
   60 in a batch seems to be the limit.  70 seg-faults. */

#include "hdf5.h"
#include <stdlib.h>

#define FILE        "/home/matthews/HDF5testing/NAM/gobii.h5"
#define DATASETNAME "/maizenam_c1+2" 
#define RANK  2

#define DIM0_SUB  60                      /* subset dimensions */ 
#define DIM1_SUB  1                         

int main (int argc, char *argv[]) {

  /* HDF5 variables */
  /* hsize_t     dims[RANK], dimsm[RANK];    */
  hsize_t     dimsm[RANK];   
  hid_t       file_id, dataset_id;        /* handles */
  hid_t       dataspace_id, memspace_id; 
  herr_t      status;                   
  int coorddim = DIM0_SUB * RANK;
  hsize_t     coord[coorddim];

  size_t      num_elements;
  char        rdata[DIM0_SUB][DIM1_SUB];  /* buffer for read */

  int i;

  if (argc < 3) {
    printf("%s", "Usage: h5fetchbatch <Sample number> <Marker number> [<Sample number> <Marker number>] ...\n");
    printf("%s", "Up to 60 Sample/Marker pairs\n");
    return 0;
  }

  /* Serialize the arguments into coord[]. */
  for (i = 1; i < argc; ++i) {
    coord[i - 1] = atoi(argv[i]);
  }

  /* Open the HDF5 file and dataset. */
  file_id = H5Fopen (FILE, H5F_ACC_RDONLY, H5P_DEFAULT);
  dataset_id = H5Dopen2 (file_id, DATASETNAME, H5P_DEFAULT);
  /* Get the size (dimensions) of the HDF5 dataset. */
  /* dims = H5Dget_storage_size(dataset_id); */

  /* Create memory space with size of subset. Get file dataspace
     and select subset from file dataspace. */
  dimsm[0] = DIM0_SUB;
  dimsm[1] = DIM1_SUB;
  memspace_id = H5Screate_simple (RANK, dimsm, NULL); 
  dataspace_id = H5Dget_space (dataset_id);
  num_elements = DIM0_SUB;
  /* herr_t H5Sselect_elements( hid_t space_id, H5S_seloper_t op, size_t num_elements, const hsize_t *coord ) */
  status = H5Sselect_elements (dataspace_id, H5S_SELECT_SET, num_elements, coord); 

  /* Read the cells. */
  /* herr_t H5Dread( hid_t dataset_id, hid_t mem_type_id, hid_t mem_space_id, hid_t file_space_id, hid_t xfer_plist_id, void * buf ) */
  status = H5Dread (dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, rdata);

  /* Print the results to stdout as a blank-delimited string. */
  for (i = 0; i < DIM0_SUB; ++i)
    printf("%c ", rdata[i][0]);
  printf("\n");

  status = H5Sclose (memspace_id);
  status = H5Sclose (dataspace_id);
  status = H5Dclose (dataset_id);
  status = H5Fclose (file_id);
  if (status >= 0) return 0;
  else return 1;

}
