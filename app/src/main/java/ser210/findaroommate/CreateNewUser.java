package ser210.findaroommate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.Support.PublicDBHelper;

public class CreateNewUser extends Activity {
    public static final int PICK_IMAGE = 1;
    private ArrayList<String> selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
    }

    public void onUploadImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    //When User Uploads an image, this is where we capture the file path.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            PublicDBHelper.uploadImage(UID,uri);
            ((ImageView)findViewById(R.id.profileImageDemo)).setImageURI(uri);
        }
    }


    public void ConfirmAccountCreate(View view) {
        if (validateForm((EditText) findViewById(R.id.field_firstname)) &&
                validateForm((EditText) findViewById(R.id.field_lastname)) &&
                validateForm((EditText) findViewById(R.id.field_housingpref)) &&
                //this.selectedImage != null &&
                validateForm((EditText) findViewById(R.id.field_phone))) {

            String firstName = ((EditText) findViewById(R.id.field_firstname)).getText().toString();
            String lastName = ((EditText) findViewById(R.id.field_lastname)).getText().toString();
            String housing = ((EditText) findViewById(R.id.field_housingpref)).getText().toString();
            String phone = ((EditText) findViewById(R.id.field_phone)).getText().toString();
            String desc = ((EditText) findViewById(R.id.DescField)).getText().toString();
            //create a new user
            User user = new User();
            user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            user.setLastName(lastName);
            user.setDescription(desc);
            user.setFirstName(firstName);
            user.setHousingPref(housing);
            user.setPhoneNumber(phone);
            user.setPartyPreference(((Spinner) findViewById(R.id.party_spinner)).getSelectedItemPosition());

            new PublicDBHelper().setNewUser(user); // add the user to the database.
            Intent intent = new Intent(this, ser210.findaroommate.HomeActivity.class);
            startActivity(intent);
            finish(); //prevent from being able to go back to this page.

        }
    }

    private boolean validateForm(EditText field) {
        boolean valid = true;

        String email = field.getText().toString();
        if (TextUtils.isEmpty(email)) {
            field.setError("Required.");
            valid = false;
        } else {
            field.setError(null);
        }

        return valid;
    }
}
