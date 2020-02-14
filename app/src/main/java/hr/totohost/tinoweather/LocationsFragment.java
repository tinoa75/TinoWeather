package hr.totohost.tinoweather;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

}
