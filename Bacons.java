import java.util.*;
import java.io.*;

class Vertex{
    private int vertexNum;
    private int baconNum;
    private ArrayList<Integer> edges;

    public Vertex(int vertexNum) {
        this.vertexNum = vertexNum;
        this.edges = new ArrayList<>();
        this.baconNum = -1; // Bacon number is initially undefined
    }
    public void addEdge(int edge){ edges.add(edge); }
    public int getVertexNum(){ return vertexNum; }
    public int getBaconNum(){ return baconNum; }
    public void setBaconNum(int baconNum){ this.baconNum = baconNum; }
    public ArrayList<Integer> getEdges(){ return edges; }
}

public class Bacons{
    private static Map<Integer, Vertex> network = new HashMap<>();

    private static void readInput(String[] args){
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
    private static int calculateBaconNumber(int start, int end) {
        if (start == end) return 0;

        Map<Integer, Integer> distances = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        distances.put(start, 0);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentDistance = distances.get(current);

            for (int neighbor : network.get(current).getEdges()) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, currentDistance + 1);
                    queue.offer(neighbor);
                    if (neighbor == end) {
                        return distances.get(neighbor);
                    }
                }
            }
        }
        return -1;  // No path exists
    }

    // Calculate the average Bacon number for a random vertex
    private static double calculateAverageBaconNumber(int randVertex) {
        int totalBaconNumber = 0, count = 0;

        for (Vertex v : network.values()) {
            if (v.getVertexNum() == randVertex) continue; // Skip the random vertex itself
            int baconNum = calculateBaconNumber(randVertex, v.getVertexNum());
            //System.out.println(v.getVertexNum() + ": " + baconNum);
            if (baconNum != -1) {
                totalBaconNumber += baconNum;
                count++;
            }
        }

        if (count > 0) return (double) totalBaconNumber / count;
        else return -1;
        
    }

    // Select a random vertex from the network
    private static int selectRandomVertex() {
        Random random = new Random();
        List<Integer> keys = new ArrayList<>(network.keySet());
        return keys.get(random.nextInt(keys.size()));
    }

    public static void main(String[] args) {
        if (args.length < 1) {  //make sure the input file name is passed in as a command line argument
            System.out.println("Please provide the input file as an argument.");
            System.exit(0);
        }

        readInput(args); //reads the input and builds the network
        
        //make sure the vertex exists in the select method
        int randVertex = selectRandomVertex();
        randVertex = 71725;
        while (!network.containsKey(randVertex)) randVertex = selectRandomVertex();
        System.out.println("The randomly selected vertex which is used as the Bacon vertex is " + randVertex);

        System.out.println("Calculating the average Bacon number from vertex " + randVertex + "...");
        double averageBaconNum = calculateAverageBaconNumber(randVertex);
        System.out.println("The average Bacon number from vertex " + randVertex + " is " + averageBaconNum);
        //add the above statement to the output file

        //while the user wishes to continue
            //get a vertex from them (confirm it is in the network)
            //get the bacon number for that vertex (see what is saved and recalculate it check if they match)
            //add the information to the output file
            //ask if they wish to continue, update the while loop condition

        //output the final file
        //thank the user

        //prints out the network, used for testing
        // for (Vertex v : network.values()) {
        //     System.out.print("Vertex " + v.getVertexNum() + " -> ");
        //     ArrayList<Integer> edges = v.getEdges();
        //     if (edges.isEmpty()) {
        //         System.out.println("No edges");
        //     } else {
        //         for (int edge : edges) {
        //             System.out.print(edge + " ");
        //         }
        //         System.out.println(); // New line after printing all edges
        //     }
        // }

        // Scanner scanner = new Scanner(System.in);
        // boolean cont = true;

        // // Main interaction loop
        // while (cont) {
        //     System.out.print("Enter a vertex number: ");
        //     int userVertex = scanner.nextInt();

        //     if (!network.containsKey(userVertex)) {
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
