package ser210.findaroommate.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import ser210.findaroommate.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    //--ADD OTHER VARIABLES AS YOU ADD THEM TO THE PROFILE--//
    //where is messy vs neat or late vs early?
    TextView _nameText;
    TextView _housingText;
    TextView _emailText;

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


        //--TEMPORATY VARIABLES SET WILL NEED TO SET THESE TO WHAT YOU GET FROM DATABASE--//
        _nameText.setText("Mary Sue"); //change to first name + " " + last name
        _housingText.setText("Cresent");




        return v;
    }

}
