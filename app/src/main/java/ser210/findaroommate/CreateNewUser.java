package ser210.findaroommate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.Support.PublicDBHelper;

public class CreateNewUser extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
    }

    public void ConfirmAccountCreate(View view) {
        if (validateForm((EditText) findViewById(R.id.field_firstname)) &&
                validateForm((EditText) findViewById(R.id.field_lastname)) &&
                validateForm((EditText) findViewById(R.id.field_housingpref)) &&
                validateForm((EditText) findViewById(R.id.field_phone))) {
            String firstName = ((EditText) findViewById(R.id.field_firstname)).getText().toString();
            String lastName = ((EditText) findViewById(R.id.field_lastname)).getText().toString();
            String housing = ((EditText) findViewById(R.id.field_housingpref)).getText().toString();
            String phone = ((EditText) findViewById(R.id.field_phone)).getText().toString();
            //create a new user
            User user = new User();
            user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setHousingPref(housing);
            user.setPhoneNumber(phone);

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
