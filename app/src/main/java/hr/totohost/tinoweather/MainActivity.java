package hr.totohost.tinoweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


// OpenWeatherMap API - Simulirati aplikaciju za prikaz temperature zraka i drugih osnovnih
// meteoroloških podataka. Omogućiti automatsko dohvaćanje lokacije. Podržavati više lokacija
// istovremeno, npr swipe-anjem ili pomoću navigation drawera.



public class MainActivity extends AppCompatActivity {

    private final String LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private final int PERMISSIONS_REQUEST_CODE = 1240;

    private LocationListener locationListener;
    private LocationManager locationManager;
    WeatherFragmentPager adapter;
    String lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = String.valueOf(location.getLatitude());
                lng = String.valueOf(location.getLongitude());

                generateWeatherPages();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        try {
            if (provider != null) {
                locationManager.requestLocationUpdates(provider, 1000, 1000, locationListener);
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Greška u dozvolama", Toast.LENGTH_SHORT).show();
        }
        ViewPager viewPager = findViewById(R.id.pager);
        adapter = new WeatherFragmentPager(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }


    private void generateWeatherPages() {
        adapter.clearFragments();
        ArrayList<String> locations = new WeatherStore(this).getLocations();

        // spremljene lokacije
        for (final String location : locations) {
            adapter.addFragment(new WeatherFragment(location));
        }

        // trenutna lokacija
         //  adapter.addFragment(new WeatherFragment(lat, lng));

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if (item.getItemId() == R.id.menu_locations) {
            onLocationListAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        generateWeatherPages();
    }

    public void onLocationListAction() {
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{LOCATION_PERMISSION}, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, LOCATION_PERMISSION)) {
                checkAndRequestPermissions();
            } else {
                Toast.makeText(this, "Molimo omogućite dozvole u postavkama", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            locationListener = null;
        }
    }
}
