package ser210.findaroommate.Support;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ser210.findaroommate.Models.User;

/**
 * Created by Joe Passanante on 4/12/2018.
 */
/*
    UserListDataStructure:

    -ID
        -User.class

 */
public class PublicDBHelper {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    //UserList Methods
    public boolean setNewUser(User user){
        if(user==null || user.getUid()==null || user.getUid().isEmpty())
            return false;
        myRef.child("UserList").child(user.getUid()).setValue(user);

        return true;
    }
    //findUser
    public static interface findUserCallback{
        void findUserCallBack(boolean b);
    }

    public void findUser(final String uid, final findUserCallback CB){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("UserList") && dataSnapshot.child("UserList").hasChild(uid)){
                   CB.findUserCallBack(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CB.findUserCallBack(false);
            }
        });
    }

    //Add new Firebase User
    public boolean setNewAuthUser(FirebaseUser user){
        String id = user.getUid();
        User rawUser = new User();
        rawUser.setUid(id);
        rawUser.setEmail(user.getEmail());
        this.setNewUser(rawUser);
        return true;
    }
}
