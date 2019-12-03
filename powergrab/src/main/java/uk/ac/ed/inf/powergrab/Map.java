package uk.ac.ed.inf.powergrab;

import java.util.*;

public class Map {
    
    protected List<ChargingStation> stations;// = new ArrayList<>();
    protected List<ChargingStation> badStations;// = new ArrayList<>();
    protected List<ChargingStation> goodStations;// = new ArrayList<>();
    
    //public float sumOfGood;
    
    public Map(List<ChargingStation> stations) {
        this.stations = stations;
        this.separateStations(stations);
    }
    
    // Separate good and bad stations
    protected void separateStations(List<ChargingStation> stations) {
        
        this.goodStations = new ArrayList<ChargingStation>();
        this.badStations = new ArrayList<ChargingStation>();
        
        for(ChargingStation station : stations) {
            if (station.isSafe()) {
                this.goodStations.add(station);
                //this.sumOfGood += station.getCoins();
            }
            else{
                this.badStations.add(station);
            }
        }
        
    }
    
    

}
