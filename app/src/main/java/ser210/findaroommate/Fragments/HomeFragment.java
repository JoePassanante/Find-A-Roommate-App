package ser210.findaroommate.Fragments;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.util.ArrayList;

import ser210.findaroommate.R;
import ser210.findaroommate.Support.PublicDBHelper;


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

        //Change home image to profile picture for demo
        new PublicDBHelper().getUserImage(FirebaseAuth.getInstance().getCurrentUser().getUid(), new PublicDBHelper.getUserImageFileCallBack() {
            @Override
            public void Result(boolean result, File file, Uri uri, FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if(getView()==null){
                        Log.e("HomeFragment","Failed to get view");
                        return;
                    }
                    ((ImageView)getView().findViewById(R.id.background_image)).setImageURI(uri);
                }
        });

        //--TEMPORARY NOTIFICATIONS LIST--//
        _notifications = new ArrayList<String>();
        _notifications.add("Creators: Joseph Passanante & Alexandra Sazhin");
        _notifications.add("We created this app in the hope that we can help people find college" +
                " roommates easier. The focus was simplicity and accesability. We hope this app will" +
                " help you in your room finding!");
        _notifications.add("Use the action bar on the top right to navigate the app!");
        _notifications.add("Complete on 5/1/2018");

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