/* loadHDF5.c, 1jul2016, from loadIFF.c, jun 15,16, from: loadPhased.c, DEM 1may16, from: loadSEED.c, DEM 6apr2016, ...
   from example code h5_crtdat.c etc.  */

/* Load one dataset in Intermediate File format into the specified HDF5 file. 
   Load both normal orientation (sites-fast) and transposed (taxa-fast).
   No header row or column, values Tab-separated.
   HDF5 datasize is passed as command line argument.    
*/

#include "hdf5.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>

int main(int argc, char *argv[]) {

  /* HDF5 variables */
  hid_t       file_id, dataset_id, dataspace_id, memspace_id, rmemspace_id;  /* identifiers */
  hid_t       datumtype;
  hsize_t     dims[2], dimsmem[2];;
  herr_t      status;
  /* Hyperslab dimensions */
  hsize_t    offset[2], stride[2], count[2], blocksize[2];

  FILE *infile;
  char *token;
  int datumsize, rownum, outndx, i, j, k;
  char *row;

  if (argc < 4) {
    printf("Usage: %s <datasize> <input file> <output HDF5 file>\n", argv[0]);
    printf("Example: %s 1 /home/matthews/BitBucket/production/testIFLinput.txt /shared_data/HDF5/Maize/SeeD_unimputed.h5\n", argv[0]);
    printf("Example: %s 2 /home/matthews/Data/Rice/Infinium/PhasedSNPs.ifl /shared_data/HDF5/Rice/PhasedSNPs.h5\n", argv[0]);
    printf("Example: %s 8 /shared_data/HDF5/test_files/SSRdata.ifl /shared_data/HDF5/Rice/ssr.h5\n", argv[0]);
    printf("<datasize> is a numeric value between 1 and 10.\n");
    return 0;
  }

  datumsize = atoi(argv[1]);
  //only creates datumtype for valid numeric entry & 
  if (datumsize >  0 && datumsize <= 10  ) {
    datumtype = H5Tcreate(H5T_STRING,datumsize);
  } else {
    printf("Invalid <datasize>.\n");	
    printf("Must be a numeric value between 1 and 10.\n");
    return 1;
  } 
  char *infilename = argv[2];
  char *h5file = argv[3];
  char *h5dataset = "allelematrix";

  /* Create a new HDF5 file using default properties. */
  file_id = H5Fcreate(h5file, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);
  /* Open the file.  */
  file_id = H5Fopen(h5file, H5F_ACC_RDWR, H5P_DEFAULT);

  /* Read the first line of the input file to get the number of samples. */
  infile = fopen (infilename, "r");
  row = malloc(100000);
  row = fgets (row, 100000, infile);
  outndx = 0;
  token = strtok(row, "\t");
  while ((token = strtok(NULL, "\t")))
    outndx++;
  int SampleCount = outndx + 1;
  fclose(infile);
  printf("Samples: %i\n", SampleCount);
  free(row);

  /* Read the whole file through to get the number of markers.  (wc -l?) */
  infile = fopen(infilename, "r");
  row = malloc(100000);
  int markernum = 0;
  while (fgets (row, 100000, infile) != NULL)
    markernum++;
  int MarkerCount = markernum;
  fclose(infile);
  printf("Markers: %i\n", MarkerCount);
  free(row);

  /*************************************/
  /* First dataset, normal orientation */

  /* Create the data space for the dataset. */
  dims[0] = MarkerCount;
  dims[1] = SampleCount;
  dataspace_id = H5Screate_simple(2, dims, NULL);

  /* Create the dataset. Each element is type datumtype. */
  dataset_id = H5Dcreate2(file_id, h5dataset, datumtype, dataspace_id,
                          H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

  /* Create a memory buffer space. */
  dimsmem[0] = 1;
  dimsmem[1] = SampleCount;
  memspace_id = H5Screate_simple(2, dimsmem, NULL);

  /* Create the hyperslab dimensions */
  offset[0] = 0; offset[1] = 0;
  stride[0] = 1; stride[1] = 1;
  count[0] = 1; count[1] = 1;
  blocksize[0] = 1; blocksize[1] = SampleCount;

  /* char dset_data[100000]; */
  char *dset_data = malloc(100000 * datumsize);
  rownum = 0;
  infile = fopen(infilename, "r");
  row = malloc(100000);
  while (fgets (row, 100000, infile)) {
    token = strtok(row, "\t");
    outndx = 0;
    int i = 0;
    while (token[i] != '\0') {
      dset_data[i] = token[i];
      i++;
    }
    while ((token = strtok(NULL, "\t\n\r"))) {
      /* Read the rest of the input line into dset_data[]. */
      ++outndx;
      i = 0;
      while (token[i] != '\0') {
	dset_data[(outndx * datumsize) + i] = token[i];
	i++;
      }
    }
    /* Adjust the hyperslab row. */
    offset[0] = rownum;
    status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
    /* Write the row. */
    status = H5Dwrite(dataset_id, datumtype, memspace_id, dataspace_id, H5P_DEFAULT, dset_data);
    ++rownum;
  }
  fclose(infile);
  free(dset_data);
  free(row);

  /* End access to the dataset and release resources used by it. */
  status = H5Dclose(dataset_id);

  /* End access to the data spaces. */
  status = H5Sclose(dataspace_id);
  status = H5Sclose(memspace_id);

  /******************************************/
  /* Second dataset, transposed orientation */

  /* Create the data space for the dataset. */
  dims[0] = SampleCount; 
  dims[1] = MarkerCount;
  dataspace_id = H5Screate_simple(2, dims, NULL);

  h5dataset ="allelematrix_samples-fast";
  /* Create the dataset. Each element is type CHAR. */
  dataset_id = H5Dcreate2(file_id, h5dataset, datumtype, dataspace_id, 
                          H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

  /* Load in 10,000-marker batches. */
  int batchsize = 10000;

  /* Create a memory buffer space. */
  dimsmem[0] = 1;
  dimsmem[1] = batchsize;
  memspace_id = H5Screate_simple(2, dimsmem, NULL);

 /* Create the initial hyperslab dimensions */
  offset[0] = 0; offset[1] = 0;
  stride[0] = 1; stride[1] = 1; 
  count[0] = 1; count[1] = 1;
  blocksize[0] = 1; blocksize[1] = batchsize;

  /* Declare the 2D array for reading the input file into. */
  char **batch_data = malloc(SampleCount * datumsize * sizeof(char *));
  for (i = 0; i < SampleCount * datumsize; i++)
    batch_data[i] = malloc(batchsize);
  /* Declare a row of the transposed array. */
  char sampledata[batchsize * datumsize];

  /* Read in the input file. */
  char *row2 = malloc(100000);
  FILE *infile2 = fopen (infilename, "r");
  rownum = 0;
  int batchcounter = 0;
  while (fgets (row2, 100000, infile2)) {
    token = strtok(row2, "\t");
    outndx = 0;
    for (i = 0; i < datumsize; i++)
      batch_data[i][batchcounter] = token[i];
    while ((token = strtok(NULL, "\t"))) {
      /* Read the rest of the input line into batch_data[]. */
      ++outndx;
      for (i = 0; i < datumsize; i++)
	batch_data[(outndx * datumsize) + i][batchcounter] = token[i];
    }
    if (batchcounter == batchsize - 1) {
      /* Adjust the hyperslab column. */
      offset[1] = (rownum - batchcounter) * datumsize;
      /* Transpose the array, one sample at a time, and write to the HDF5 file. */
      for (i = 0; i < SampleCount; i++) {
	for (j = 0; j < batchcounter; j++)
	  for (k = 0; k < datumsize; k++)
	    sampledata[(j * datumsize) + k] = batch_data[(i * datumsize) + k][j];
	offset[0] = i;
	status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
	status = H5Dwrite(dataset_id, datumtype, memspace_id, dataspace_id, H5P_DEFAULT, sampledata);
      }
      batchcounter = 0;
    }
    rownum++;
    batchcounter++;
  }
  /* At end of file. Now write out the remaining fraction of a batch. */
  dimsmem[1] = batchcounter;
  rmemspace_id = H5Screate_simple(2, dimsmem, NULL);
  offset[1] = (rownum - batchcounter) * datumsize;
  blocksize[1] = batchcounter;
  for (i = 0; i < SampleCount; i++) {
    for (j = 0; j < batchcounter; j++)
      for (k = 0; k < datumsize; k++)
        sampledata[(j * datumsize) + k] = batch_data[(i * datumsize) + k][j];
    offset[0] = i;
    status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
    status = H5Dwrite(dataset_id, datumtype, rmemspace_id, dataspace_id, H5P_DEFAULT, sampledata);
  }

  free(row2);
  fclose (infile2);

  /* End access to the dataset and release resources used by it. */
  status = H5Dclose(dataset_id);
  if (status < 0)
    return 2;
  /* End access to the data spaces. */ 
  status = H5Sclose(memspace_id);
  if (status < 0)
    return 3;
  status = H5Sclose(rmemspace_id);
  if (status < 0)
    return 4;
  status = H5Sclose(dataspace_id);
  if (status < 0)
    return 5;
  /* Close the file. */
  status = H5Fclose(file_id);
  if (status < 0)
    return 6;

  return 0;
}
