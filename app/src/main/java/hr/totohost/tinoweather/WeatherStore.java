package hr.totohost.tinoweather;

import android.content.Context;

import java.util.ArrayList;
import com.mukesh.tinydb.TinyDB;

class WeatherStore {
    private TinyDB tinydb;
    private static final String LOCATIONS_KEY = "locations";

    WeatherStore(Context context) {
        tinydb = new TinyDB(context);
    }

    void addLocation(String location) {
        ArrayList<String> locations = tinydb.getListString(LOCATIONS_KEY);

        // ne postoji array, inicijaliziraj novi
        if(locations == null) {
            locations = new ArrayList<>();
        }

        locations.add(location);

        tinydb.putListString(LOCATIONS_KEY, locations);
    }


    ArrayList<String> getLocations() {
        return tinydb.getListString(LOCATIONS_KEY);
    }
}
