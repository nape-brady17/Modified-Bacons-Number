//This program was written by Brady Napier for COP 5537- Final Project

import java.util.*;
import java.io.*;
import java.text.*;

class Vertex{   //stores each vertex for the graph/network
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
    private static File outputFile = new File("output.txt");

    private static void readInput(String[] args){   //reads the graph/network and saves it
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

    private static int calculateBaconNumber(int start, int end){    //calculates the Bacon number between 2 vertices (this is a modified BFS)
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

    private static double calculateAverageBaconNumber(int randVertex){  //calculates the average Bacon number from a single vertex by finding the Bacon number to every other vertex in the graph/network
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
            System.out.println("The average Bacon number from vertex " + randVertex + " is " + df.format(averageBaconNum) + "\n");
            writeToFile("The average Bacon number from vertex " + randVertex + " is " + df.format(averageBaconNum) + "\n");
            return averageBaconNum;    //if there is at least 1 vertex
        }
        else return -1; //otherwise return -1, no vertices are neighbors with randVertex
        
    }

    private static int selectRandomVertex(){    //selects a random vertex and confirms that it is in the graph/network
        Random random = new Random();
        List<Integer> keys = new ArrayList<>(network.keySet());
        int randVertex = keys.get(random.nextInt(keys.size()));
        while (!network.containsKey(randVertex)) randVertex = keys.get(random.nextInt(keys.size()));
        return randVertex;
    }

    private static String calculateLikelihoodOfCollab(int baconNum, double averageBaconNum){    //calculates the likelihood of a collaboration based on the average Bacon number and Bacon number of the user vertex
        if (baconNum == 0) return "Guaranteed (collaborate with yourself on everything)";   //user vertex is the same as the bacon vertex
        if (baconNum == 1) return "Very high (already collaborated before)";    //the two authors have already collaborated
        if (baconNum > 1 && baconNum < (averageBaconNum - 0.5)) return "High"; //high chance, the bacon number is less than the average
        else if (baconNum > (averageBaconNum - 0.5) && baconNum < (averageBaconNum + 0.5)) return "Medium"; //medium chance, the bacon number is right around the average
        else return "Low";  //low chance, the bacon number is higher than the average
    }

    private static void getUserInput(int randVertex, double averageBaconNum){   //gets all user input for all vertices they want to test, outputs necessary information
        System.out.println("Note that not every vertex may exist in the network, some recommend vertices will be provided each iteration that exist in the network");

        boolean cont = true;
        Scanner in = new Scanner(System.in);
        int userVertex, baconNum;

        try{
            while (cont){    //while the user wants to keep going
                ArrayList<Integer> recommended = new ArrayList<>();
                int rec;
                System.out.print("\nSome recommended vertices would be: ");
                for (int i = 0; i < 5; i++){    //list 5 unique recommended vertices that are in the network
                    rec = selectRandomVertex();
                    while (recommended.contains(rec)) rec = selectRandomVertex();
                    recommended.add(rec);
                    if (i < 3) System.out.print(rec + ", ");
                    else if (i == 3) System.out.print(rec + ", or ");
                    else System.out.println(rec);
                }

                System.out.print("Enter a vertex: ");
                userVertex = in.nextInt();  //get the user vertex

                if (!network.containsKey(userVertex)){  //ensure the user vertex is in the network
                    System.out.println("The network does not contain that vertex, please try a different vertex");
                    continue;
                }

                baconNum = calculateBaconNumber(randVertex, userVertex);    //calculate the bacon number of the user vertex

                if (baconNum != -1){    //if a path exists, output all information
                    System.out.println("The Bacon number for vertex " + userVertex + " is: " + baconNum);
                    System.out.println("\tThe likelihood of collaboration between vertex " + randVertex + " and vertex " + userVertex + " is: " + calculateLikelihoodOfCollab(baconNum, averageBaconNum));
                    writeToFile("The Bacon number for vertex " + userVertex + " is: " + baconNum);
                    writeToFile("\tThe likelihood of collaboration between vertex " + randVertex + " and vertex " + userVertex + " is: " + calculateLikelihoodOfCollab(baconNum, averageBaconNum) + "\n");
                }
                else{   //if no path exists
                    System.out.println("No path exists between the Bacon vertex (" + randVertex + ") and your selected vertex (" + userVertex + "), please try again");
                    continue;
                }

                System.out.print("Would you like to try another vertex (press y to continue): ");
                cont = in.next().equalsIgnoreCase("Y");
            }
        } catch (Exception e){  //something went wrong when taking the user input
            System.out.println("An unexpected error occurred, please restart the program.");
            System.exit(0);
        }

        System.out.println("\nThank you for using the Modified Bacon Number Calculator!");
        System.out.println("\toutput.txt contains all information from this running of the program.");
        in.close();
        return;
    }

    private static void writeToFile(String str){    //writes the string passed in to the output file
        try{
            FileWriter fw = new FileWriter(outputFile, true);   //open the file in append mode
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(str);  //write the string to the file
            bw.newLine();   //write a new line to the file
            bw.close();
        } catch (Exception e){  //something went wrong when writing to the file
            System.out.println("An error occurred when writing to the file, please restart the program");
            System.exit(0);
        }
        return;
    }

    private static void clearOutputFile(){  //clears the output file to ensure it is blank each time the program is run
        try{
            outputFile.delete();    //delete the old output.txt
            outputFile.createNewFile(); //create a new output.txt that is blank to write to
        } catch (Exception e){
            System.out.println("An error occurred while clearing the output file, please restart the program");
            System.exit(0);
        }
    }

    public static void main(String[] args){
        clearOutputFile();  //ensure a new output.txt is created each time the program is run
        readInput(args); //reads the input and builds the network
        
        int randVertex = selectRandomVertex();  //gets the random vertex, which acts as the Bacon vertex
        System.out.println("The randomly selected vertex which is used as the Bacon vertex is " + randVertex);
        writeToFile("The randomly selected vertex which is used as the Bacon vertex is " + randVertex);

        double averageBaconNum = calculateAverageBaconNumber(randVertex);   //calculates the average bacon number from the Bacon vertex, adds it to the output file

        getUserInput(randVertex, averageBaconNum);   //take all user input, add it to the output file
    }
}
