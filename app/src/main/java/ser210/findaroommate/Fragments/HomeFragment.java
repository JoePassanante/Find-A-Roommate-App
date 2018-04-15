package ser210.findaroommate.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ser210.findaroommate.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ListView _notificationsList;
    ArrayList<String> _notifications;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //--TEMPORARY NOTIFICATIONS LIST--//
        _notifications = new ArrayList<String>();
        _notifications.add("Notifications");
        _notifications.add("For the");
        _notifications.add("User");

        //populate List View
        _notificationsList = v.findViewById(R.id.notification_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                _notifications);
        _notificationsList.setAdapter(adapter);

        return v;
    }

}