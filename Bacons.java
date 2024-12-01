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

        private int calculateBaconNumber(int start, int end){
            if (start == end) return 0; //If they are the same the bacon number is 0

            //Doing network.size will not work for this
            Queue<Integer> q = new LinkedList<>();
            int[] baconNums = new int[network.size() + 1];
            Arrays.fill(baconNums, -1);
            boolean[] visited = new boolean[network.size() + 1];

            //initialization for BFS
            q.offer(start);
            baconNums[start] = 0;
            visited[start] = true;

            while (!q.isEmpty()){
                int cur = q.poll();
                int curBaconNum = baconNums[cur];

                Vertex v = network.get(cur);
                for (int i : v.getEdges()){
                    if (!visited[i]){   //if the edge has not been visited
                        visited[i] = true;
                        baconNums[i] = curBaconNum + 1;
                        if (i == end) return baconNums[i];  //if you made it to the end return the bacon number
                        q.offer(i);
                    }
                }
            }
            return -1;  //there was no path from start to end
        }

        private double calculateAverageBaconNumber(int randVertex){
            int totalBacon = 0, numVertices = 0;

            //calculate the bacon number to every reachable vertex
            //average all the bacon numbers together
            for (Vertex v : network){
                int to = v.getVertexNum();
                int baconNum = calculateBaconNumber(randVertex, to);
                if (baconNum != -1){
                    totalBacon += baconNum;
                    numVertices++;
                }
            }

            return (double) totalBacon / numVertices;
        }
    
        public static void main(String[] args) {
            b.readInput(args);  //reads all input and creates the network
            int randVertex = b.selectRandomVertex();    //selects a valid random vertex
            double averageBaconNum = b.calculateAverageBaconNumber(randVertex);   //calculates the average bacon number from the randomly selected vertex to all others in the network

            //call method to calculate the average bacon number from the randomly selected vertex 
            //call method to read user selected vertex
            //call method to calculate the bacon number from this vertex (make sure the vertex exists first)
            //call method to see if the user wishes to continue
                //if yes go back to step 4, use a do while loop with a boolean to see if they want to continue
                //if no continue below to the next step
            //call method to output a file containing necessary information
            //call method to thank the user

     
    
    
            //Below this is used for ensuring input is read in correctly
            for (Vertex v : network){
            System.out.print(v.getVertexNum() + ": ");
            for (Integer i : v.getEdges()){
                System.out.print(i + ", ");
            }
                System.out.println();
            }

            System.out.println("\n" + randVertex);
            System.out.println(averageBaconNum);
    }
}
