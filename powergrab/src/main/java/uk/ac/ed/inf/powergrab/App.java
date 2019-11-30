package uk.ac.ed.inf.powergrab;

import java.io.*;
import java.net.*;
import org.apache.commons.io.IOUtils;
import com.mapbox.geojson.*;
import java.util.*;
import com.google.gson.*;
import java.time.*;

public class App {
	
    // Creates a list of stations out of the feature collection.
	public static ArrayList<ChargingStation> createStationList(FeatureCollection fc){ 
		
		ArrayList<ChargingStation> stations = new ArrayList<ChargingStation>();
		for (Feature f : fc.features()) {
			String id = f.getProperty("id").getAsString();
			float coins = f.getProperty("coins").getAsFloat(); 
			float power = f.getProperty("power").getAsFloat();
			String markerSymbol = f.getProperty("marker-symbol").getAsString();
			if (f.geometry().type().equals("Point")) {
				List<Double> listCoordinates = ((Point)f.geometry()).coordinates();
				Position coordinates = new Position(listCoordinates.get(1), listCoordinates.get(0));
				
				ChargingStation station = new ChargingStation(id, coins, power, markerSymbol, coordinates);
				stations.add(station);
			}			
		}
		return stations;
	}
	
	// Adds the path into the initial feature collection, as a line string.
	public static FeatureCollection outputPath(List<Position> path, FeatureCollection fc) {
		
		List<Point> pointsList = new ArrayList<Point>();
		for (Position position : path) {
			pointsList.add(Point.fromLngLat(position.longitude, position.latitude));
		}
		
		LineString pathLineString = LineString.fromLngLats(pointsList);
		Feature pathFeature = Feature.fromGeometry(pathLineString);
		
		List <Feature> featureList = fc.features();
		featureList.add(pathFeature);
		
		FeatureCollection fcReturn = FeatureCollection.fromFeatures(featureList);
		
		return fcReturn;
		
	}
	
	// Creates and outputs the files required.
	public static void createOutputFiles(FeatureCollection fc, String detailedMoves, String name) {
	    
	    File filetxt = new File(String.format("/Users/andreas2/Documents/outILP/%s.txt", name));
        
        //Create the text file
        try {
            if (filetxt.createNewFile())
            {
                System.out.println("Text file is created!");
            } else {
                System.out.println("Text file already exists.");
            }
            
          //Write Content
            FileWriter writertxt = new FileWriter(filetxt);
            writertxt.write(detailedMoves);
            writertxt.close();
            
            File filejson = new File(String.format("/Users/andreas2/Documents/outILP/%s.geojson", name));
            
            //Create the Json file
            if (filejson.createNewFile())
            {
                System.out.println("GeoJson file is created!");
            } else {
                System.out.println("GeoJson file already exists.");
            }
             
            //Write Content
            FileWriter writerjson = new FileWriter(filejson);
            writerjson.write(fc.toJson());
            writerjson.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
             
	}
	
	// Retrieves the map from the URL provided and returns it as a feature collection.
	private static FeatureCollection parseMap(String mapString) {
        
	    URL mapURL;
        try {
            mapURL = new URL(mapString);
            HttpURLConnection conn = (HttpURLConnection) mapURL.openConnection();
            
            InputStream inputStream = conn.getInputStream(); 
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "UTF-8"); //CHECK IF ADDING DEPENDENY IS ALLOWED!! APACHE!!
            String mapSource = writer.toString();
            
            FeatureCollection fc = FeatureCollection.fromJson(mapSource);
            
            return fc;
            
        } catch (MalformedURLException e) {
          //e.printStackTrace();
            System.out.println("URL not well formed");
        } catch (IOException e) {
          //e.printStackTrace();
            System.out.println("Invalid Input");
        }
        return null;
    }
    
	
    private static void playGame(String mapString, Drone drone, String name) {
        
        
        FeatureCollection fc = parseMap(mapString);
        
        // id the feature collection returned is not null, 
        if (fc != null) {
            
            // create a list of stations from the feature collection
            List<ChargingStation> listOfStations = createStationList(fc);
            Map map = new Map(listOfStations);
            // Call the function calculate moves to output the drone's path
            List<Position>  path = drone.calculateMoves(map);
            
            // Remove the final new line character in the string containing the details of each move
            String allMoves = drone.detailedMoves.trim();
//          for (int i = 0; i < path.size() - 2; i++) {
//              allMoves += String.format("%s,%s,%s,%s,%s,%f,%f\n", path.get(i).latitude, path.get(i).longitude, drone.directionHistory.get(i), path.get(i+1).latitude, path.get(i+1).longitude, drone.coinsHistory.get(i), drone.powerHistory.get(i));
//          }
//          int n = path.size() - 2;
//           allMoves += String.format("%s,%s,%s,%s,%s,%f,%f\n", path.get(n).latitude, path.get(n).longitude, drone.directionHistory.get(n), path.get(n+1).latitude, path.get(n+1).longitude, drone.coinsHistory.get(n), drone.powerHistory.get(n));
            
            // add the path to the initial feature collection
            FeatureCollection fcFinal = outputPath(path, fc); 
            
            System.out.println(fcFinal.toJson());
           
            // Create the output files
            createOutputFiles(fcFinal, allMoves, name);
        }
        else {
            return;
        }
        
    }
    
	

    public static void main(String[] args) {
		
//		String day = args[0];
//		String month = args[1];
//		String year = args[2];
//		double latitude = Double.parseDouble(args[3]);
//		double longitude = Double.parseDouble(args[4]);
//		int seed = Integer.parseInt(args[5]);
//		String droneMode = args[6];

	    String year = "2019";
        String month = "11";
        String day = "17";
        
        // Check if the date is valid and well formatted.
        try {
            LocalDate date  = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        } catch (DateTimeException e){
            System.out.println("Invalid date entered.");
            return;
        }
        
        // Create the string that is going to be used as the URL to retrieve the map.
        String mapString = String.format("http://homepages.inf.ed.ac.uk/stg/powergrab/%s/%s/%s/powergrabmap.geojson", year, month, day);
        //String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/2019/02/02/powergrabmap.geojson";
        
        // Check if the initial position is within the play area.
        Position p1 = new Position(55.944425, -3.188396);
        if (!p1.inPlayArea()) {
            System.out.println("Initial position is out of bounds.");
            return;
        }
        
        // Create a drone depending on the drone mode and seed entered. If the drone mode is neither 'stateless' of 'stateful', then output an error message.
        String droneMode  = "stateful";
        Drone drone;
        if(droneMode.equals("stateless")) {
            drone = new StatelessDrone(p1, 5678);
        }
        else if (droneMode.equals("stateful")){
            drone = new StatefulDrone(p1, 5678);
        }
        else {
            System.out.println("Invalid drone mode. Drone mode is either 'stateless' or 'statefull'.");
            return;
        }
        
	    double startTime = System.nanoTime();
	    
	    // Create the name of the game that is going to be used for the two output files.
	    String gameName = String.format("%s-%s-%s-%s", droneMode, day, month, year);
	    
	    
	    playGame(mapString, drone, gameName);
	   
		double duration = System.nanoTime() - startTime;
		System.out.println(duration/1000000000);

    }	
	
}
