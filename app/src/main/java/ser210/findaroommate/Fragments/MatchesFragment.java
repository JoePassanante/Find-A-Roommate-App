package ser210.findaroommate.Fragments;


import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ser210.findaroommate.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends ListFragment {
    ArrayList<String> _matches;

    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _matches = new ArrayList<String>();
        //--GET ARRAY LIST FROM ARGUMENTS AND SET IT TO _matches --//

        //temporary matches array
        _matches.add("List of");
        _matches.add("Matches");
        _matches.add("Here");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                _matches);
        setListAdapter(adapter);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
