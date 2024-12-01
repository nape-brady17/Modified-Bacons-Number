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
    
        //want to speed this method up somehow, searching takes FOREVER
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

            System.out.println("reads input correctly");
    
            return network;
        }
    
        private int selectRandomVertex(){
            Random random = new Random();
            int randIndex = random.nextInt(network.size());
            return network.get(randIndex).getVertexNum();
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

            for (Vertex v : network){   //for all vertices
                int to = v.getVertexNum();
                int baconNum = calculateBaconNumber(randVertex, to);    //calculate the bacon number to that vertex
                if (baconNum != -1){
                    totalBacon += baconNum; //add it to the total
                    numVertices++;  //add one to the number of vertices
                }
            }

            return (double) totalBacon / numVertices;   //return the average bacon number
        }

        public static void main(String[] args) {
            //create a file to output at the end 

            b.readInput(args);  //reads all input and creates the network

            int randVertex = b.selectRandomVertex();    //selects a random vertex from the network

            double averageBaconNum = b.calculateAverageBaconNumber(randVertex);   //calculates the average bacon number from the randomly selected vertex to all others in the network
                //should work after calculateBaconNumber works, needs to be updated to run BFS correctly

            //add the random vertex and the average bacon number to the file created above

            boolean cont = true;
            do{
                //get vertex from the user, ensure the vertex exists in the network
                //calculate the bacon number and likelihood of collaboration
                //add the user vertex, bacon number, and likelihood of collaboration to the file
                    //also display it to the user
                //ask the user if they want to continue, update cont if needed
                cont = false;
            } while (cont);


            //call method to output the file containing all necessary information
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
