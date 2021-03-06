package ser210.findaroommate.Fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.R;
import ser210.findaroommate.Support.PublicDBHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment implements View.OnClickListener {
    private View _v;
    private ImageButton _leftButton;
    private ImageButton _rightButton;
    private Button _matchButton;
    private FrameLayout _profileFrame;
    private FirebaseAuth mAuth;
    private PublicDBHelper publicDB;

    //DB loaded stuff
    private ArrayList<User>FinalUserList;
    private int currentView = 0;

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
        _matchButton = (Button)_v.findViewById(R.id.matchwithuser);
        _matchButton.setOnClickListener(this);
        _profileFrame = (FrameLayout) _v.findViewById(R.id.profile_frame);

        mAuth = FirebaseAuth.getInstance();
        publicDB = new PublicDBHelper();

        //time to get all the users we want to look at
        publicDB.getListofUsers(new PublicDBHelper.userIDListCallback() {
            @Override
            public void userIDListCallback(ArrayList<String> UIDS, ArrayList<User> UserList) {
                FinalUserList = UserList;
                if(UserList.size()>0)
                    onRightButtonClick(null);
            }
        });

        return _v;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == _leftButton.getId()) {
            this.onLeftButtonClick(v);
        }else
        if (v.getId() == _rightButton.getId()) {
            this.onRightButtonClick(v);
        }else
        if(v.getId() == this._matchButton.getId()){
            this.onMatchButtonClick(v);
        }
    }

    private User getNextUser() {
        this.currentView++;
        this.runIndexCheck();
        return this.FinalUserList.get(currentView);
    }
    private User getLastUser(){
        this.currentView--;
        this.runIndexCheck();
        return this.FinalUserList.get(currentView);
    }
    private void runIndexCheck(){
        if(this.currentView>=this.FinalUserList.size()){
            this.currentView = 0;
        }else if(this.currentView<0){
            this.currentView = FinalUserList.size()-1;
        }
    }

    private void loadUser(User user){
        //create brand new Profile Fragment
        UserProfileFragment profileFrag = new UserProfileFragment();
        Bundle bundle = new Bundle();
        String name = user.getFirstName() + " " + user.getLastName();

        bundle.putString("name", ((name.isEmpty()||name.contentEquals(" "))?user.getUid(): name));
        bundle.putString("housing", user.getHousingPref());
        bundle.putInt("party", user.getPartyPreference());
        bundle.putString("description", user.getDescription());
        bundle.putString("phone", user.getPhoneNumber());
        bundle.putString("UID",user.getUid());

        profileFrag.setPrivatePhone(true);
        profileFrag.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(_profileFrame.getId(), profileFrag);
        ft.commit();
    }

    private void onRightButtonClick(View view) {
        User n = getNextUser();
        Log.i("User Comp",n.getUid() + " | " + this.mAuth.getCurrentUser().getUid());
        //check to make sure that user doesnt equal ourself.
        if(n.getUid().contentEquals(mAuth.getCurrentUser().getUid())&& FinalUserList.size()>1){
            onRightButtonClick(view);
            return;
        }
        this.loadUser(n);
    }
    private void onLeftButtonClick(View view){
        User n = getLastUser();
        //check to make sure that user doesn't equal oneself.
        if(n.getUid().contentEquals(mAuth.getCurrentUser().getUid())&& FinalUserList.size()>1){
            onLeftButtonClick(view);
            return;
        }
        this.loadUser(n);
    }
    private void onMatchButtonClick(View view){
        //capture current user
        String matchedUser = this.FinalUserList.get(this.currentView).getUid();
        this.publicDB.createNewMatch(matchedUser, mAuth.getCurrentUser().getUid());
    }
}
