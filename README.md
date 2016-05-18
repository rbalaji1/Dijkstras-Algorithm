# Dijkstras-Algorithm
Implementation of the shortest path algorithm of dijsktras

——————————————————————————————————————————————————————————————
Ragavendran Balaji
rbalaji1@uncc.edu
800853917

Dijkstra’s Algorithm
————————————————————————————————
	README
————————————————————————————————

1: FOLDER INFORMATION:

-	The zip file contains 1 java file. Graph.java
-	This java file is the modified version of the previously provided Graph.java 	file which includes the unweighted shortest path code.
-	This modified Graph.java contains new methods to satisfy the requirements of 	the project


2: Compiling and execution:

-	To compile the  program in linux or mac simply use terminal

command:
“javac Graph.java” 

and to run the program

command:
“java Graph <input txt file that has the initial graph details>”


you can also use eclipse to run the program.

3: Program Design:

-	The program contains 4 class
	1:	class Vertex  —> represents the vertex in the graph
	2:	class Edge    —> represents the weighted edge in the graph
	3:	class MinHeap —> binary min-heap impelentation 
	4:	class Graph   —> contains all the required methods for the project


- class MinHeap

 * Binary Min-Heap implementation for extracting the vertex with min transmisison time from the queue
 * public void insert(double x)             --> insert the double value into the min-heap
 * public void increasekey(int s, double x) --> increase the priority
 * public int parent(int s)                 --> returns the parent index
 * public int left(int s)                   --> returns the left child index
 * public int right(int s)                  --> returns the right chile index
 * public double extractMin()               --> extracts the min element that is the root element key[1]
 * public void minHeapify(int x)            --> maintains the properties of the heap
 * public int indexof(double x)             --> returns index of the key value
 * public void print()                      --> prints the heap


- class Graph

additional methods apart from those provided earlier
* public void addedge(String sourceName, String destName, double time) --> add a new edge
* public void deleteedge(String tail, String head)             --> delete an edge
* public void edgeupdate(String tail, String head, int status) --> update the status of an edge
* public void vertexupdate(String ver, int status)             --> update the status of a vertex
* public void dijkstras(String s, String d)                    --> dijkstras algorithm for shortest path
* void pathprint(Vertex d)                                     --> print the shortest path of the vertex d
* public void printsortedge(List<Edge> ed)              	   --> print edges in the List in alphabetically sorted order
* public void print()                                          --> print the graph in alphabetically sorted order
* public void reachableBFS()                                   --> print vertices reachable from each vertex


4: The reachable algorithm
* find all the vertices reachable from a vertex using BFS (Breadth First Search) and
*  print it for every vertex in the graph.
* BFS algorithm is used here and the status of the vertices and edges are checked to see if they are
* DOWN or UP, if DOWN ignore the edge or vertex and continue
* Since the algorithm is BFS its complexity will be O(|V| + |E|)

