#### Usage

The script is called with nine (9) parameters  on command line.

`python loadVariantMatrix.py <DatasetName> <Dataset_Identifier.matrix> <Dataset_Identifier.marker_id> <Dataset_Identifier.dnarun_id> <hostname> <port> <dbuser> <dbpass> <dbname>`

<Dataset_Identifier.variant> The file containing N X M matrix of variant calls. File has no header.   
<Dataset_Identifier.marker_id> The marker IDs for the markers from the database. File has no header.   
<Dataset_Identifier.dnarun_id> The dnarun IDs for the dnaruns from the database.  File has a header "dnarun_id"

The Dataset is used to name MonetDB table.  
If the same table name already exists in database, it would be dropped and reloaded with data from the files. Loading the same files/files with same name, would drop the existing table and reload the data.


`<hostname>` The server name where database is hosted. The host should be reachable from the calling server.  
`<port>` The port number where database is hosted. The port should be open and listening for connections.  
`<dbuser>` The database user name.The user should have create privileges to create MonetDB tables.  
`<dbpass>` The database user password.  
`<dbname>` The database name.  

 
Data files must have absolute path. 

The script concatenates <Dataset_Identifier.marker_id> & <Dataset_Identifier.martix> file to create a resultant file <Dataset_Identifier.monetmp> in same location where <Dataset_Identifier.martix> is present. This resultant file is loaded into MonetDB. OS user running the script must have rights to create file in the directory.

 
#### Error Handling

Currently script only performs minimal error checks. All errors are reported to standard error output.

Script prompts an error message and exits with status 1, if called with less parameters.

Script prompts an error message and exits with status 1, if **data files extensions** are not correct.

All database and OS errors are reported as it is on standar error output.
