package ser210.findaroommate.Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import ser210.findaroommate.Support.MatchesCustomAdaptor;
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
        this.setListAdapt(context);

        return super.onCreateView(inflater, container, savedInstanceState);
    }




    private void setListAdapt(final Context context){
        new PublicDBHelper().getMatchedUsers(FirebaseAuth.getInstance().getCurrentUser().getUid(), new PublicDBHelper.userIDListCallback() {
            @Override
            public void userIDListCallback(ArrayList<String> UIDS, ArrayList<User> UserList) {
                Log.i("List",String.valueOf(UIDS.size()));
                myMatches = UserList;
                MatchesCustomAdaptor adapt = new MatchesCustomAdaptor(UserList,context);
                adapt.setButtonHandler(new MatchesCustomAdaptor.MatchesCustomAdaptorButtonFunctions() {
                    @Override
                    public void onDeletePress(View view, int pos, User user) {
                        DeleteButton(view, pos, user, context);
                    }

                    @Override
                    public void onViewPress(View view, int pos, User user) {
                        ViewButton(view, pos, user, context);
                    }
                });
                setListAdapter(adapt);
            }
        });
    }


    private void ViewButton(View view, int pos, User user, Context context){
        Log.i("Button","View: " + pos + " | " + user.toString());
        FragmentManager fm = getFragmentManager();
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle bundle = new Bundle();
        String name = user.getFirstName() + " " + user.getLastName();

        bundle.putString("name", ((name.isEmpty()||name.contentEquals(" "))?user.getUid(): name));
        bundle.putString("housing", user.getHousingPref());
        bundle.putInt("party", user.getPartyPreference());
        bundle.putString("description", user.getDescription());
        bundle.putString("phone", user.getPhoneNumber());
        bundle.putString("UID",user.getUid());

        fragment.setPrivatePhone(false); // we are matched with them, and therefore should see their phone number
        fragment.setShowContact(true);
        fragment.setArguments(bundle);

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
    private void DeleteButton(View view, int pos, User user, final Context context){
        Log.i("Button","Delete: " + pos + " | " + user.toString());
        new PublicDBHelper().removeMatch(FirebaseAuth.getInstance().getCurrentUser().getUid(), user.getUid(), new PublicDBHelper.removeMatchCallBack() {
            @Override
            public void removed(boolean b) {
                setListAdapt(context); // Refresh List
            }
        });
    }
}
