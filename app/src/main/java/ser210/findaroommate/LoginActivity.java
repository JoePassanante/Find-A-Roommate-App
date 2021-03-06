package ser210.findaroommate;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.Support.LocalDBHelper;
import ser210.findaroommate.Support.PublicDBHelper;


public class LoginActivity extends Activity implements View.OnClickListener {
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private PublicDBHelper publicDB;
    private FirebaseAuth mAuth;
    private LocalDBHelper localDB;
// ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set Views/Input fields
        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);

        // Buttons
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.createAccountButton).setOnClickListener(this);

        //Initialize database helper.
        publicDB = new PublicDBHelper();
        localDB = new LocalDBHelper(this);

        //Initialize Firebase Auth.
        mAuth = FirebaseAuth.getInstance();

        //Signout past User
        this.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Update UI with saved account
        localDB.open();
        LocalDBHelper.SavedLoginUser user = localDB.getSavedUser();
        if(user!=null){
            mEmailField.setText(user.email);
            mPasswordField.setText(user.password);
            ((CheckBox)findViewById(R.id.RememeberCheckBox)).setChecked(true);
        }else{
            Log.i("Load Saved User","No User Found");
            ((CheckBox)findViewById(R.id.RememeberCheckBox)).setChecked(false);
        }
        localDB.close();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.createAccountButton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.signInButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
    //account management
    //please note - the following code is being modeled directly off of Google's tutorial.

    /**
     *
     * @param email - Email String that is used for creating the account. If this already exists, than creation will fail.
     * @param password - Password string that will be used for the password for the account. This can be anything.
     */
    private void createAccount(String email,String password){
        Log.d("createAccount",email);
        if(!validateForm()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createAccount", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                      //      updateUI(user);
                            //go to main app screen
                            nextScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createAccount", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            mStatusTextView.setText("");
                        }
                    }
                });
    }

    /**
     *
     * @param email - Email String that is being used for sign in.
     * @param password - Password String that is beig used for sign in.
     */
    private void signIn(String email, String password) {
        Log.d("signIn", "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signIn", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                       //     updateUI(user);
                            //go to main app screen
                            nextScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            mStatusTextView.setText("Failed!");
                        }
                    }
                });
    }

    /**
     * Signs out the current user.
     */
    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    /**
     * Sends an email to the current user asking for them to verify their account. If they do, the Auth variable will reflect this action.
     */
    private void sendEmailVerification() {
        // Disable button

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("emailVerf", "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    /**
     * Makes sure that the current fields inputted are not null.
     * @return True - if the current filled out information is not null; False - If one of the fields is empty.
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    /**
     * Updates the UI to the current firbase User.
     * @param user - The current user to be passed to the UI
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.main_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);

        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText("");

            findViewById(R.id.main_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Contains logic responsible for moving the user into the main app after successfully logging in.
     * Also checks to make sure that there is a proper database reference to the UID.
     */
    private void nextScreen(){
        //Save login information if Remember account is clicked
        if(((CheckBox) findViewById(R.id.RememeberCheckBox)).isChecked()){
            localDB.open();
            localDB.setSavedUser(mEmailField.getText().toString(),mPasswordField.getText().toString());
            localDB.close();
        }else{
            localDB.open();
            localDB.removeAllUsers();
            localDB.close();
        }


        //create the next intent, we do this outside of the callback class so we can acquire the current context.
        final Intent homeintent = new Intent(this, ser210.findaroommate.HomeActivity.class);
        final Intent newintent = new Intent(this, ser210.findaroommate.CreateNewUser.class);
        //check to see if the user exists in the database
        //We use a callBack interface for this as we don't know how long it will take for the publicDBHelper to pull all users and check their properties.
        publicDB.findUser(mAuth.getCurrentUser().getUid(), new PublicDBHelper.findUserCallback() {
            @Override
            public void findUserCallBack(boolean b, User user) { //We will not be using User in this case.
                Log.i("NextScreen",String.valueOf(user==null));
                Log.i("NextScreen",String.valueOf(b));
                if(!b){
                    publicDB.setNewAuthUser(mAuth.getCurrentUser());
                    Log.i("NextScreen","User: " + mAuth.getCurrentUser().getEmail() + " was just created!");
                    startActivity(newintent);
                    finish(); //prevent from going back to this screen.
                }else{
                    Log.i("NextScreen","User: " + mAuth.getCurrentUser().getEmail() + " exists in our database.");
                    startActivity(homeintent);
                    //we don't want them going back to the login screen.
                    finish();
                }
            }
        });
    }

}




