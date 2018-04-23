package ser210.findaroommate.Support;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.net.URI;

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
    final public static String USER_LIST = "UserList";
    final public static String MATCH_PENDING_LIST = "MatchesPending";
    final public static String MAtCH_COMPLETED_LIST = "MatchesComplete";

    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    //UserList Methods
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

    public void findUser(final String uid, final findUserCallback CB) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(USER_LIST) && dataSnapshot.child(USER_LIST).hasChild(uid)) {
                    User user = dataSnapshot.getValue(User.class);
                    CB.findUserCallBack(true, user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CB.findUserCallBack(false, null);
            }
        });
    }

    //Add new Firebase User
    public boolean setNewAuthUser(FirebaseUser user) {
        String id = user.getUid();
        User rawUser = new User();
        rawUser.setUid(id);
        rawUser.setEmail(user.getEmail());
        this.setNewUser(rawUser);
        return true;
    }

    //uploadImages
    public static boolean uploadImage(String UID, Uri uri) {
        StorageReference targetRef = storageRef.child(UID);
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
    public static interface getUserImageFileCallBack{
        void Result(boolean result,File file, Uri uri, FileDownloadTask.TaskSnapshot taskSnapshot);
    }

    public void getUserFile(String UID, final getUserImageFileCallBack CB){
        try{
            final File localFile = File.createTempFile(UID, "jpg");
            StorageReference targetRef = storageRef.child(UID);
            targetRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    CB.Result(true,localFile,Uri.fromFile(localFile), taskSnapshot);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    CB.Result(false,null,null, null);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
