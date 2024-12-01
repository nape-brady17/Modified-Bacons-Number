import java.util.*;
import java.io.*;
import java.math.*;

class Vertex{
    private int vertexNum;
    private ArrayList<Integer> edges;

    public Vertex(){};
    public Vertex(int vertexNum, int edge){
        this.vertexNum = vertexNum;
        edges = new ArrayList<>();
        edges.add(edge);
    }
    public void addEdge(int edge){ edges.add(edge); }
    public int getVertexNum(){ return vertexNum; }
    public ArrayList<Integer> getEdges(){ return edges; }
}


public class Bacons{
    private static Bacons b = new Bacons();

    private int has(ArrayList<Vertex> network, int from){
        for (Vertex v : network){
            if (v.getVertexNum() == from) return network.indexOf(v);
        }
        return -1;
    }

    private ArrayList<Vertex> readInput(String[] args){
        ArrayList<Vertex> network = new ArrayList<>();

        //for each vertex read in check if its already in graph
        //if it is do graph.find(vertex) to give that vertex then add the new edge
        //if it is not create a new vertex and add it to the arraylist
        try{
            File file = new File(args[0]);
            Scanner in = new Scanner(file);
            long numLines = -1;
            int from, to, at;

            try(LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file))) {
                lineNumberReader.skip(Long.MAX_VALUE);
                numLines = lineNumberReader.getLineNumber();
            }

            for (int i = 0; i < 4; i++){
                in.nextLine();
            }
            
            for (long i = 0; i < numLines - 4; i++){
                from = in.nextInt();
                to = in.nextInt();
                at = b.has(network, from);
                if (at != -1){ //the vertex is in the network already at vertex at
                    network.get(at).addEdge(to);
                }else{
                    Vertex v = new Vertex(from, to);
                    network.add(v);
                }
            }
            in.close();
        } catch(Exception e){  //any other unexpected error
            System.out.println(e);
            System.out.println("An error occurred please restart the program.");
            System.exit(0);
        }

        return network;
    }

    public static void main(String[] args) {
        //call method to read all input and create the graph
        //call method to randomly select vertex
        //call method to calculate the average bacon number from the randomly selected vertex
        //call method to read user selected vertex
        //call method to calculate the bacon number from this vertex
        //call method to see if the user wishes to continue
            //if yes go back to step 4, use a do while loop with a boolean to see if they want to continue
            //if no continue below to the next step
        //call method to output a file containing necessary information
        //call method to thank the user

        ArrayList<Vertex> network = b.readInput(args);

        for (Vertex v : network){
            System.out.print(v.getVertexNum() + ": ");
            for (Integer i : v.getEdges()){
                System.out.print(i + ", ");
            }
            System.out.println();
        }
    }
}