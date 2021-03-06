package ser210.findaroommate.Support;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ser210.findaroommate.Models.Match;
import ser210.findaroommate.Models.User;

/**
 * Created by Joe Passanante on 4/12/2018.
 */
public class PublicDBHelper {
    final public static String USER_LIST = "UserList";
    final public static String MATCH_LIST = "CurrentMatches";
    final public static String USER_MATCH_REFERENCE = "UserMatchReference";

    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    //UserList Methods

    /**
     * @param user - Accepts a User to be added to be added to the firebase database
     * @return - returns boolean of success state
     */
    public boolean setNewUser(User user) {
        if (user == null || user.getUid() == null || user.getUid().isEmpty())
            return false;
        myRef.child(USER_LIST).child(user.getUid()).setValue(user);

        return true;
    }

    //findUser
    public static interface findUserCallback {
        void findUserCallBack(boolean b, User user);
    }

    /**
     * @param uid - User ID to find
     * @param CB  - Callback when user is found/not found
     */
    public void findUser(final String uid, final findUserCallback CB) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Find User", "User List Exists" + String.valueOf(dataSnapshot.hasChild(USER_LIST)));
                Log.i("Find User", "User List" + uid + " Exists" + String.valueOf(dataSnapshot.child(USER_LIST).hasChild(uid)));
                if (dataSnapshot.hasChild(USER_LIST) && dataSnapshot.child(USER_LIST).hasChild(uid)) {
                    User user = dataSnapshot.child(USER_LIST).child(uid).getValue(User.class);
                    CB.findUserCallBack(true, user);
                    Log.i("Find User", "User: " + user.getUid() + " Has been found");
                } else {
                    CB.findUserCallBack(false, null);
                    Log.i("Find User", "User: " + uid + " Has not been found1");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CB.findUserCallBack(false, null);
                Log.i("Find User", "User: " + uid + " Has not been found2");
                Log.i("Find User", "Auth: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                Log.i("Find User", "ERROR " + String.valueOf(databaseError.getCode()));
                Log.i("Find User", "ERROR " + String.valueOf(databaseError.getMessage()));
                Log.i("Find User", "ERROR " + String.valueOf(databaseError.getDetails()));
            }
        });
    }

    //
    public static interface userIDListCallback {
        /**
         * @param UIDS     - List of user ID's, if we only want to save strings (as saving entire users can get expensive)
         * @param UserList - List of User Objects
         */
        void userIDListCallback(ArrayList<String> UIDS, ArrayList<User> UserList);
    }

    /**
     * @param CB - Gets list of all the users in teh databse
     */
    public void getListofUsers(final userIDListCallback CB) {
        myRef.child(PublicDBHelper.USER_LIST).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> UIDList = new ArrayList<String>();
                ArrayList<User> UserList = new ArrayList<User>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    UIDList.add(childSnapshot.getKey());
                    UserList.add(childSnapshot.getValue(User.class));
                }
                CB.userIDListCallback(UIDList, UserList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Add new Firebase User

    /**
     * Creates a new user based of the FirebaseUser information. Leaves profile details empty.
     *
     * @param user - Firebase user
     * @return
     */
    public boolean setNewAuthUser(FirebaseUser user) {
        String id = user.getUid();
        User rawUser = new User();
        rawUser.setUid(id);
        rawUser.setEmail(user.getEmail());
        this.setNewUser(rawUser);
        return true;
    }

    //uploadImages

    /**
     * @param UID - User ID to upload image under
     * @param uri - Path to image that we are uploading
     * @return
     */
    public static boolean uploadImage(String UID, Uri uri) {
        StorageReference targetRef = storageRef.child("UserProfileImages/" + UID);
        UploadTask uploadTask = targetRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
        return true;
    }

    //getImagesOfUser
    public static interface getUserImageFileCallBack {
        /**
         * @param result       - True - Found USER image(False if default)
         * @param file         - File location of found image
         * @param uri          - Uri of image
         * @param taskSnapshot - taskSnapshot for reference
         */
        void Result(boolean result, File file, Uri uri, FileDownloadTask.TaskSnapshot taskSnapshot);
    }

    /**
     * @param UID - User ID to find image, will return with default image if not found
     * @param CB  - Call Back for when image is found.
     */
    public void getUserImage(String UID, final getUserImageFileCallBack CB) {
        try {
            final File localFile = File.createTempFile(UID, "jpg");
            StorageReference targetRef = storageRef.child("UserProfileImages/" + UID);
            targetRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    CB.Result(true, localFile, Uri.fromFile(localFile), taskSnapshot);
                    Log.i("ImageLoader", localFile.getAbsolutePath());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    loadDefaultImage(CB);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param CB - Callback method for getting placeholder image
     */
    private void loadDefaultImage(final getUserImageFileCallBack CB) {
        this.getUserImage("placeholder.png", new PublicDBHelper.getUserImageFileCallBack() {
            @Override
            public void Result(boolean result, File file, Uri uri, FileDownloadTask.TaskSnapshot taskSnapshot) {
                if (result == true) {
                    CB.Result(false, file, uri, null);
                }
            }
        });
    }

    //Matching Stuff
    /* How matches work:
        When a user matches with another user, they create a match object that is stored directly in the root of the database.
        When the match is created in the database, both users under their respective ID will find a match-reference to the match object stored
    */
    public static interface MatchListCallback {
        void matchIDListCallback(ArrayList<String> MatchIDs);
    }

    /**
     * @param UID
     * @param CB  - Used to get return of match ID's that contain given user's ID
     */
    public void getListofMatches(String UID, final MatchListCallback CB) {
        myRef.child(USER_MATCH_REFERENCE).child(UID).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> myReturn = new ArrayList<String>();
                //Get list of MatchID's
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    myReturn.add(child.getValue().toString());
                    Log.i("Return", child.getValue().toString());
                }
                CB.matchIDListCallback(myReturn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CB.matchIDListCallback(null);
            }
        });
    }

    //Match with users

    /**
     * This method checks to see if a match already exists, if it does than it simply updates the match to reflex that UID accepts Foreign UID
     * If no match is found, creates a new one where Foreign ID is unconfirmed
     *
     * @param foreignUID - User ID one to match with (Verified internally)
     * @param myUID      - User ID two to match with (Not Verified, expected to be own ID)
     */
    public void createNewMatch(final String foreignUID, final String myUID) {
        this.findUser(foreignUID, new findUserCallback() { //Check to see if the foreign user exists.. we assume myUID works
            @Override
            public void findUserCallBack(boolean b, final User user) {
                //At this point we know both Users exist
                final DatabaseReference userList = myRef.child(USER_MATCH_REFERENCE);
                final DatabaseReference matchList = myRef.child(MATCH_LIST);
                final ArrayList<Match> workingMatches = new ArrayList<Match>();
                //
                getListofMatches(myUID, new MatchListCallback() {
                    @Override
                    public void matchIDListCallback(final ArrayList<String> MatchIDs) {
                        if (!MatchIDs.isEmpty()) {
                            //We have a list of matches we need to check
                            //Save Matches
                            for (int currentIteration = 0; currentIteration < MatchIDs.size(); currentIteration++) {
                                final int x = currentIteration;
                                //Look up the match ID
                                findMatch(MatchIDs.get(currentIteration), new findMatchCallback() {
                                    @Override
                                    public void findMatchCallback(Match m) {
                                        int myIteration = x;
                                        if (m == null) //ID doesn't exist
                                            return;
                                        //If the match ID is there, we will call update match
                                        if (m.getUserIDOne().contentEquals(myUID) && m.getUserIDTwo().contentEquals(foreignUID)) { //check slot one
                                            Log.i("MatchSearch", "Match valid");
                                            updateMatch(foreignUID, m.isUserTwoConfirm(), myUID, true, m.getMyKey(), matchList);
                                            workingMatches.add(m);
                                        } else if (m.getUserIDTwo().contentEquals(myUID) && m.getUserIDOne().contentEquals(foreignUID)) {
                                            Log.i("MatchSearch", "Match valid");
                                            updateMatch(foreignUID, m.isUserOneConfirm(), myUID, true, m.getMyKey(), matchList);
                                            workingMatches.add(m);
                                        } else {
                                            Log.i("MatchSearch", "Match invalid");

                                            //check to see if we are at the end of the loop
                                            Log.i("My Iteration", String.valueOf(myIteration));
                                            //when we are the last match checked... see if we need to generate one
                                            if (myIteration == MatchIDs.size() - 1) {
                                                //check to see if we had any valid matches, if we did not that means all our current matches do not contain the user we
                                                //want to match with. Therefore, make a new match.
                                                if (workingMatches.isEmpty()) {
                                                    Log.i("CreateMatch", "Creating a new match");
                                                    makeANewMatch(foreignUID, myUID, matchList, userList);
                                                } else {
                                                    Log.i("CreateMatch", "No need to create a new match");
                                                }
                                            }

                                        }
                                    }
                                }); //END FIND MATCH
                            }// END FOR LOOP
                        } else {
                            makeANewMatch(foreignUID, myUID, matchList, userList);
                        }
                    }//END GET LIST OF MATCHES
                });
            }
        });
    }

    /**
     * @param foreignUID - User ID one to match with, THERE IS NO FACT CHECKING FOR THIS (Verify real ID before executing)
     * @param myUID      - User ID two that is matching, NO FACT CHECKING(Verify real ID before executing)
     * @param matchList  - Database reference to match list
     * @param userList   - Database reference to user list
     */
    private void makeANewMatch(String foreignUID, String myUID, DatabaseReference matchList, DatabaseReference userList) {
        //CREATE NEW MATCH
        String key = matchList.push().getKey();
        Match match = new Match(foreignUID, false, myUID, true);//really doesn't matter what user variable is who
        match.setMyKey(key);

        //Push the new Match
        matchList.child(key).setValue(match);

        //Add match reference to both users so they can edit the match status
        userList.child(myUID).child(userList.child(myUID).push().getKey()).setValue(key);
        userList.child(foreignUID).child(userList.child(foreignUID).push().getKey()).setValue(key);
    }

    /**
     * @param foreignID    - Foreign User ID that is being called on to match with
     * @param foreignState - Foreign state, as in if they have already confirmed or not
     * @param myID         - Current User ID
     * @param myState      - Current User State(should be true if trying to match with this foreign)
     * @param matchID      - The match ID of the currently existing match we are updating
     * @param matchList    - Database reference to Match List.
     */
    private void updateMatch(String foreignID, boolean foreignState, String myID, boolean myState, String matchID, DatabaseReference matchList) {
        Match m = new Match(foreignID, foreignState, myID, myState);
        m.setMyKey(matchID);
        matchList.child(matchID).setValue(m);
    }

    /**
     * Interface used as callback template for finding a match
     */
    public static interface findMatchCallback {
        void findMatchCallback(Match m);
    }

    /**
     * @param matchID - Match ID to find in database
     * @param CB      - Call back to return Match Object, when/ if match is found, else null
     */
    public void findMatch(final String matchID, final findMatchCallback CB) {
        myRef.child(MATCH_LIST).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(matchID)) {
                    CB.findMatchCallback(dataSnapshot.child(matchID).getValue(Match.class));
                } else {
                    CB.findMatchCallback(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CB.findMatchCallback(null);
            }
        });
    }

    /**
     * @param UID - User ID to look up matches
     * @param MCB - Call Back method when results are found.
     */
    public void getMatchedUsers(final String UID, final userIDListCallback MCB) {
        final ArrayList<String> UIDS = new ArrayList<String>();
        final ArrayList<User> Users = new ArrayList<User>();

        //Get list of all of the matches UID is involved in.
        getListofMatches(UID, new MatchListCallback() {
            @Override
            public void matchIDListCallback(final ArrayList<String> MatchIDs) {
                if (MatchIDs.isEmpty()) {
                    MCB.userIDListCallback(UIDS, Users);
                    return;
                }
                //Run through the list of matches
                for (int currentIteration = 0; currentIteration < MatchIDs.size(); currentIteration++) {
                    final int x = currentIteration;

                    //Find the match using MatchID
                    findMatch(MatchIDs.get(currentIteration), new findMatchCallback() {
                        int myIteration = x;

                        @Override
                        public void findMatchCallback(Match m) {
                            //Check if both users are confirmed
                            String searchUID = "";
                            if (m.isUserOneConfirm() && m.isUserTwoConfirm()) {
                                //Check which slot contains the foreign UID
                                if (m.getUserIDOne().contentEquals(UID)) { //if slot one is UID, slot 2 must be the foreign user
                                    searchUID = m.getUserIDTwo();
                                } else { //if we are not slot 1, we must be slot two, therefore foreign UID is in slot 1
                                    searchUID = m.getUserIDOne();
                                }
                            }
                            //Very last callback in the chain
                            findUser(searchUID, new findUserCallback() {
                                @Override
                                public void findUserCallBack(boolean b, User user) {
                                    if (b && user != null && !user.getUid().isEmpty()) {
                                        Users.add(user);
                                        UIDS.add(user.getUid());
                                    }
                                    Log.i("Get Confirm", "We at " + myIteration);
                                    if (myIteration == MatchIDs.size() - 1) {
                                        MCB.userIDListCallback(UIDS, Users);

                                    }
                                }
                            });//END Find USER

                        }
                    }); // END FIND MATCH CALL BACK
                }//END FOR LOOP
            }
        });//END GET LIST OF MATCHES CALLBACK
    }// End get matched users

    public static interface removeMatchCallBack{
        void removed(boolean b);
    }
    public void removeMatch(final String UID, final String ForeignUID, final removeMatchCallBack CB) {
        this.getListofMatches(UID, new PublicDBHelper.MatchListCallback() {
            @Override
            public void matchIDListCallback(ArrayList<String> MatchIDs) {
                for (String s : MatchIDs) {
                    findMatch(s, new PublicDBHelper.findMatchCallback() {
                        @Override
                        public void findMatchCallback(Match m) {
                            //check if match is correct
                            if (m.getUserIDOne().contentEquals(ForeignUID) && m.getUserIDTwo().contentEquals(UID)) {
                                updateMatch(ForeignUID,m.isUserOneConfirm(),UID,false,m.getMyKey(),myRef.child(PublicDBHelper.MATCH_LIST));
                                CB.removed(true);
                            } else if (m.getUserIDTwo().contentEquals(ForeignUID) && m.getUserIDOne().contentEquals(UID)) {
                                updateMatch(ForeignUID,m.isUserTwoConfirm(),UID,false,m.getMyKey(),myRef.child(PublicDBHelper.MATCH_LIST));
                                CB.removed(true);
                            }
                        }
                    });
                }
            }
        });
    }
}
