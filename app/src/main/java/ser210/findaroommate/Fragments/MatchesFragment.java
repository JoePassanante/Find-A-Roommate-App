package ser210.findaroommate.Fragments;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.R;
import ser210.findaroommate.Support.PublicDBHelper;


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
        final Context c = inflater.getContext();
        _matches = new ArrayList<String>();
        //--GET ARRAY LIST FROM ARGUMENTS AND SET IT TO _matches --//
        new PublicDBHelper().getMatchedUsers(FirebaseAuth.getInstance().getCurrentUser().getUid(), new PublicDBHelper.userIDListCallback() {
            @Override
            public void userIDListCallback(ArrayList<String> UIDS, ArrayList<User> UserList) {
                Log.i("List",String.valueOf(UIDS.size()));
                ArrayAdapter<User> adapter = new ArrayAdapter<User>(
                        c,
                        android.R.layout.simple_list_item_1,
                        UserList);
                setListAdapter(adapter);
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
