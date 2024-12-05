This program was written by Brady Napier for COP 5537 Final Project

This program predicts the likelihood of collaboration between two authors in a collaboration network. Authors are represented by
vertices and collaborations are represented by edges. In order to predict the likelihood of a collaboration one author is selected
at random and the average "Bacon number" for this vertex is found. The user then inputs other vertices to find the "Bacon number" of.
Using the average "Bacon number" from the randomly selected vertex and the "Bacon number" of the user selected vertex the likelihood of 
collaboration is calculated.

To run the program ensure that all files are located in the current working directory, then run:
    javac Bacons.java
    javac Bacons inputFileName

    inputFileName should be replaced by one of the five large network files provided:
        CA-AstroPh.txt
        CA-CondMat.txt
        CA-GrQc.txt
        CA-HepPh.txt
        CA-HepTh.txt
    
    The program will then randomly selected the "Bacon vertex" and calculate the average "Bacon number" from that vertex. Note: this may
    take a minute depending on the network size. The program will then ask the user to input a vertex, five recommended vertices are provided
    to help select a vertex that is in the network. Enter the vertex number and press enter. The program will then calculate the "Bacon number"
    from the user selected vertex and predict the likelihood of a collaboration between the "Bacon vertex" and user selected vertex. The program
    will ask the user if they would like to try another vertex? To continue press y or Y, to end the program press any other letter on the keyboard.
    If the user wishes to continue the program will ask teh user to input a vertex and the process will loop until the user decides to exit or an error
    occurs. After the user is done an output file (output.txt) is produced that shows all of the results from running the program, this file can be 
    used to compare the likelihood of a collaboration with a common author between different authors.
