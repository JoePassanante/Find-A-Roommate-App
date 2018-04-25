package ser210.findaroommate.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.R;
import ser210.findaroommate.Support.PublicDBHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    //--ADD OTHER VARIABLES AS YOU ADD THEM TO THE PROFILE--//
    //where is messy vs neat or late vs early?
    TextView _nameText;
    TextView _housingText;
    TextView _partyText;
    MultiAutoCompleteTextView _descriptionText;
    TextView _phoneText;

    PublicDBHelper publicDB;
    FirebaseAuth mAuth;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        //--ANY VARIABLES YOU ADD WILL NEED TO BE GOTTEN HERE FOR ACCESS--//
        _nameText = (TextView) v.findViewById(R.id.nameText);
        _housingText = (TextView) v.findViewById(R.id.housingText);
        _partyText = (TextView) v.findViewById(R.id.partyText);
        _descriptionText = (MultiAutoCompleteTextView) v.findViewById(R.id.descriptionText);
        _phoneText = (TextView) v.findViewById(R.id.phoneText);

        //Initialize database helper.
        publicDB = new PublicDBHelper();

        //Initialize Firebase Auth.
        mAuth = FirebaseAuth.getInstance();


        //--TEMPORATY VARIABLES SET WILL NEED TO SET THESE TO WHAT YOU GET FROM DATABASE--//
        _nameText.setText("Loading"); //change to first name + " " + last name
        _housingText.setText("Loading");
        _partyText.setText("Loading");
        //_descriptionText.setText("Loading");
        _phoneText.setText("Loading");

        //get from database and put it in
        publicDB.findUser(mAuth.getCurrentUser().getUid(), new PublicDBHelper.findUserCallback() {
            @Override
            public void findUserCallBack(boolean b, User user) {
           String name = user.getFirstName() + " " + user.getLastName();
            //_nameText.setText(name);
            //_housingText.setText(user.getHousingPref());
            //_partyText.setText(user.getPartyPreference());
            //_phoneText.setText(user.getPhoneNumber());
            }
        });


        return v;
    }

}
