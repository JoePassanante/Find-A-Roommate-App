package ser210.findaroommate;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.Support.PublicDBHelper;

public class PublicDBDataTest {
    @Test
    public static void main(String[]args){
        new PublicDBHelper().getListofUsers(new PublicDBHelper.userIDListCallback() {
            @Override
            public void userIDListCallback(ArrayList<String> UIDS, ArrayList<User> UserList) {
                for(String UID: UIDS){
                    Log.i("Testing UID",UID);
                }
            }
        });


    }

}
