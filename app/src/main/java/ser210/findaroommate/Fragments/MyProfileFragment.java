package ser210.findaroommate.Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.R;
import ser210.findaroommate.Support.PublicDBHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    private FrameLayout _myProfileFrame;
    private FirebaseAuth mAuth;
    private PublicDBHelper publicDB;
    private String _userName;
    private String _userHousing;
    private int _userParty;
    private String _userDescription;
    private String _userPhone;

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
        //Initialize Firebase Auth.
        mAuth = FirebaseAuth.getInstance();
        publicDB = new PublicDBHelper();


        publicDB.findUser(mAuth.getCurrentUser().getUid(), new PublicDBHelper.findUserCallback() {
            @Override
            public void findUserCallBack(boolean b, User user) {
                Log.i("Loading Profile","Found " + String.valueOf(b) + " " + user.getUid());
                _userName = user.getFirstName() + " " + user.getLastName();
                _userHousing = user.getHousingPref();
                _userParty = user.getPartyPreference();
                _userDescription = user.getDescription();
                _userPhone = user.getPhoneNumber();

                Bundle bundle = new Bundle();
                bundle.putString("name", _userName);
                bundle.putString("housing", _userHousing);
                bundle.putInt("party", _userParty);
                bundle.putString("description", _userDescription);
                bundle.putString("phone", _userPhone);
                bundle.putString("UID",user.getUid());

                //Pass to the User Profile Fragment
                Fragment myProfilefrag = new UserProfileFragment();
                myProfilefrag.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(_myProfileFrame.getId(), myProfilefrag);
                ft.commit();
            }
        });


        return v;
    }

}
