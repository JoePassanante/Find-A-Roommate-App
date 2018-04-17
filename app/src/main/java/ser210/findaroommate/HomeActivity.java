package ser210.findaroommate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ser210.findaroommate.Fragments.BrowseFragment;
import ser210.findaroommate.Fragments.HomeFragment;
import ser210.findaroommate.Fragments.MatchesFragment;
import ser210.findaroommate.Fragments.MyProfileFragment;

public class HomeActivity extends Activity {
    private int _currentPosition = 0;
    private String[] _titles;
    private Menu _menu;
    private Fragment _visibleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //variables
        _titles = getResources().getStringArray(R.array.titles);
    }


    //inflate action bar
    public boolean onCreateOptionsMenu(Menu menu){
        _menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        selectItem(_currentPosition);
        return super.onCreateOptionsMenu(menu);
    }


    //dealing with selected Item from menu
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
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
    private void selectItem(int position){
        Fragment fragment;
        MenuItem item = _menu.findItem(R.id.action_other);


        switch(position){
            case 0: //HOME
                item.setVisible(false);
                fragment = new HomeFragment();
                break;
            case 1: //MY PROFILE
                fragment = new MyProfileFragment();
                item.setTitle("Edit");
                item.setVisible(true);
                break;
            case 2: //BROWSE
                fragment = new BrowseFragment();
                item.setTitle("Contact");
                item.setVisible(true);
                break;
            case 3: //MATCHES
                fragment = new MatchesFragment();
                item.setTitle("Edit");
                item.setVisible(true);

                //--GET LIST OF MATCHES FROM LOCAL DATABASE--//
                //pass as an array list through bundle and arguments
                //and give it to the matches fragment

                break;
            default: //default home
                fragment = new HomeFragment();
                item.setVisible(false);
        }

        _visibleFragment = fragment;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        setActionBarTitle(position);

    }

    //set the screen title
    private void setActionBarTitle(int position){
        String title = "";
        if(position == 0){
            title = getResources().getString(R.string.app_name);
        } else {
            title = _titles[position - 1];
        }
        getActionBar().setTitle(title);
    }

    private void performOtherAction(){
        if(_visibleFragment instanceof MyProfileFragment){
            //--CODE FOR EDITING MY PROFILE--//
        }

        if(_visibleFragment instanceof BrowseFragment){
            //--CODE FOR CONTACTING THE CURRENT PICKED USER--//
        }

        if(_visibleFragment instanceof MatchesFragment){
            //CODE FOR EDITING MATCHES--//
        }


    }



}

