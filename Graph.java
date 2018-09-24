import javax.sound.sampled.Line;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.awt.geom.Line2D;
import java.util.Map;

//Haytham Abdelhakim Mohamed
//Class ID 55

public class Graph {
    HashMap<String, Vertex> nodeList = new HashMap<>();
    HashMap<String, Edge> edgeList = new HashMap<>();

    public class Vertex implements Comparable<Vertex> {
        String ID;
        double Longitude;
        double Latitude;
        double distance;
        boolean visited;
        ArrayList<Vertex> adjacencyList;
        Vertex previousIntersection;

        public Vertex(String ID, double Latitude, double Longitude) {
            this.ID = ID;
            this.Longitude = Longitude;
            this.Latitude = Latitude;
            this.adjacencyList = new ArrayList<>();
        }

        public boolean isVisited() {
            return visited;
        }

        public String getIntersectionID() {
            return ID;
        }


        public int compareTo(Vertex n) {
            if (this.distance < n.distance) {
                return -1;
            } else if (this.distance > n.distance) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public class Edge {
        String name;
        Vertex intersection1;
        Vertex intersection2;
        double weight;


        public Edge(String name, Vertex intersect1, Vertex intersect2) {
            this.name = name;
            this.intersection1 = intersect1;
            this.intersection2 = intersect2;
            this.weight = distance(intersect1.Latitude, intersect1.Longitude, intersect2.Latitude, intersect2.Longitude);
        }

        public double getWeight() {
            return weight;
        }

        public Vertex getFirstIntersection() {
            return intersection1;
        }

        public Vertex getSecondIntersection() {
            return intersection2;
        }

    }

    public void addVertex(String IDIntersection, String lat, String longi) {
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(longi);
        nodeList.put(IDIntersection, new Vertex(IDIntersection, latitude, longitude));

    }

    public void addEdge(String roadID, String firstInterID, String secondInterID){

        String edgy = firstInterID.concat(secondInterID);
        edgeList.put(edgy,new Edge(roadID,nodeList.get(firstInterID),nodeList.get(secondInterID)));
        //Updating AdjacencyList of both Intersections
        nodeList.get(firstInterID).adjacencyList.add(nodeList.get(secondInterID));
        nodeList.get(secondInterID).adjacencyList.add(nodeList.get(firstInterID));


    }
        /* Getting the distance between two Intersections in miles using Haversine formula

    https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
     */

        private double distance(double lat1, double lon1, double lat2, double lon2) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 0.8684;
            return (dist);
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        /*::  This function converts decimal degrees to radians             :*/
        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        /*::  This function converts radians to decimal degrees             :*/
        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }


    public ArrayList<Vertex> shortestPath(Vertex startInt, Vertex endInt){
        ArrayList<Vertex> path = new ArrayList<>();

        // In the case that startInt and endInt are the same
        if(startInt.getIntersectionID().equals(endInt.getIntersectionID())){
            path.add(startInt);
            return path;
        }

        // Iterating through hashMap and setting the distance/cost of every intersection to infinity
        for (Vertex Inter : nodeList.values()) {
            Inter.distance = Double.POSITIVE_INFINITY;
        }


        //Setting startInt's cost to zero
        startInt.distance = 0;

        //Making a priorityQueue and adding startInt to it
        PriorityQueue<Vertex> pqueue = new PriorityQueue<>();
        pqueue.add(startInt);

        while (!pqueue.isEmpty()) {
            //Dequeue the intersection with the highestPriority/lowest cost
            Vertex currentInt = pqueue.poll();

            //Creating an arrayList of unVisited Neighbors of the intersection
            ArrayList<Vertex> unVisited = new ArrayList<>();

            for (int i = 0; i < currentInt.adjacencyList.size(); i++) {
                if (!currentInt.adjacencyList.get(i).isVisited())
                    unVisited.add(currentInt.adjacencyList.get(i));
            }

            double cost;

            for (int k = 0; k < unVisited.size(); k++) {
                cost = currentInt.distance + weightEdge(currentInt, unVisited.get(k));
                if (cost < unVisited.get(k).distance) {
                    unVisited.get(k).distance = cost;

                    //update priorityQueue
                    if (!pqueue.contains(unVisited.get(k)))
                        pqueue.add(unVisited.get(k));

                    //Set previous pointer to currentVert
                    unVisited.get(k).previousIntersection = currentInt;

                }

            }

        }

        //Creating empty ArrayList of Intersections
        ArrayList<Vertex> emptyPath = new ArrayList<>();

        Vertex startPath = endInt;

        path.add(endInt);
        while (startPath != startInt){

            //In the case where the intersections aren't connected
            if (startPath.previousIntersection == null)
                return emptyPath;
            path.add(startPath.previousIntersection);

            //Go to previous Intersection
            startPath = startPath.previousIntersection;

        }

        Collections.reverse(path); // Reverse the path
        return path;

    }

    public double weightEdge(Vertex firstInter, Vertex secondInter){
        String firstInterID = firstInter.getIntersectionID();
        String secondInterID = secondInter.getIntersectionID();

        String edgy1 = firstInterID.concat(secondInterID); // edge(Inter1, Inter2) == edge(Inter2, Inter1)
        String edgy2 = secondInterID.concat(firstInterID);
        if (edgeList.containsKey(edgy1))
            return edgeList.get(edgy1).getWeight();
        else
            return edgeList.get(edgy2).getWeight();


    }

}