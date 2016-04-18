# Piloted-Clarke-and-Wright
A piloted implementation of the Clarke and Wright's savings algorithm on TSP.

The algorithm itself is in /src/dii/vrp/tp/PilotedClarkeAndWright.java

### Instalation
* Create a java project with the sources
* In the class TClarkeAndWright edit the path variable to point to the CMT data files
* Change the k variable to what you want 
```sh
$ cw.setK(10);
```
* Run the main in TClarkeAndWright

### Result
Here is a small example on CMT instances 1 and 2.

|    CMT             |    01     | 01         | 02        | 02         |
|--------------------|-----------|------------|-----------|------------|
|    K               |    CPU    |    Cost    |    CPU    |    Cost    |
|    0 (no pilot)    |    35     |    689     |    55     |    1023    |
|    100             |    37     |    579     |    69     |    891     |
|    1000            |    49     |    599     |    88     |    895     |


After a benchmark on 14 instances with K=100, 10% cost reduction for 10% more CPU time.
