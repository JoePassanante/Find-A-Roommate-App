package ser210.findaroommate.Fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment implements View.OnClickListener{
    View _v;
    ImageButton _leftButton;
    ImageButton _rightButton;
    FrameLayout _profileFrame;

    public BrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _v = inflater.inflate(R.layout.fragment_browse, container, false);

        _leftButton = (ImageButton) _v.findViewById(R.id.leftButton);
        _leftButton.setOnClickListener(this);
        _rightButton = (ImageButton) _v.findViewById(R.id.rightButton);
        _rightButton.setOnClickListener(this);
        _profileFrame = (FrameLayout) _v.findViewById(R.id.profile_frame);

        //--GET FIRST USER FROM DATABASE AND PUT INTO NEW FRAGMENT--//
        Fragment firstProfile = new UserProfileFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(_profileFrame.getId(), firstProfile);
        ft.commit();


        return _v;

    }

    public User getUser(){
        //--PUT YOUR CODE TO GET A USER HERE--//

        return null;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == _leftButton.getId()){
            //--GET PREVIOUS USER FROM DATABASE--//
            //if no last do nothing show toast
        }

        if(v.getId() == _rightButton.getId()){
            //--GET NEXT USER FROM DATABASE--//
            //if no next do nothing show toast
        }


        //create brand new Profile Fragment
        Fragment profileFrag = new UserProfileFragment();
        Bundle bundle = new Bundle();
        //--PUT NEW USER INTO BUNDLE--//
        //array list of stuff?

        profileFrag.setArguments(bundle);
        //replace it
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(_profileFrame.getId(), profileFrag);
        ft.commit();



    }

    public void onButtonClick(View view){

    }
}
