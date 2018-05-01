package ser210.findaroommate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import ser210.findaroommate.Fragments.BrowseFragment;
import ser210.findaroommate.Fragments.HomeFragment;
import ser210.findaroommate.Fragments.MatchesFragment;
import ser210.findaroommate.Fragments.MyProfileFragment;
import ser210.findaroommate.Models.User;
import ser210.findaroommate.Support.PublicDBHelper;

public class HomeActivity extends Activity {
    private int _currentPosition = 0;
    private String[] _titles;
    private Menu _menu;
    private Fragment _visibleFragment;
    private User me;
    private PublicDBHelper publicDB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //variables
        _titles = getResources().getStringArray(R.array.titles);

        //Initialize database helper.
        publicDB = new PublicDBHelper();

        //Initialize Firebase Auth.
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        publicDB.findUser(mAuth.getCurrentUser().getUid(), new PublicDBHelper.findUserCallback() {
            @Override
            public void findUserCallBack(boolean b, User user) {
                setUser(user);
            }
        });
    }

    private void setUser(User user) {
        this.me = user;
    }

    //inflate action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        _menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        selectItem(_currentPosition);
        return super.onCreateOptionsMenu(menu);
    }


    //dealing with selected Item from menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:

                selectItem(0);
                return true;
            case R.id.action_profile:

                selectItem(1);
                return true;
            case R.id.action_browse:

                selectItem(2);
                return true;
            case R.id.action_matches:
                selectItem(3);
                return true;

            case R.id.action_other:
                //what to do with the other action switches based on current fragment
                performOtherAction();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //deal with selected item
    private void selectItem(int position) {
        Fragment fragment;
        MenuItem item = _menu.findItem(R.id.action_other);


        switch (position) {
            case 0: //HOME
                fragment = new HomeFragment();
                item.setTitle("Log Out");
                item.setVisible(true);
                break;
            case 1: //MY PROFILE
                fragment = new MyProfileFragment();
                item.setTitle("Edit");
                item.setVisible(true);
                break;
            case 2: //BROWSE
                fragment = new BrowseFragment();
                //item.setTitle("Contact");
                item.setVisible(false);
                break;
            case 3: //MATCHES
                fragment = new MatchesFragment();
                //item.setTitle("Edit");
                item.setVisible(false);

                //--GET LIST OF MATCHES FROM LOCAL DATABASE--//
                //pass as an array list through bundle and arguments
                //and give it to the matches fragment

                break;
            default: //default home
                fragment = new HomeFragment();
                item.setVisible(false);
                item.setTitle("Log Out");
                item.setVisible(true);
        }

        _visibleFragment = fragment;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        setActionBarTitle(position);

    }

    //set the screen title
    private void setActionBarTitle(int position) {
        String title = "";
        if (position == 0) {
            title = getResources().getString(R.string.app_name);
        } else {
            title = _titles[position - 1];
        }
        getActionBar().setTitle(title);
    }

    private void performOtherAction() {
        if (_visibleFragment instanceof MyProfileFragment) {
            //--CODE FOR EDITING MY PROFILE--//
            Intent intent = new Intent(this, EditUserActivity.class);
            intent.putExtra("first_name", me.getFirstName());
            intent.putExtra("last_name", me.getLastName());
            intent.putExtra("housing_pref", me.getHousingPref());
            intent.putExtra("phone_num", me.getPhoneNumber());
            intent.putExtra("descrip", me.getDescription());
            intent.putExtra("party_pref", me.getPartyPreference());
            startActivity(intent);
        }

        if (_visibleFragment instanceof BrowseFragment) {
            //--CODE FOR CONTACTING THE CURRENT PICKED USER--//
        }

        if (_visibleFragment instanceof MatchesFragment) {
            //CODE FOR EDITING MATCHES--//
        }
        //if we are home.. than we probably want to be able to log out!
        if (_visibleFragment instanceof  HomeFragment){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, ser210.findaroommate.LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }


}

