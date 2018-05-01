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
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.R;
import ser210.findaroommate.Support.PublicDBHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends ListFragment {
    private ArrayList<User>myMatches;
    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = inflater.getContext();
        //--GET ARRAY LIST FROM ARGUMENTS AND SET IT TO _matches --//
        new PublicDBHelper().getMatchedUsers(FirebaseAuth.getInstance().getCurrentUser().getUid(), new PublicDBHelper.userIDListCallback() {
            @Override
            public void userIDListCallback(ArrayList<String> UIDS, ArrayList<User> UserList) {
                Log.i("List",String.valueOf(UIDS.size()));
                myMatches = UserList;
                ArrayAdapter<User> adapter = new ArrayAdapter<User>(
                        context,
                        android.R.layout.simple_list_item_1,
                        UserList);
                setListAdapter(adapter);
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(myMatches==null || myMatches.isEmpty())
            return;

        //ALEXANDRA ADD UI LOGIC HERE

    }
}
