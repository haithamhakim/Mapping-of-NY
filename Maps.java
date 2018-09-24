import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.geom.Line2D;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
//Haytham Abdelhakim Mohamed
//Class ID 55

public class Maps extends JPanel {
    public HashMap<String,Graph.Edge> roads;
    public HashMap<String, Graph.Vertex> intersectionMap;
    public ArrayList<Graph.Vertex> shortPath;
    public boolean shortPathFlag = false;

    private double maxLat;
    private double minLat;
    private double maxLong;
    private double minLong;
    private double xScale;
    private double yScale;




    public Maps(HashMap<String, Graph.Edge> roads, HashMap<String, Graph.Vertex> intersectionMap, ArrayList<Graph.Vertex> shortPath) {
        this.roads = roads;
        this.intersectionMap = intersectionMap;
        this.shortPath = shortPath;

        // Sets parameters for board
        Map.Entry<String, Graph.Vertex> entry = intersectionMap.entrySet().iterator().next();
        Graph.Vertex start= entry.getValue();
        maxLat = start.Latitude;
        minLat = start.Latitude;
        maxLong = start.Longitude;
        minLong = start.Longitude;
        //SetPreferredSize

        setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.WHITE);


    }


    public void paintComponent(Graphics p) {

        setParameters();
        Graphics2D p2 = (Graphics2D) p;
        super.paintComponent(p2);
        p2.setStroke(new BasicStroke(1));


        xScale = this.getWidth() / (maxLong - minLong);
        yScale = this.getHeight() / (maxLat - minLat);


        double x1, y1, x2, y2;

        //Graphing Roads

        int colorFlag = 0; // to change color, it will alternate because even and odd numbers alternate and I am moding with 2

        for (Graph.Edge myRoad : roads.values()) {
            colorFlag++;
            if (colorFlag % 2 == 1)
                p2.setColor(Color.BLACK);
            else
                p2.setColor(Color.BLUE);


            scale();
            x1 = myRoad.getFirstIntersection().Longitude;
            y1 = myRoad.getFirstIntersection().Latitude;
            x2 = myRoad.getSecondIntersection().Longitude;
            y2 = myRoad.getSecondIntersection().Latitude;

            p2.draw(new Line2D.Double((x1 - minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), (x2 - minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));


        }

        if (shortPathFlag && !shortPath.isEmpty() && shortPath.size() > 1) {
            p2.setColor(Color.magenta);
            p2.setStroke(new BasicStroke(4));


            for (int x = 0; x < shortPath.size() - 1; x++) {
                x1 = shortPath.get(x).Longitude;
                y1 = shortPath.get(x).Latitude;


                x2 = shortPath.get(x + 1).Longitude;
                y2 = shortPath.get(x + 1).Latitude;


                p2.draw(new Line2D.Double((x1 - minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), (x2 - minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));

            }


        }
    }
        public void imageAdd() throws IOException {

            BufferedImage myPicture = ImageIO.read(new File("i-heart-ny_hero.jpg"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            add(picLabel);
        }

    public void setParameters(){

        for (Graph.Vertex Inter : intersectionMap.values()) {
            if (Inter.Latitude > maxLat)
                maxLat = Inter.Latitude;
            if (Inter.Latitude < minLat)
                minLat = Inter.Latitude;
            if (Inter.Longitude > maxLong)
                maxLong = Inter.Longitude;
            if (Inter.Longitude < minLong)
                minLong = Inter.Longitude;
        }

    }

    //Rescales the panel
    public void scale() {

        xScale = this.getWidth() / (maxLong - minLong);
        yScale = this.getHeight() / (maxLat - minLat);

    }



}