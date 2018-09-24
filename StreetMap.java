import java.io.FileNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

//Haytham Abdelhakim Mohamed
//Class ID 55

public class StreetMap {
    public static void main(String[] args) throws FileNotFoundException {
        Graph myGraph = new Graph();

        boolean showMap = false;
        boolean shortPath = false;
        String startInt = "";
        String endInt = "";


        String mapFile = args[0]; //file name of the map

        //checks the command line arguments

        for (int j = 1; j < args.length; j++) {
            if (args[j].equals("--show"))
                showMap = true;
            else if (args[j].equals("--directions")) {
                shortPath = true;

                startInt = args[j + 1];
                endInt = args[j + 2];

            }


        }


        //Reading in file


        File f = new File(mapFile);
        Scanner scan = new Scanner(f);
        String[] info;
        //int numIntersections = 0;

        //Adding Intersections
        while (scan.hasNextLine()) {
            info = scan.nextLine().split("\t");

            if (info[0].equals("i")) {
                //numIntersections++;
                myGraph.addVertex(info[1], info[2], info[3]);
            } else {
                myGraph.addEdge(info[1], info[2], info[3]);

            }


        } //end of while loop
        scan.close();

        ArrayList<Graph.Vertex> path = new ArrayList<>();

        // If the shortPath is selected in terminal show the path and total distance in miles
        if (shortPath)
            path = myGraph.shortestPath(myGraph.nodeList.get(startInt), myGraph.nodeList.get(endInt));
        if (shortPath && !path.isEmpty()) {

            System.out.print("Path: ");


            for (Graph.Vertex myInter : path) {
                System.out.print(" " + myInter.getIntersectionID());

            }
            System.out.println();
            System.out.print("Miles traveled: ");
            System.out.printf("%.4f", path.get(path.size() - 1).distance);
            System.out.println();


        }


        //Shows map if indicated in the commmand line

        if (showMap) {
            Maps myGraphGUI = new Maps(myGraph.edgeList, myGraph.nodeList, path);


            if (shortPath)
                myGraphGUI.shortPathFlag = true;
            JFrame frame = new JFrame("Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            frame.getContentPane().add(myGraphGUI);
            frame.pack();
            frame.setVisible(true);

            if (mapFile.equals("nys.txt")) {
                Maps myMessage=new Maps(myGraph.edgeList, myGraph.nodeList, path);
                JFrame frame2 = new JFrame("Message");
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                try {
                    myMessage.imageAdd();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                frame2.getContentPane().add(myMessage);
                frame2.pack();
                frame2.setVisible(true);
            }


        }

    }

}