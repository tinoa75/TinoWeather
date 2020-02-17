package hr.totohost.tinoweather;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationsFragment extends ListFragment {

    private ArrayAdapter adapter;
    private ArrayList<String> location_list = new ArrayList<>();

    public LocationsFragment() {
        // Required empty public constructor
    }

    void loadData() {
        location_list.clear();
        location_list.addAll(new WeatherStore(getActivity()).getLocations());
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new ArrayAdapter<>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1, location_list);
        setListAdapter(adapter);
        loadData();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // Delete location
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> av, View v, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("Jeste li sigurni da zelite obrisati lokaciju?");
                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new WeatherStore(getActivity()).removeLocation(location_list.get(position));
                                loadData();
                                dialog.dismiss();

                            }
                        });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;
            }
        });
    }






}
