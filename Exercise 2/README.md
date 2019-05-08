# Assignment 2

## Exercise 1: Map Reduce

### Setup

```bash
mvn clean package
```
This will create the jar file into the `target/MapReduce-1.0-SNAPSHOT.jar`

#### Copy jar file to server

We used "SFTP Net Drive 2017" to have local access to remote SSH server. We copy-paste from target/MapReduce-1.0-SNAPSHOT.jar to SSH server.

#### Run the map reduce job for task A

```bash
hadoop jar [Jar-file.jar] [Driver] [HDFS input path] [HDFS output path]
```
Example:
```bash
hadoop jar MapReduce-1.0-SNAPSHOT.jar MapReduce.Task_A.CheckOutCount /user/adbs/2019S/shared/seattle-checkouts-by-title/checkouts-by-title.csv /user/e11846033f/TaskA
```

#### Run the map reduce job for task B

```bash
hadoop jar [Jar-file.jar] [Driver] [HDFS input1 path] [HDFS input2 path] [HDFS output path]
```
Example:
```bash
hadoop jar MapReduce-1.0-SNAPSHOT.jar MapReduce.Task_B.CheckOutCountWithLocation /user/adbs/2019S/shared/seattle-checkouts-by-title/checkouts-by-title.csv /user/adbs/2019S/shared/seattle-library-collection-inventory/library-collection-inventory.csv /user/e11846033f/TaskB
```

#### Copying output from HDFS to local file system

```bash
hadoop fs -copyToLocal [Output file location]
```
Example 1:
```bash
hadoop fs -copyToLocal TaskA/part-r-00000
```
Example 2:
```bash
hadoop fs -copyToLocal TaskB/part-r-00000
```

Input path can be one of:
* `checkouts-by-title.csv` - contains all checkouts by title
* `library-collection-inventory.csv` - contains all library collection inventory
* `checkouts-by-title-abbr.csv` - contains part of checkouts by title dataset
* `library-collection-inventory-abbr.csv` - contains part of library collection inventory

### Instructions

### Extra
