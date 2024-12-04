import java.util.*;
import java.io.*;

class Vertex{
    private int vertexNum;  //corresponds to the vertex number from the input file
    private int baconNum;   //the Bacon number of the vertex
    private ArrayList<Integer> edges;   //the edges of the vertex

    public Vertex(int vertexNum){
        this.vertexNum = vertexNum;
        this.edges = new ArrayList<>();
        this.baconNum = -1; // Bacon number is initially undefined
    }
    public void addEdge(int edge){ edges.add(edge); }
    public void setBaconNum(int baconNum){ this.baconNum = baconNum; }
    public int getVertexNum(){ return vertexNum; }
    public int getBaconNum(){ return baconNum; }
    public ArrayList<Integer> getEdges(){ return edges; }
}

public class Bacons{
    private static Map<Integer, Vertex> network = new HashMap<>();

    private static void readInput(String[] args){
        if (args.length < 1){  //make sure the input file name is passed in as a command line argument
            System.out.println("Please provide the input file as an argument.");
            System.exit(0);
        }

        try{
            File file = new File(args[0]);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            int from, to;

            while ((line = in.readLine()) != null){ //while you're not at the end of the file
                if (line.trim().startsWith("#")) continue;  //if the line starts with # it's a comment so ignore it

                String[] parts = line.split("\\s+");    //split the lines
                from = Integer.parseInt(parts[0]);  //the first integer is the from vertex
                to = Integer.parseInt(parts[1]);    //the second integer is the to vertex

                Vertex v = network.get(from);   //get the from vertex from the network
                if (v != null) v.addEdge(to);   //it it's in the network, add the edge
                else{   //if it's not in the network
                    v = new Vertex(from);   //create a new from vertex
                    v.addEdge(to);  //add the edge
                    network.put(from, v);   //put that vertex in the network
                }

                if (!network.containsKey(to)) network.put(to, new Vertex(to));  //check if to is in the network, if not add it
            }
            in.close();
        } catch (Exception e){  //something went wrong, end the program
            System.out.println(e);
            System.out.println("An error occurred. Please restart the program.");
            System.exit(0);
        }
    }

    // Breadth-First Search (BFS) to calculate Bacon number between start and end
    private static int calculateBaconNumber(int start, int end){
        if (start == end) return 0; //if start and end are the same, then the Bacon number is 0

        Map<Integer, Integer> distances = new HashMap<>();
        Queue<Integer> q = new LinkedList<>();
        distances.put(start, 0);    //add the start vertex with the distance of 0
        q.offer(start); //add the start vertex to the queue

        while (!q.isEmpty()){   //while the queue still has vertices
            int cur = q.poll(); //get the next vertex
            int curDistance = distances.get(cur);   //get the next vertices distance

            for (int neighbor : network.get(cur).getEdges()){   //for all edges from the current vertex
                if (!distances.containsKey(neighbor)){  //if neighbor is not in distances, then it hasn't been visited
                    distances.put(neighbor, curDistance + 1);   //add it to distances with a one added to the current distance because it's one more step away
                    q.offer(neighbor);  //add neighbor to the queue
                    if (neighbor == end) return distances.get(neighbor);    //if neighbor is the end then return its distance
                }
            }
        }
        return -1;  //no path exists
    }

    // Calculate the average Bacon number for a random vertex
    private static double calculateAverageBaconNumber(int randVertex){
        System.out.println("Calculating the average Bacon number from vertex " + randVertex + "...");

        int totalBaconNumber = 0, count = 0;

        for (Vertex v : network.values()){  //for all vertices in the network
            if (v.getVertexNum() == randVertex) continue; //skip the random vertex itself
            int baconNum = calculateBaconNumber(randVertex, v.getVertexNum());  //calculate the Bacon number for the vertex from the random vertex
            v.setBaconNum(baconNum);    //save the vertex's Bacon number
            if (baconNum != -1){    //if the Bacon number exists
                totalBaconNumber += baconNum;   //add it to the running total
                count++;    //add one to the count of vertices
            }
        }

        if (count > 0) return (double) totalBaconNumber / count;    //if there is at least 1 vertex
        else return -1; //otherwise return -1, no vertices are neighbors with randVertex
        
    }

    // Select a random vertex from the network
    private static int selectRandomVertex(){
        Random random = new Random();
        List<Integer> keys = new ArrayList<>(network.keySet());
        int randVertex = keys.get(random.nextInt(keys.size()));
        while (!network.containsKey(randVertex)) randVertex = keys.get(random.nextInt(keys.size()));
        return randVertex;
    }

    public static void main(String[] args){
        readInput(args); //reads the input and builds the network
        
        int randVertex = selectRandomVertex();  //gets the random vertex, which acts as the Bacon vertex
        System.out.println("The randomly selected vertex which is used as the Bacon vertex is " + randVertex);

        double averageBaconNum = calculateAverageBaconNumber(randVertex);   //calculates the average bacon number from the Bacon vertex
        System.out.println("The average Bacon number from vertex " + randVertex + " is " + averageBaconNum);    //add this to the output file

        //while the user wishes to continue
            //get a vertex from them (confirm it is in the network)
            //get the bacon number for that vertex (see what is saved and recalculate it check if they match)
            //add the information to the output file
            //ask if they wish to continue, update the while loop condition

        //output the final file
        //thank the user

        //prints out the network, used for testing
        // for (Vertex v : network.values()){
        //     System.out.print("Vertex " + v.getVertexNum() + " -> ");
        //     ArrayList<Integer> edges = v.getEdges();
        //     if (edges.isEmpty()){
        //         System.out.println("No edges");
        //     } else {
        //         for (int edge : edges){
        //             System.out.print(edge + " ");
        //         }
        //         System.out.println(); // New line after printing all edges
        //     }
        // }

        // Scanner scanner = new Scanner(System.in);
        // boolean cont = true;

        // // Main interaction loop
        // while (cont){
        //     System.out.print("Enter a vertex number: ");
        //     int userVertex = scanner.nextInt();

        //     if (!network.containsKey(userVertex)){
        //         System.out.println("Vertex not found. Please try again.");
        //         continue;
        //     }

        //     // Assume vertex 1 is the "Kevin Bacon" vertex
        //     int baconNum = calculateBaconNumber(1, userVertex);
        //     System.out.println("Bacon number for vertex " + userVertex + ": " + baconNum);

        //     double avgBaconNum = calculateAverageBaconNumber(userVertex);
        //     System.out.println("Average Bacon number for vertex " + userVertex + ": " + avgBaconNum);

        //     System.out.print("Do you want to continue? (y/n): ");
        //     String response = scanner.next();
        //     cont = response.equalsIgnoreCase("y");
        // }

        // scanner.close();
        // System.out.println("Thank you for using the Bacon Number Calculator!");
    }
}
