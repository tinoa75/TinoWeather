package hr.totohost.tinoweather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LocationsActivity extends AppCompatActivity {

    LocationsFragment locationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

         locationsFragment = (LocationsFragment) getSupportFragmentManager().findFragmentById(R.id.locations_list_fragment);
    }


//    @Override
//    public void onViewCreated(MenuItem item) {
 //}




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if (item.getItemId() == R.id.menu_add) {
            onLocationAddAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLocationAddAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.add_location_dialog_title));
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);



        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Input validation

                if (input.getText().toString().trim().equals("")) {
                    input.requestFocus();
                }else {

                    dialog.dismiss();
                    String text = input.getText().toString();

                    new WeatherStore(LocationsActivity.this).addLocation(text);
                    locationsFragment.loadData();
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_locations, menu);
        return true;
    }
}
