import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

class Vertex{
    private int vertexNum;  //corresponds to the vertex number from the input file
    private ArrayList<Integer> edges;   //the edges of the vertex

    public Vertex(int vertexNum){
        this.vertexNum = vertexNum;
        this.edges = new ArrayList<>();
    }
    public void addEdge(int edge){ edges.add(edge); }
    public int getVertexNum(){ return vertexNum; }
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

    private static double calculateAverageBaconNumber(int randVertex){
        System.out.println("Calculating the average Bacon number from vertex " + randVertex + ". Depending on the network size this may take a minute.");

        int totalBaconNumber = 0, count = 0;

        for (Vertex v : network.values()){  //for all vertices in the network
            if (v.getVertexNum() == randVertex) continue; //skip the random vertex itself
            int baconNum = calculateBaconNumber(randVertex, v.getVertexNum());  //calculate the Bacon number for the vertex from the random vertex
            if (baconNum != -1){    //if the Bacon number exists
                totalBaconNumber += baconNum;   //add it to the running total
                count++;    //add one to the count of vertices
            }
        }

        if (count > 0){
            DecimalFormat df = new DecimalFormat("#.###");
            double averageBaconNum = (double) totalBaconNumber / count;
            System.out.println("The average Bacon number from vertex " + randVertex + " is " + df.format(averageBaconNum) + "\n");    //add this to the output file
            return averageBaconNum;    //if there is at least 1 vertex
        }
        else return -1; //otherwise return -1, no vertices are neighbors with randVertex
        
    }

    private static int selectRandomVertex(){
        Random random = new Random();
        List<Integer> keys = new ArrayList<>(network.keySet());
        int randVertex = keys.get(random.nextInt(keys.size()));
        while (!network.containsKey(randVertex)) randVertex = keys.get(random.nextInt(keys.size()));
        return randVertex;
    }

    private static void getUserInput(int randVertex, double averageBaconNum){
        System.out.println("Note that not every vertex may exist in the network, some recommend vertices will be provided each iteration that exist in the network");

        boolean cont = true;
        Scanner in = new Scanner(System.in);
        int userVertex, baconNum;

        while(cont){    //while the user wants to keep going
            System.out.print("\nSome recommended vertices would be: ");
            for (int i = 0; i < 5; i++){
                if (i < 3) System.out.print(selectRandomVertex() + ", ");
                else if (i == 3) System.out.print(selectRandomVertex() + ", or ");
                else System.out.println(selectRandomVertex());
            }

            System.out.print("Enter a vertex: ");
            userVertex = in.nextInt();  //get the user vertex

            if (!network.containsKey(userVertex)){  //ensure the user vertex is in the network
                System.out.println("The network does not contain that vertex, please try a different vertex");
                continue;
            }

            baconNum = calculateBaconNumber(randVertex, userVertex);    //calculate teh bacon number of the user vertex

            if (baconNum != -1){
                System.out.println("The Bacon number for vertex " + userVertex + " is " + baconNum);    
                //add this to the output file
                //predict the likelihood of a collaboration here, output it and add it to the outfile file
            }
            else{
                System.out.println("No path exists between the Bacon vertex (" + randVertex + ") and your selected vertex (" + userVertex + "), please try again");
                continue;
            }

            System.out.print("Would you like to try another vertex (press Y to continue): ");
            cont = in.next().equalsIgnoreCase("Y");
        }

        System.out.println("Thank you for using the Modified Bacon Number Calculator!");
        in.close();
        return;
    }

    public static void main(String[] args){
        readInput(args); //reads the input and builds the network
        
        int randVertex = selectRandomVertex();  //gets the random vertex, which acts as the Bacon vertex
        System.out.println("The randomly selected vertex which is used as the Bacon vertex is " + randVertex);

        double averageBaconNum = calculateAverageBaconNumber(randVertex);   //calculates the average bacon number from the Bacon vertex

        //output the final file

        getUserInput(randVertex, averageBaconNum);   //take all user input, add it to the output file

        //output the output file

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
    }
}
