/* loadMABC_KASP.c, DEM 14apr16, from loadQCKASP.c, DEM 14apr2016, from ...
   from example code h5_crtdat.c etc.  */

/* KASP format(?), tab-separated, 5 lines before the header, beginning
   with "PROJECT".  Two leading columns before allele calls.  Coding like
   "G:G", "?".  Columns are markers, rows are samples(!!). */

#include "hdf5.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>

int main(int argc, char *argv[]) {

  /* HDF5 variables */
  hid_t       file_id, dataset_id, dataspace_id, memspace_id;  /* identifiers */
  hsize_t     dim[1], dimmem[1], dims[2], dimsmem[2];;
  herr_t      status;
  /* Hyperslab dimensions */
  hsize_t    offset[2], stride[2], count[2], blocksize[2];

  FILE *infile;
  char *token;
  int rownum, outndx;
  char *row;
  row = malloc(1000000);

  if (argc < 3) {
    printf("Usage: %s <input file> <output file> <dataset name>\n", argv[0]);
    printf("Example: %s /shared_data/cimmyt_data/Maize/MABC/Genotyping-903.497-01-grid_CK14MBC031.csv /shared_data/HDF5/Maize/MABC_KASP_CK14MBC031.h5 MABC_KASP\n", argv[0]);
    return 0;
  }

  char *infilename = argv[1];
  char *h5file = argv[2];
  char *h5dataset = argv[3];

  /* Create a new HDF5 file using default properties. */
  file_id = H5Fcreate(h5file, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);
  /* Open the file.  */
  file_id = H5Fopen(h5file, H5F_ACC_RDWR, H5P_DEFAULT);

  /********************************/
  /* First dataset, marker names: */
  /* Read the 6th line of the input file to get the marker names. */
  char *markernames[100000];
  int r;
  infile = fopen (infilename, "r");
  /* Skip to line 6. */
  for (r = 0; r < 6; r++)
    fgets (row, 1000000, infile);
  outndx = 0;
  token = strtok(row, "\t");
  printf("first token: %s\n", token);
  if (strcmp(token, "PROJECT") != 0) {
    printf("Didn't find PROJECT header row, exiting.\n");
    return(0);
  }
  while ((token = strtok(NULL, "\t\n\r"))) {
    /* Omit the two leading columns before the data (PROJECT, SubjectID). */
    if (outndx > 0) {
      /* Read the rest of the input line into dset_data[]. */
      markernames[outndx - 1] = strdup(token);
      /* printf ("row %i, name = %s\n", outndx, markernames[outndx - 10]); */
    }
    outndx++;
  }
  int MarkerCount = outndx - 1;
  fclose(infile);
  printf("%i Markers:\n%i: %s\n", MarkerCount, 0, markernames[0]);
  printf("%i: %s\n", 1, markernames[1]);
  printf("%i: %s\n", MarkerCount - 2, markernames[MarkerCount - 2]);
  printf("%i: %s\n\n", MarkerCount - 1, markernames[MarkerCount - 1]);

  char *copy = strdup(h5dataset);
  char *markerset = strcat(copy, "_markers");

  /* Set the datatype and size for marker names. */
  hid_t datatype_id = H5Tcopy(H5T_C_S1);
  status = H5Tset_size(datatype_id, H5T_VARIABLE);

  /* Create the data space for the dataset. */
  dim[0] = MarkerCount;
  dataspace_id = H5Screate_simple(1, dim, NULL);

  /* Create the dataset. */
  dataset_id = H5Dcreate2(file_id, markerset, datatype_id, dataspace_id,
                          H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

  /* Create a memory buffer space. */
  dimmem[0] = MarkerCount;
  memspace_id = H5Screate_simple(1, dimmem, NULL);

  /* Create the hyperslab dimensions */
  offset[0] = 0; 
  stride[0] = 1; 
  count[0] = 1; 
  blocksize[0] = MarkerCount; 
  status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
  /* Write the dataset. */
  status = H5Dwrite(dataset_id, datatype_id, memspace_id, dataspace_id, H5P_DEFAULT, markernames);


  /*********************************/
  /* Second dataset, sample names: */
  char *samplenames[100000];
  infile = fopen(infilename, "r");
  /* Skip to line 6. */
  for (r = 0; r < 6; r++)
    fgets (row, 1000000, infile);
  int samplenum = 0;
  while (fgets (row, 1000000, infile)) {
    /* Read the sample names, second column */
    token = strtok(row, "\t");
    token = strtok(NULL, "\t");
    /* Use strdup to pass "token" by value instead of by reference. */
    samplenames[samplenum] = strdup(token);
    samplenum++;
  }
  fclose(infile);

  int SampleCount = samplenum;
  printf("%i Samples:\n", samplenum);
  printf("%i: %s\n", 0, samplenames[0]);
  printf("%i: %s\n", SampleCount - 2, samplenames[SampleCount - 2]);
  printf("%i: %s\n", SampleCount - 1, samplenames[SampleCount - 1]);
  /* int i; */
  /* for (i=0; i < SampleCount; i++) { */
  /*   printf("%i: %s\n", i, samplenames[i]); */
  /* } */

  /* Create the data space for the dataset. */
  dim[0] = SampleCount;
  dataspace_id = H5Screate_simple(1, dim, NULL);

  /* Create the dataset. */
  copy = strdup(h5dataset);
  char *sampleset = strcat(copy, "_samples");
  dataset_id = H5Dcreate2(file_id, sampleset, datatype_id, dataspace_id,
                          H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

  /* Create a memory buffer space. */
  dimmem[0] = SampleCount;
  memspace_id = H5Screate_simple(1, dimmem, NULL);

  /* Create the hyperslab dimensions */
  offset[0] = 0; 
  stride[0] = 1; 
  count[0] = 1; 
  blocksize[0] = SampleCount; 
  status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
  /* Write the dataset. */
  status = H5Dwrite(dataset_id, datatype_id, memspace_id, dataspace_id, H5P_DEFAULT, samplenames);


  /**********************************/
  /* Main dataset, the allele calls */
  /* Create the data space for the dataset. */
  dims[0] = MarkerCount;
  dims[1] = SampleCount; 
  dataspace_id = H5Screate_simple(2, dims, NULL);

  /* Create the dataset. Each element is type CHAR. */
  dataset_id = H5Dcreate2(file_id, h5dataset, H5T_NATIVE_CHAR, dataspace_id, 
                          H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

  /* Create a memory buffer space. */
  dimsmem[0] = MarkerCount;
  dimsmem[1] = 1;
  memspace_id = H5Screate_simple(2, dimsmem, NULL);

  /* Create the hyperslab dimensions */
  offset[0] = 0; offset[1] = 0;
  stride[0] = 1; stride[1] = 1; 
  count[0] = 1; count[1] = 1;
  blocksize[0] = MarkerCount; blocksize[1] = 1;

  char dset_data[100000];
  char iupac(char *);  /* Declare subroutine, defined below. */
  rownum = 0;
  infile = fopen(infilename, "r");
  /* Skip six lines of header. */
  for (r = 0; r < 6; r++)
    fgets (row, 1000000, infile);
  while (fgets (row, 1000000, infile)) {
    token = strtok(row, "\t");
    outndx = 0;
    /* Strip \n and possible \r from the end of the last token. */
    while ((token = strtok(NULL, "\t\n\r"))) {
      /* Omit the two leading columns before the data (PROJECT, SubjectID). */
      if (outndx > 0) {
	/* Read the rest of the input line into dset_data[]. */
	dset_data[outndx - 1] = iupac(token);
      }
      ++outndx;
    }
    /* Adjust the hyperslab row. */
    offset[1] = rownum;
    status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
    /* Write the dataset. */
    status = H5Dwrite(dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, dset_data);

    /* Echo to stdout. */
    /* int i; */
    /* for (i = 0; i < MarkerCount; ++i) { */
    /* 	printf("%c", dset_data[i]); */
    /* } */
    /* printf("\n"); */

    ++rownum;
  }
  fclose(infile);
  free(row);

  /* End access to the dataset and release resources used by it. */
  status = H5Dclose(dataset_id);

  /* End access to the data space. */ 
  status = H5Sclose(dataspace_id);

  /* Close the datatype. */
  status = H5Tclose(datatype_id);

  /* Close the file. */
  status = H5Fclose(file_id);

  if (status) return 0;
  else return 1;
}

/* Translate scores to one-character IUPAC "ambiguity" code. */
char iupac(char *alleles) {
  if (strcmp(alleles, "A:A") == 0)
    return('A');  /* Note, single-quotes are for char, double-quotes for char *. */
  if (strcmp(alleles, "C:C") == 0)
    return('C');
  if (strcmp(alleles, "G:G") == 0)
    return('G');
  if (strcmp(alleles, "T:T") == 0)
    return('T');
  if (strcmp(alleles, "A:G") == 0 || strcmp(alleles, "G:A") == 0)
    return('R');
  if (strcmp(alleles, "C:T") == 0 || strcmp(alleles, "T:C") == 0)
    return('Y');
  if (strcmp(alleles, "C:G") == 0 || strcmp(alleles, "G:C") == 0)
    return('S');
  if (strcmp(alleles, "A:T") == 0 || strcmp(alleles, "T:A") == 0)
    return('W');
  if (strcmp(alleles, "G:T") == 0 || strcmp(alleles, "T:G") == 0)
    return('K');
  if (strcmp(alleles, "A:C") == 0 || strcmp(alleles, "C:A") == 0)
    return('M');
  if (strcmp(alleles, "?") == 0)
    return('N');
  if (strcmp(alleles, "Bad") == 0)
    return('N');
  if (strcmp(alleles, "") == 0)
    return('N');
  /* If it's anything else, flag it. */
  printf("Error: alleles = %s\n", alleles);
  return('?');
}
