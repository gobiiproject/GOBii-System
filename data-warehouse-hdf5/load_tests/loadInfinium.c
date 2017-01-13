/* loadMSWAMMY.c, DEM 12apr2016, from loadSEED.c, DEM 6apr2016, from ...
   ... from example code h5_crtdat.c etc.  */
/* An Infinium 6K dataset */

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
    printf("Example: %s /shared_data/IRRI/Infinium6K/34-GSL-INF_MSWAMMY/34_GSL-INF_MSWAMMY_P1_P6_23OCT2015_FSFinalReport.txt /shared_data/HDF5/rice.h5 34_GSL-INF_MSWAMMY\n", argv[0]);
    return 0;
  }

  char *infilename = argv[1];
  char *h5file = argv[2];
  char *h5dataset = argv[3];
  int headerrow = 9;  /* counting from 0 */

  /* Create a new HDF5 file using default properties. */
  file_id = H5Fcreate(h5file, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);
  /* Open the file.  */
  file_id = H5Fopen(h5file, H5F_ACC_RDWR, H5P_DEFAULT);

  /********************************/
  /* First dataset, sample names: */
  /* Read the first line of the input file to get the sample names. */
  char *samplenames[10000];
  infile = fopen (infilename, "r");
  rownum = 0;
  outndx = 0;
  while (fgets (row, 1000000, infile)) {
    token = strtok(row, "\t");
    if (rownum == headerrow) {
      samplenames[outndx] = strdup(token);
      while ((token = strtok(NULL, "\t\n\r"))) {
	/* Read the rest of the input line into samplenames[]. */
	outndx++;
	samplenames[outndx] = strdup(token);
      }
    }
    rownum++;
  }
  
  int SampleCount = outndx + 1;
  fclose(infile);
  printf("Samples\n0: %s\n", samplenames[0]);
  printf("1: %s\n", samplenames[1]);
  printf("%i: %s\n\n", SampleCount - 1, samplenames[SampleCount - 1]);

  /* char *sampleset = strcat(h5dataset, "samples\0"); */
  char *sampleset = "34_GSL-INF_MSWAMMY_samples";

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
    if (rownum > headerrow) {
      /* Use strdup() to pass "token" by value instead of by reference. */
      markernames[markernum] = strdup(token);
      markernum++;
    }
    rownum++;
  }
  fclose(infile);

  int MarkerCount = markernum;
  printf("Markers\n%i: %s\n", 0, markernames[0]);
  printf("%i: %s\n", 1, markernames[1]);
  printf("%i: %s\n\n", MarkerCount - 1, markernames[MarkerCount - 1]);
  /* int i; */
  /* for (i=0; i < MarkerCount; i++) { */
  /*   printf("%i: %s\n", i, markernames[i]); */
  /*   printf("ttest %i: %s\n\n", i, tst[i]); */
  /* } */

  /* Create the data space for the dataset. */
  dim[0] = MarkerCount;
  dataspace_id = H5Screate_simple(1, dim, NULL);

  /* Create the dataset. */
  char *markerset = "34_GSL-INF_MSWAMMY_markers";
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

  char dset_data[10000];
  char iupac(char *);  /* Declare subroutine, defined below. */
  int i;
  rownum = 0;
  infile = fopen(infilename, "r");
  while (fgets (row, 1000000, infile)) {
    /* Omit the header rows. */
    if (rownum > headerrow) {
      /* Skip over the first token, the marker name. */
      token = strtok(row, "\t");
      printf("rownum = %i: %s\n", rownum, token);
      outndx = 0;
      /* Include \n and \r as delimiters to strip the (DOS) linebreak from the last token. */
      while ((token = strtok(NULL, "\t\n\r"))) {
	dset_data[outndx] = iupac(token);
	++outndx;
      }
      /* Adjust the hyperslab row. */
      offset[0] = rownum - headerrow - 1;
      status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
      /* Write the dataset. */
      status = H5Dwrite(dataset_id, H5T_NATIVE_CHAR, memspace_id, dataspace_id, H5P_DEFAULT, dset_data);
      /* Echo to stdout. */
      for (i = 0; i < SampleCount; ++i) {
      	printf("%c", dset_data[i]);
      }
      printf("\n");
    }
    rownum++;
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

/* Translate pairs of nucleotides (diploid) to one-character IUPAC "ambiguity" code. */
char iupac(char *alleles) {
  if (strcmp(alleles, "AA") == 0)
    return('A');  /* Note, single-quotes are for char, double-quotes for char *. */
  if (strcmp(alleles, "CC") == 0)
    return('C');
  if (strcmp(alleles, "GG") == 0)
    return('G');
  if (strcmp(alleles, "TT") == 0)
    return('T');
  if (strcmp(alleles, "AG") == 0 || strcmp(alleles, "GA") == 0)
    return('R');
  if (strcmp(alleles, "CT") == 0 || strcmp(alleles, "TC") == 0)
    return('Y');
  if (strcmp(alleles, "CG") == 0 || strcmp(alleles, "GC") == 0)
    return('S');
  if (strcmp(alleles, "AT") == 0 || strcmp(alleles, "TA") == 0)
    return('W');
  if (strcmp(alleles, "GT") == 0 || strcmp(alleles, "TG") == 0)
    return('K');
  if (strcmp(alleles, "AC") == 0 || strcmp(alleles, "CA") == 0)
    return('M');
  if (strcmp(alleles, "++") == 0 || strcmp(alleles, "+") == 0)
    return('+');
  if (strcmp(alleles, "+-") == 0 || strcmp(alleles, "-+") == 0)
    return('O');
  /* For Illumina Infinium, apparently "--" means missing data. */
  /* if (strcmp(alleles, "--") == 0 || strcmp(alleles, "-") == 0) */
  /*   return('-'); */
  if (strcmp(alleles, "--") == 0 || strcmp(alleles, "-") == 0)
    return('N');
  if (strcmp(alleles, "NN") == 0 || strcmp(alleles, "N") == 0)
    return('N');
  /* If it's anything else, flag it. */
  printf("Error: alleles = %s\n\n", alleles);
  return('?');
}
