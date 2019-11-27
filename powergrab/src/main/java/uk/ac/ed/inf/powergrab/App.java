package uk.ac.ed.inf.powergrab;

import java.io.*;
import java.net.*;
import org.apache.commons.io.IOUtils;
import com.mapbox.geojson.*;
import java.util.*;
import com.google.gson.*;

public class App {
	
	public static ArrayList<ChargingStation> createStationList(FeatureCollection fc){ // new class
		
		ArrayList<ChargingStation> stations = new ArrayList<ChargingStation>();
		//List<ChargingStation> stations = new ArrayList<Position, ChargingStation>();
		for (Feature f : fc.features()) {
			String id = f.getProperty("id").getAsString();
			double coins = f.getProperty("coins").getAsDouble(); 
			double power = f.getProperty("power").getAsDouble();
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
	
	public static void createOutputFiles(FeatureCollection fc, String name) {
	    
	    File filetxt = new File(String.format("/Users/andreas2/Documents/outILP/%s.txt", name));
        
        //Create the file
        try {
            if (filetxt.createNewFile())
            {
                System.out.println("Text file is created!");
            } else {
                System.out.println("Text file already exists.");
            }
            
          //Write Content
            FileWriter writertxt = new FileWriter(filetxt);
            writertxt.write(fc.toJson());
            writertxt.close();
            
            File filejson = new File(String.format("/Users/andreas2/Documents/outILP/%s.geojson", name));
            
            //Create the file
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
    
	public static void main(String[] args) {
		
//		String day = args[0];
//		String month = args[1];
//		String year = args[2];
//		double latitude = Double.parseDouble(args[3]);
//		double longitude = Double.parseDouble(args[4]);
//		int seed = Integer.parseInt(args[5]);
//		String droneMode = args[6];
		
	   String year = "2020";
	   String month = "09";
	   String day = "02";
	   
		String mapString = String.format("http://homepages.inf.ed.ac.uk/stg/powergrab/%s/%s/%s/powergrabmap.geojson", year, month, day);
		//String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/2019/02/02/powergrabmap.geojson";
		
		try {
			URL mapURL = new URL(mapString);
			HttpURLConnection conn = (HttpURLConnection) mapURL.openConnection();
			
			InputStream inputStream = conn.getInputStream(); // newclass
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer, "UTF-8"); //CHECK IF ADDING DEPENDENY IS ALLOWED!! APACHE!!
			String mapSource = writer.toString();
			
			FeatureCollection fc = FeatureCollection.fromJson(mapSource);
//			System.out.println(fc.features());
			List<ChargingStation> StationsList = createStationList(fc);
			
			Position p1 = new Position(55.944425, -3.188396);
			String droneMode  = "stateful";
			Drone d1;
			if(droneMode.equals("stateless")) {
				d1 = new StatelessDrone(p1, 5678);
			}
			else {
				d1 = new StatefulDrone(p1, 5678);
			}
			
			List<Position>  path = d1.calculateMoves(StationsList);
			
			FeatureCollection fcFinal = outputPath(path, fc); 
			
			System.out.println(fcFinal.toJson());
			
			String name = String.format("%s-%s-%s-%s", droneMode, day, month, year);
			
			createOutputFiles(fcFinal, name);
		
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
