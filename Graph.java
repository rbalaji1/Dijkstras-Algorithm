/*******************************************
 * Name : Ragavendran Balaji
 * email: rbalaji1@uncc.edu
 *
 * DIJKSTRAS ALGORITHM
 * updated graph.java file with new methods
 * ******************************************
 */

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;



// Used to signal violations of preconditions for
// various shortest path algorithms.
class GraphException extends RuntimeException
{
    
    public GraphException( String name )
    {
        super( name );
    }
}

// Represents a vertex in the graph.
class Vertex
{
    public String     name;   // Vertex name
    public List<Edge> adj;    // Adjacent list of edges that have the vertices
    public Vertex     prev;   // Previous vertex on shortest path
    public double        dist;   // Distance of path
    public int stat;		  // status tag for the vertex
    public int color;	// color of the vertex for use in DFS reachable W(white), G(grey), B(black)
    
    public Vertex( String nm )
    { name = nm; adj = new LinkedList<Edge>( ); reset( ); stat = 1; color = Graph.W; }
    
    public void reset( )
    { dist = Graph.INFINITY; prev = null; }
    
}

//Edge class to represent the weighted edge
class Edge
{
    public Vertex vertex;		//name of the vertex on the other end
    public double time;		// transmission time
    public int stat;		// to set the status as up = 1 or down = 0
    
    public Edge(Vertex n, double t){
        vertex = n;
        time = t;
        stat = 1;
    }
    
}


/*
 * -----------------------------------------------------------------------------------------------------------
 */

/**
 * Codes written by Ragavendran Balaji
 */


/**
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
 */

class MinHeap{
    
    public static final int INFINITY = Integer.MAX_VALUE;
    public double[] key;			// the array of keys (in this case these will be the transmission time)
    public int size;				// size of the array
    
    public MinHeap(int s){		// constructor to initialize the array and size
        
        key = new double[s + 1];
        size = 0;
    }
    
    /**
     * method to insert an element to the heap
     */
    public void insert(double x){
        
        if(size == key.length)
            throw new NoSuchElementException( "overflow" );
        size = size + 1;
        key[size] = INFINITY;
        increasekey(size, x);
        
    }
    
    /**
     *  heap increase key (increase priority)
     */
    public void increasekey(int s, double x){
        
        double temp;
        
        key[s] = x;
        while( s > 1 && key[s] < key[parent(s)]){		// check if current key is less than the parent and then switch if necessary
            temp = key[s];
            key[s] = key[parent(s)];
            key[parent(s)] = temp;
            s = parent(s);
        }
        
    }
    
    public int parent(int s){		// returns parent index
        return s/2;
    }
    public int left(int s){		// return left child index
        return 2*s;
    }
    public int right(int s){	// returns right child index
        return 2*s + 1;
    }
    
    /**
     * method to extract the min element. here it will be the root element
     */
    public double extractMin(){
        
        double min;
        if(size < 1)
            throw new NoSuchElementException( "heap underflow" );
        min = key[1];
        key[1] = key[size];
        size = size - 1;
        minHeapify(1);			// used to rearrange the array to satisfy the heap property
        return min;
    }
    
    /**
     *  method to maintain the min heap property (itterative)
     */
    public void minHeapify(int x){
        
        int small;
        double temp;
        int l, r;
        int i = x;
        while(i < size){		// itterative method for min heap
            l = left(i);
            r = right(i);
            if(l <= size && key[l] < key[i])	// check if left child is small
                small = l;
            else
                small = i;
            if(r <= size && key[r] < key[small])	//check if right child is small
                small = r;
            
            if(small != i){		//switch
                temp = key[i];
                key[i] = key[small];
                key[small] = temp;
                i = small;
            }
            else
                break;
        }
        
    }
    
    /**
     * Return the index of a key
     */
    public int indexof(double x){
        int index = 0;
        for(int i=0; i<size; i++){
            if(key[i] == x)
                index = i;
        }
        return index;
    }
    
    /**
     * method to print the heap
     */
    public void print(){
        System.out.print("HEAP :  ");
        for(int i=1; i<size + 1; i++){
            System.out.print(key[i] + " ");
        }
        System.out.println();
    }
}

/*
 * -------------------------------------------------------------------------------------------------------------------------------------------
 */


/* Graph class: evaluate shortest paths.
*
* CONSTRUCTION: with no parameters.
*
* ******************PUBLIC OPERATIONS**********************
* void addEdge( String v, String w )
*                             --> Add additional edge
* void printPath( String w )   --> Print path after alg is run
* void unweighted( String s )  --> Single-source unweighted
* ******************ERRORS*********************************
* Some error checking is performed to make sure graph is ok,
* and to make sure graph satisfies properties needed by each
* algorithm.  Exceptions are thrown if errors are detected.


* Methods written by Ragavendran Balaji
* public void addedge(String sourceName, String destName, double time) --> add a new edge
* public void deleteedge(String tail, String head)             --> delete an edge
* public void edgeupdate(String tail, String head, int status) --> update the status of an edge
* public void vertexupdate(String ver, int status)             --> update the status of a vertex
* public void dijkstras(String s, String d)                    --> dijkstras algorithm for shortest path
* void pathprint(Vertex d)                                     --> print the shortest path of the vertex d
* public void printsortedge(List<Edge> ed)              --> print edges in the List in alphabetically sorted order
* public void print()                                          --> print the graph in alphabetically sorted order
* public void reachableBFS()                                   --> print vertices reachable from each vertex
*/

public class Graph
{
    public static final int INFINITY = Integer.MAX_VALUE;
    public static final int DOWN = 0;
    public static final int UP = 1;
    public static final int W = 0;
    public static final int G = 1;
    public static final int B = 2;
    private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );

    
    /**
     * Driver routine to print total distance.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     */
    public void printPath( String destName )
    {
        Vertex w = vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            System.out.print( "(Distance is: " + w.dist + ") " );
            printPath( w );
            System.out.println( );
        }
    }
    
    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    private Vertex getVertex( String vertexName )
    {
        Vertex v = vertexMap.get( vertexName );
        if( v == null )
        {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }
    
    /**
     * Recursive routine to print shortest path to dest
     * after running shortest path algorithm. The path
     * is known to exist.
     */
    private void printPath( Vertex dest )
    {
        if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " to " );
        }
        System.out.print( dest.name );
    }
    
    /**
     * Initializes the vertex output info prior to running
     * any shortest path algorithm.
     */
    private void clearAll( )
    {
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }
    
    /**
     * Single-source unweighted shortest-path algorithm.
     */
    public void unweighted( String startName )
    {
        clearAll( );
        
        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );
        
        Queue<Vertex> q = new LinkedList<Vertex>( );
        q.add( start ); start.dist = 0;
        
        while( !q.isEmpty( ) )
        {
            Vertex v = q.remove( );
            
            for( Edge e : v.adj )
            {
                Vertex w = e.vertex;
                if( w.dist == INFINITY )
                {
                    w.dist = v.dist + 1;
                    w.prev = v;
                    q.add( w );
                }
            }
        }
    }
    
    //---------------------------------------------   ---------------------------------------------    ---------------------------------------------
    //---------------------------------------------   ---------------------------------------------   ---------------------------------------------
    /**
     *  Codes written by Ragavendran Balaji
     */
    
    /**
     * Add a new edge to the graph. (with weights)
     */
    public void addEdge( String sourceName, String destName, double time )
    {
        int f = 0;
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        for(int i=0; i<v.adj.size(); i++){
            if(v.adj.get(i).vertex.equals(w)){      // adding new edge to the adjacent of v
                v.adj.get(i).time = time;
                System.out.println("Updated the transmission time for the existing edge");
                f = 1;
            }
        }
        if(f == 0){
            Edge e = new Edge(w, time);
            v.adj.add( e );
        }
    }
    
    
    /**
     * This method deletes the edge between tail and head
     */
    public void deleteedge(String tail, String head){
        
        Vertex v = this.vertexMap.get(tail);		// extract the vertex
        if( v == null )
            throw new NoSuchElementException( "vertex not found" );
        List<Edge> ed = v.adj;
        for(int i=0; i<ed.size(); i++){
            if(ed.get(i).vertex.name.equals(head)){
                v.adj.remove(i);				//remove the edge from the adjacency list
                System.out.println("Edge removed");
            }
            
        }
        
    }
    
    
    
    /**
     * This method updated the status of the edge to UP or DOWN depending on the status variable
     */
    
    public void edgeupdate(String tail, String head, int status){
        
        //System.out.println("here");
        int f = 0;
        String st = "";
        if(status == DOWN)
            st = "DOWN";
        else if(status == UP)
            st = "UP";
        Vertex v = this.vertexMap.get(tail);		// extract vertex
        if( v == null )
            throw new NoSuchElementException( "tail vertex not found" );
        List<Edge> ed = v.adj;
        
        for(int i=0; i<v.adj.size(); i++){
            if(ed.get(i).vertex.name.equals(head)){
                f = 1;
                ed.get(i).stat = status;			// the status of edge is changed according to the argument
                System.out.println("The edge from " + tail + " to " + head + " is now " + st);
            }
        }
        if(f == 0)
            System.out.println("head vertex not found");
        
    }
    
    
    
    /**
     *  This method updated the status of the vertex to UP or DOWN depending on the status variable
     */
    public void vertexupdate(String ver, int status){
        String st = "";
        if(status == DOWN)
            st = "DOWN";
        else if(status == UP)
            st = "UP";
        Vertex v = this.vertexMap.get(ver);		//vertex extracted
        if( v == null )
            throw new NoSuchElementException( "vertex not found" );
        v.stat = status;						//status of vertex changed according to the argument
        System.out.println("The vertex " + v.name + " is " + st);
        
    }
    
    
    
    
    /**
     * Dijkstra's Algorithm implementation
     */
    public void dijkstras(String s, String d){
        
        MinHeap queue = new MinHeap(this.vertexMap.size() + 1);			//object of MinHeap with the size of the vertexMap
        
        ArrayList<String> vertex = new ArrayList<String>(this.vertexMap.keySet());
        HashMap<Double,Vertex> ver = new HashMap<Double,Vertex>();
        
        Vertex source = this.vertexMap.get(s);
        Vertex dest = this.vertexMap.get(d);
        double min;
        
        if(source.stat == DOWN)			// check is source is down
            throw new NoSuchElementException( "source vertex is DOWN" );
        
        for(int i=0; i<vertex.size(); i++){
            Vertex v = this.vertexMap.get(vertex.get(i));
            //System.out.println(v.name);
            v.dist = this.INFINITY;				//initialize dist of all vertices to be infinity and prev to null
            v.prev = null;
            
        }
        
        source.dist = 0.0;		// set source distance as 0
        
        for(int i=0; i<vertex.size(); i++){
            Vertex v = this.vertexMap.get(vertex.get(i));
            ver.put(v.dist, v);					// putting vertices in hashmap with the transmission time as key
            queue.insert(v.dist);
        }
        // queue.insert(source.dist);
        
        while(queue.size != 0){				//dijkstra's algorithm
            min = queue.extractMin();		// extracting min vertex
            Vertex u = ver.remove(min);
            for(int i=0; i<u.adj.size(); i++){
                Edge w = u.adj.get(i);
                Vertex v = u.adj.get(i).vertex;
                //System.out.println("V : " + v.name + " U : " + u.name);
                if(w.stat != DOWN && v.stat != DOWN){
                    if(v.dist > u.dist + w.time){		// updating the distance of the vertex
                        int index = queue.indexof(v.dist);
                        v.dist = Math.round((u.dist + w.time) * 100.0)/100.0;		//rounding to 2 decimal places
                        //System.out.println("Node :" + v.name + " distance " + v.dist);
                        v.prev = u;
                        
                        ver.put(v.dist, v);
                        queue.increasekey(index, v.dist);			// putting all the adjacent vertices to the minHeap
                    }
                }
            }
        }
        pathprint(dest);
        
    }
    
    
    /**
     * Print method to print the shortest path to the destination
     */
    
    void pathprint(Vertex d){
        
        String path = "" + d.dist;
        while(d.prev != null){
            path = d.name + " " + path;
            d = d.prev;
        }
        path = d.name + " " + path;
        System.out.println(path);
    }
    
    
    
    /**
     * this method prints all the adjacent edges in alphabetically sorted order
     *
     */
    public void printsortedge(List<Edge> ed){
        
        HashMap<String,Double> edge = new HashMap<String,Double>();
        //HashMap<String,Double> sortededge = new HashMap<String,Double>();
        String edname = "";
        
        for(int i=0; i<ed.size(); i++){
            if(ed.get(i).stat == DOWN)
                edname = ed.get(i).vertex.name + " DOWN";       // ammends DOWN after the name of the vertex
            else
                edname = ed.get(i).vertex.name;
            edge.put(edname, ed.get(i).time);
            
        }
        ArrayList<String> sortededges = new ArrayList<String>(edge.keySet());
        Collections.sort(sortededges);      //sorts the vertices
        //System.out.println(edges);
        
        
        for(int i=0; i<sortededges.size(); i++){
            System.out.println("  " + sortededges.get(i) + " " +  edge.get(sortededges.get(i)));    //prints with the transmission time
        }
        
        
    }
    
    
    /**
     * Prints all vertices in alphabetically sorted order and calls printsortedge to print the
     * adjacent edges in sorted order
     */
    public void print(){
        
        ArrayList<String> v = new ArrayList<String>(this.vertexMap.keySet());
        //System.out.println(v);
        Collections.sort(v);            //sorts the arraylist
        //System.out.println(v);
        for(int i=0; i<v.size(); i++){
            Vertex ver = this.vertexMap.get(v.get(i));
            if( ver == null )
                throw new NoSuchElementException( "vertex not found" );
            System.out.print(ver.name);
            if(ver.stat == DOWN)
                System.out.print(" DOWN");
            System.out.println();
            List<Edge> ed = ver.adj;
            this.printsortedge(ed);         // print the adjacent vertices
            
        }
        
        
    }
    
    
    /**
     * find all the vertices reachable from a vertex using BFS (Breadth First Search) and
     *  print it for every vertex in the graph.
     * BFS algorithm is used here and the status of the vertices and edges are checked to see if they are
     * DOWN or UP, if DOWN ignore the edge or vertex and continue
     * Since the algorithm is BFS its complexity will be O(|V| + |E|)
     */
    
    public void reachableBFS(){
        Queue<Vertex> queue = new LinkedList<Vertex>();			//object of MinHeap with the size of the vertexMap
        
        ArrayList<String> vertex = new ArrayList<String>(this.vertexMap.keySet());
        HashMap<Double,Vertex> ver = new HashMap<Double,Vertex>();
        
        Collections.sort(vertex);
        double d = 0.0;
        
        for(int i=0; i<vertex.size(); i++){
            Vertex source = vertexMap.get(vertex.get(i));		// source vertex itterated through every vertex
            if(source.stat != DOWN){
                System.out.println(source.name);
                for(int j=0; j<vertex.size(); j++){
                    Vertex v = this.vertexMap.get(vertex.get(j));
                    if(v.equals(source)){
                        v.dist = 0.0;
                        v.color = G;
                    }
                    else{
                        v.dist = INFINITY;				//initialize dist of all vertices to be infinity and prev to null
                        v.color = W;
                    }
                    v.prev = null;
                    //ver.put(v.dist, v);
                }
                
                queue.add(source);
                
                
                while(!queue.isEmpty()){				//BFS algorithm
                    Vertex u = queue.remove();
                    //System.out.println(u.name);
                    for(int j=0; j<u.adj.size(); j++){
                        Edge w = u.adj.get(j);
                        Vertex v = u.adj.get(j).vertex;
                        if(w.stat != DOWN && v.stat != DOWN){		//checking if the edge or vertex is down
                            if(v.color == W){		//checking if vertex visited by color
                                v.color = G;		// changing color
                                v.dist = u.dist + 1;
                                v.prev = u;
                                //System.out.println("  " + v.name);
                                queue.add(v);		// adding to queue
                            }
                        }
                    }
                    u.color = B;			// color B represents all vertices reachable by source
                }
                
                for(int j=0; j<vertex.size(); j++){		// printing every vertex reachable by source
                    Vertex v = vertexMap.get(vertex.get(j));
                    if(!v.equals(source)){
                        //System.out.println(v.name + "  " + v.color);
                        if(v.color == B)
                            System.out.println("  " + v.name);
                    }
                }
            }
        }
    }
    
    
    
    
    /**
     * Process a request; return false if end of file.
     */
    
    public static boolean processRequest( Scanner in, Graph g )
    {
        
        try
        {
            //while(true){
            System.out.print( "\nEnter Request : \n");
            String request = in.nextLine( );
            StringTokenizer rq = new StringTokenizer(request);
            String tail = "";
            String head = "";
            String dest = "";
            String source = "";
            String ver = "";
            double time = 0.0;
            
            switch(rq.nextToken()){			// switch case for every request query
                    
                case "addedge":		tail = rq.nextToken( );
                    head = rq.nextToken( );
                    time = Double.parseDouble(rq.nextToken( ));
                    g.addEdge( tail, head, time );
                    System.out.println("Added a new edge");
                    break;
                    
                case "deleteedge":	tail = rq.nextToken( );
                    head = rq.nextToken( );
                    g.deleteedge(tail, head);
                    break;
                    
                case "edgedown":	tail = rq.nextToken( );
                    head = rq.nextToken( );
                    g.edgeupdate(tail, head, DOWN);
                    break;
                    
                case "edgeup":		tail = rq.nextToken( );
                    head = rq.nextToken( );
                    g.edgeupdate(tail, head, UP);
                    break;
                    
                case "vertexdown":	ver = rq.nextToken();
                    g.vertexupdate(ver, DOWN);
                    break;
                    
                case "vertexup":	ver = rq.nextToken();
                    g.vertexupdate(ver, UP);
                    break;
                    
                case "path":		source = rq.nextToken( );
                    dest = rq.nextToken( );
                    g.dijkstras(source, dest);
                    break;
                    
                case "print":		g.print();
                    break;
                    
                case "reachable":	g.reachableBFS();
                    break;
                    
                case "quit":		System.out.println("Terminated");
                    System.exit(0);
                    break;
                    
                default :	System.out.println("query doesnot exist please try again");
                    
            }
            
            // System.out.print( "Enter destination node: " );
            // String destName = in.nextLine( );
            
            // g.unweighted( startName );
            //g.printPath( destName );
            //}
        }
        catch( NoSuchElementException e )
        {System.out.println(e);
            return false; }
        catch( GraphException e )
        { System.err.println( e ); }
        return true;
    }
    
    /**
     * A main routine that:
     * 1. Reads a file containing edges (supplied as a command-line parameter);
     * 2. Forms the graph;
     * 3. Repeatedly prompts for two vertices and
     *    runs the shortest path algorithm.
     * The data file is a sequence of lines of the format
     *    source destination 
     */
    public static void main( String [ ] args )
    {
        Graph g = new Graph( );
        try
        {
            FileReader fin = new FileReader( args[0] );		// read the file from argument
            Scanner graphFile = new Scanner( fin );
            
            // Read the edges and insert
            String line;
            while( graphFile.hasNextLine( ) )		// scan each line in the file
            {
                line = graphFile.nextLine( );
                StringTokenizer st = new StringTokenizer( line );
                
                try
                {
                    if( st.countTokens( ) != 3 )		//checking if each line has a source vertex, a destination vertex and transmission time
                    {
                        System.err.println( "Skipping ill-formatted line " + line );
                        continue;
                    }
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                    double time = Double.parseDouble(st.nextToken( ));	
                    g.addEdge( source, dest, time );			// add edge in both the direction
                    g.addEdge( dest, source, time );	
                    
                }
                catch( NumberFormatException e )
                { System.err.println( "Skipping ill-formatted line " + line ); }
            }
        }
        catch( IOException e )
        { System.err.println( e ); }
        
        System.out.println( "File read..." );
        System.out.println( g.vertexMap.size( ) + " vertices" );
        System.out.println("Graph created");
        
        Scanner in = new Scanner( System.in );				// list of valid queries
        System.out.print( "\n\nEnter Query with the following syntax : \n"
                         + "---\naddedge 	tail_vertex   head_vertex   transmit_time \n"
                         + "deleteedge 	tailvertex    headvertex \n"
                         + "edgedown	tailvertex    headvertex \n"
                         + "edgeup 		tailvertex    headvertex \n"
                         + "vertexdown 	vertex \n"
                         + "vertexup 	vertex \n"
                         + "path 		from_vertex   to_vertex \n"
                         + "print \n"
                         + "reachable \n"
                         + "quit \n"
                         + "---\n" );	
        while( processRequest( in, g ));		// process request
        
    }
}
