/* loadBeadXpress, DEM 13apr16, from loadDArTSeq.c, DEM 13apr16, ... 
   ... from example code h5_crtdat.c etc.  */

/* BeadXpress format, tab-separated, 1 line before the header, beginning
   with "Marker\Genotype".  One leading columns before allele calls, the
   marker name.  Coding like "G/G", "N/N". */

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
    printf("Example: %s /home/matthews/Data/Rice/BXP/GS0011861_ALCHEMY_PlusStrand_INGER_GDMS_Final_14Nov2013P_copy.csv /shared_data/HDF5/Rice/BXP_GS0011861.h5 GS0011861\n", argv[0]);
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
  /* First dataset, sample names: */
  /* Read the first line of the input file to get the sample names. */
  char *samplenames[100000];
  infile = fopen (infilename, "r");
  int r;
  /* Skip down to row 1. */
  for (r = 0; r < 2; r++)
    fgets (row, 1000000, infile);
  outndx = 0;
  /* Omit the leading column before the data (Marker\Genotype). */
  token = strtok(row, "\t");
  printf("first token: %s\n", token);
  if (strcmp(token, "Marker\\Genotype") != 0) {
    printf("Didn't find Marker\\Genotype header row, exiting.\n");
    return(0);
  }
  while ((token = strtok(NULL, "\t\n\r"))) {
    /* Read the rest of the input line into samplenames[]. */
    samplenames[outndx] = strdup(token);
    /* printf ("column %i, name = %s\n", outndx, samplenames[outndx]); */
    outndx++;
  }
  int SampleCount = outndx;
  fclose(infile);
  printf("%i Samples:\n%i: %s\n", SampleCount, 0, samplenames[0]);
  printf("%i: %s\n", 1, samplenames[1]);
  printf("%i: %s\n", SampleCount - 2, samplenames[SampleCount - 2]);
  printf("%i: %s\n\n", SampleCount - 1, samplenames[SampleCount - 1]);

  char *copy = strdup(h5dataset);
  char *sampleset = strcat(copy, "_samples");

  /* Set the datatype and size for sample names. */
  hid_t datatype_id = H5Tcopy(H5T_C_S1);
  status = H5Tset_size(datatype_id, H5T_VARIABLE);

  /* Create the data space for the dataset. */
  dim[0] = SampleCount;
  dataspace_id = H5Screate_simple(1, dim, NULL);

  /* Create the dataset. */
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


  /*********************************/
  /* Second dataset, marker names: */
  /* Read the marker names, column 0 */
  /* char *markernames[1000000000];  /\* dimension 1 billion *\/ */
  char *markernames[1000000];
  infile = fopen(infilename, "r");
  rownum = 0;
  int markernum = 0;
  while (fgets (row, 1000000, infile)) {
    token = strtok(row, "\t");
    /* Omit the header rows. */
    if (rownum > 1) {
      /* Use strdup to pass "token" by value instead of by reference. */
      markernames[markernum] = strdup(token);
      markernum++;
    }
    rownum++;
  }
  fclose(infile);

  int MarkerCount = markernum;
  printf("%i Markers:\n%i: %s\n", MarkerCount, 0, markernames[0]);
  printf("%i: %s\n", MarkerCount - 2, markernames[MarkerCount - 2]);
  printf("%i: %s\n", MarkerCount - 1, markernames[MarkerCount - 1]);
  /* int i; */
  /* for (i=0; i < MarkerCount; i++) { */
  /*   printf("%i: %s\n", i, markernames[i]); */
  /* } */

  /* Create the data space for the dataset. */
  dim[0] = MarkerCount;
  dataspace_id = H5Screate_simple(1, dim, NULL);

  /* Create the dataset. */
  copy = strdup(h5dataset);
  char *markerset = strcat(copy, "_markers");
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
  dimsmem[0] = 1; 
  dimsmem[1] = SampleCount;
  memspace_id = H5Screate_simple(2, dimsmem, NULL);

  /* Create the hyperslab dimensions */
  offset[0] = 0; offset[1] = 0;
  stride[0] = 1; stride[1] = 1; 
  count[0] = 1; count[1] = 1;
  blocksize[0] = 1; blocksize[1] = SampleCount; 

  char dset_data[100000];
  char iupac(char *);  /* Declare subroutine, defined below. */
  rownum = 0;
  infile = fopen(infilename, "r");
  while (fgets (row, 1000000, infile)) {
    /* Omit the leading column before the data (marker name). */
    token = strtok(row, "\t");
    /* Omit the header rows. */
    if (rownum > 1) {
      outndx = 0;
      while ((token = strtok(NULL, "\t\n\r"))) {
	/* Read the rest of the input line into dset_data[]. */
	dset_data[outndx] = iupac(token);
	++outndx;
      }
      /* Adjust the hyperslab row. */
      offset[0] = rownum - 2;
      status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
      /* Write the dataset. */
      status = H5Dwrite(dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, dset_data);
      /* Echo to stdout. */
      /* int i; */
      /* for (i = 0; i < SampleCount; i++) { */
      /* 	printf("%c", dset_data[i]); */
      /* } */
      /* printf("\n"); */
    }
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

/* Translate DArTSeq scores to one-character IUPAC "ambiguity" code. */
char iupac(char *alleles) {
  if (strcmp(alleles, "A/A") == 0)
    return('A');  /* Note, single-quotes are for char, double-quotes for char *. */
  if (strcmp(alleles, "C/C") == 0)
    return('C');
  if (strcmp(alleles, "G/G") == 0)
    return('G');
  if (strcmp(alleles, "T/T") == 0)
    return('T');
  if (strcmp(alleles, "A/G") == 0 || strcmp(alleles, "G/A") == 0)
    return('R');
  if (strcmp(alleles, "C/T") == 0 || strcmp(alleles, "T/C") == 0)
    return('Y');
  if (strcmp(alleles, "C/G") == 0 || strcmp(alleles, "G/C") == 0)
    return('S');
  if (strcmp(alleles, "A/T") == 0 || strcmp(alleles, "T/A") == 0)
    return('W');
  if (strcmp(alleles, "G/T") == 0 || strcmp(alleles, "T/G") == 0)
    return('K');
  if (strcmp(alleles, "A/C") == 0 || strcmp(alleles, "C/A") == 0)
    return('M');
  if (strcmp(alleles, "N/N") == 0)
    return('N');
  /* If it's anything else, flag it. */
  printf("Error: alleles = %s\n\n", alleles);
  return('?');
}

