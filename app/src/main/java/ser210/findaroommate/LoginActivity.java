package ser210.findaroommate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ser210.findaroommate.Support.PublicDBHelper;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private PublicDBHelper publicDB;
    private FirebaseAuth mAuth;
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
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.verify_email_button).setOnClickListener(this);

        //Initialize database helper.
        publicDB = new PublicDBHelper();

        //Initialize Firebase Auth.
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
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
                            updateUI(user);
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
                            mStatusTextView.setText("Failed - Please make this a string resource BTW");
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
                            updateUI(user);
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
                            mStatusTextView.setText("Failed");
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
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

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
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText("");

            findViewById(R.id.main_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    /**
     * Contains logic responsible for moving the user into the main app after successfully logging in.
     * Also checks to make sure that there is a proper database reference to the UID.
     */
    private void nextScreen(){

        //For simplicity... we are going to let unverified users go through

      //  if(mAuth.getCurrentUser().isEmailVerified()!=true)
      //      return;

        //create the next intent, we do this outside of the callback class so we can acquire the current context.
        final Intent intent = new Intent(this, ser210.findaroommate.HomeActivity.class);

        //check to see if the user exists in the database
        //We use a callBack interface for this as we don't know how long it will take for the publicDBHelper to pull all users and check their properties.
        publicDB.findUser(mAuth.getCurrentUser().getUid(), new PublicDBHelper.findUserCallback() {
            @Override
            public void findUserCallBack(boolean b) {
                if(!b){
                    publicDB.setNewAuthUser(mAuth.getCurrentUser());
                    Log.i("NextScreen","User: " + mAuth.getCurrentUser().getEmail() + " was just created!");

                }else{
                    Log.i("NextScreen","User: " + mAuth.getCurrentUser().getEmail() + " exists in our database.");
                }
                startActivity(intent);
                //we don't want them going back to the login screen.
                finish();
            }
        });
    }

}




