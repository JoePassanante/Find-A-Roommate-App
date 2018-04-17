package ser210.findaroommate.Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ser210.findaroommate.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    FrameLayout _myProfileFrame;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        _myProfileFrame = v.findViewById(R.id.my_profile_frame);

        //--GET THE USER PROFILE FROM DATABASE--//
        //Pass to the User Profile Fragment
        Fragment myProfilefrag = new UserProfileFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(_myProfileFrame.getId(), myProfilefrag);
        ft.commit();


        return v;
    }

}
