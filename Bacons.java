import java.util.*;
import java.io.*;

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
    private static ArrayList<Vertex> network = new ArrayList<>();
    
        //may want to speed this method up somehow, searching takes FOREVER
        private int has(int from){
            for (Vertex v : network){
                if (v.getVertexNum() == from) return network.indexOf(v);
            }
            return -1;
        }
    
        private ArrayList<Vertex> readInput(String[] args){
            try{    //read the file and create the network
                File file = new File(args[0]);
                BufferedReader in = new BufferedReader(new FileReader(file));
                String line;
                int from, to, at;
    
                while ((line = in.readLine()) != null){ //while not at the end of the file
                    if (line.trim().startsWith("#")) continue;  //if the line does not start with '#'
    
                    String[] parts = line.split("\\s+");    //split the line
                    from = Integer.parseInt(parts[0]);  //get the from vertex
                    to = Integer.parseInt(parts[1]);    //get the to vertex
                    at = b.has(from);  //check if from is in the network already
                    if (at != -1){ //the vertex is in the network already at vertex at
                        network.get(at).addEdge(to);    //if it is then add the edge
                    }else{  //if its not then create a new vertex
                        Vertex v = new Vertex(from, to);
                        network.add(v);
                    }
                }
                in.close();
            } catch(Exception e){  //any unexpected error
                System.out.println(e);
                System.out.println("An error occurred please restart the program.");
                System.exit(0);
            }
    
            return network;
        }
    
        private int selectRandomVertex(){
            Random random = new Random();
            int randVertex;
            do{ randVertex =  random.nextInt((network.size() - 0) + 1);
                System.out.println(randVertex);
            } while (has(randVertex) == -1);

            return randVertex;
        }
    
        public static void main(String[] args) {
            //Things to do:
                //call method to randomly select vertex
                //call method to calculate the average bacon number from the randomly selected vertex
                //call method to read user selected vertex
                //call method to calculate the bacon number from this vertex
                //call method to see if the user wishes to continue
                    //if yes go back to step 4, use a do while loop with a boolean to see if they want to continue
                    //if no continue below to the next step
                //call method to output a file containing necessary information
                //call method to thank the user
    
            b.readInput(args);  //reads all input and creates the network
            int randVertex = b.selectRandomVertex();    //selects a valid random vertex

     
    
    
            //Below this is used for ensuring input is read in correctly
            for (Vertex v : network){
            System.out.print(v.getVertexNum() + ": ");
            for (Integer i : v.getEdges()){
                System.out.print(i + ", ");
            }
                System.out.println();
            }

            System.out.println("\n" + randVertex);
    }
}