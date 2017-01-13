/* loadInfinium2.c, 27may2016, from loadMSWAMMY.c, DEM 12apr2016, from loadSEED.c, DEM 6apr2016, 
   ... from example code h5_crtdat.c etc.  */
/* An Infinium 6K dataset */
/* Loads the transposed data too, dataset allelematrix_samplesfast. */
/* Shards of the batching code are present but batching isn't actually 
   implemented, not needed since the file has < 10000 markers. */

#include "hdf5.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>

int main(int argc, char *argv[]) {

  /* HDF5 variables */
  hid_t       file_id, dataset_id, dataspace_id, memspace_id;  /* identifiers */
  hsize_t     dims[2], dimsmem[2];;
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
    printf("Example: %s /shared_data/IRRI/Infinium6K/34-GSL-INF_MSWAMMY/34_GSL-INF_MSWAMMY_P1_P6_23OCT2015_FSFinalReport.txt /shared_data/HDF5/Rice/34-GSL-INF_MSWAMMY.h5\n", argv[0]);
    /* printf("Example: %s /home/matthews/Data/Rice/Infinium/34_GSL-INF_MSWAMMY_P1_P6_23OCT2015_FSFinalReport.ifl /shared_data/HDF5/Rice/34-GSL-INF_MSWAMMY.h5\n", argv[0]); */
    return 0;
  }

  char *infilename = argv[1];
  char *h5file = argv[2];
  int headerrow = 9;  /* counting from 0 */

  /********************************/
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

  /*********************************/
  /* Read the marker names, column 0 */
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
  /* } */

  /**********************************/
  /* Read the allele calls. */
  /* First dataset, normal orientation ("sites-fast") */

  /* Create a new HDF5 file using default properties. */
  file_id = H5Fcreate(h5file, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);
  /* Open the file.  */
  file_id = H5Fopen(h5file, H5F_ACC_RDWR, H5P_DEFAULT);

  /* Create the data space for the dataset. */
  dims[0] = MarkerCount;
  dims[1] = SampleCount; 
  dataspace_id = H5Screate_simple(2, dims, NULL);

  /* Create the dataset. Each element is type CHAR. */
  char *h5dataset = "allelematrix";
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
      /* printf("rownum = %i: %s\n", rownum, token); */
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
      /* for (i = 0; i < SampleCount; ++i) { */
      /* 	printf("%c", dset_data[i]); */
      /* } */
      /* printf("\n"); */
    }
    rownum++;
  }
  fclose(infile);
  free(row);

  /******************************************/
  /* Second dataset, transposed orientation */

  /* Create the data space for the dataset. */
  dims[0] = SampleCount; 
  dims[1] = MarkerCount;
  dataspace_id = H5Screate_simple(2, dims, NULL);

  /* Create the dataset. Each element is type CHAR. */
  h5dataset = "allelematrix_samples-fast";
  dataset_id = H5Dcreate2(file_id, h5dataset, H5T_NATIVE_CHAR, dataspace_id, 
                          H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

  /* Load in 10,000-marker batches. */
  int batchsize = 10000;

  /* Create a memory buffer space. */
  /* dimsmem[0] = SampleCount; */
  dimsmem[0] = 1;
  dimsmem[1] = batchsize; 
  memspace_id = H5Screate_simple(2, dimsmem, NULL);

  /* Create the initial hyperslab dimensions */
  offset[0] = 0; offset[1] = 0;
  stride[0] = 1; stride[1] = 1; 
  count[0] = 1; count[1] = 1;
  /* blocksize[0] = SampleCount; blocksize[1] = batchsize;  */
  blocksize[0] = 1; blocksize[1] = batchsize; 

  /* Declare the 2D array for reading the input file into. */
  /* char **batch_data = malloc((SampleCount+1) * sizeof(char *)); */
  /* int k; */
  /* for (k = 0; k < SampleCount+1; k++) */
  /*   batch_data[k] = malloc(batchsize); */
  char batch_data[SampleCount][batchsize];

  char *row2 = (char *) malloc(100000);
  FILE *infile2 = fopen (infilename, "r");
  if (infile2 == NULL)
    return errno;
  rownum = 0;
  int batchcounter = 0;
  /* Read in the input file. */
  while (fgets (row2, 100000, infile2)) {
    if (rownum > headerrow) {
      /* Skip the first column, marker name, */
      token = strtok(row2, "\t");
      outndx = 0;
      while ((token = strtok(NULL, "\t\n\r"))) {
	/* Read the rest of the input line into batch_data[]. */
 	batch_data[outndx][batchcounter] = iupac(token);
	++outndx;
      }
      batchcounter++;
    }
    rownum++;
  }

	/* For debugging. Echo this data row to stdout. */
	for (i = 0; i < SampleCount; i++)
		printf("%c", batch_data[i][batchcounter-1]);
	printf("\n");

  /* At end of file. Now write out the remaining fraction of a batch. */
  dimsmem[1] = batchcounter;
  hid_t rmemspace_id;
  rmemspace_id = H5Screate_simple(2, dimsmem, NULL);
  /* offset[1] = rownum - (batchcounter); */
  blocksize[1] = batchcounter;

  char sampledata[SampleCount];
  int j;

  for (i = 0; i < SampleCount; i++) {
    for (j = 0; j < batchcounter; j++)
      sampledata[j] = batch_data[i][j];
    offset[0] = i;
    status = H5Sselect_hyperslab(dataspace_id, H5S_SELECT_SET, offset, stride, count, blocksize);
    status = H5Dwrite(dataset_id, H5T_NATIVE_CHAR, rmemspace_id, dataspace_id, H5P_DEFAULT, sampledata);
  }

  /* for (i = 0; i < SampleCount; i++) { */
  /*   for (j = 0; j < 100; j++)  */
  /*   /\* for (i = 0; i < SampleCount; i++) *\/ */
  /*     printf("%c", batch_data[i][j]); */
  /*   printf("\n"); */
  /* } */


  free(row2);
  fclose (infile2);

  /*********/

  /* End access to the dataset and release resources used by it. */
  status = H5Dclose(dataset_id);

  /* End access to the data space. */ 
  status = H5Sclose(dataspace_id);

  /* Close the datatype. */
  /* status = H5Tclose(datatype_id); */

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
  if (strcmp(alleles, "--") == 0 || strcmp(alleles, "-") == 0)
    return('N');
  if (strcmp(alleles, "NN") == 0 || strcmp(alleles, "N") == 0)
    return('N');
  /* If it's anything else, flag it. */
  printf("Error: alleles = %s\n", alleles);
  return('?');
}
