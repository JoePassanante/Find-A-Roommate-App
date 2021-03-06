package ser210.findaroommate.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;

import org.w3c.dom.Text;

import java.io.File;

import ser210.findaroommate.Models.User;
import ser210.findaroommate.R;
import ser210.findaroommate.Support.PublicDBHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {
    private TextView _nameText, _housingText, _partyText, _descriptionText, _phoneText;

    private String name = " ";
    private String housing = " ";
    private int party = 0;
    private String description = " ";
    private String phone = " ";
    private String UserID;
    private boolean privatePhone = false;
    private boolean showContact = false;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        //--ANY VARIABLES YOU ADD WILL NEED TO BE GOTTEN HERE FOR ACCESS--//
        _nameText = (TextView) v.findViewById(R.id.nameText);
        _housingText = (TextView) v.findViewById(R.id.housingText);
        _partyText = (TextView) v.findViewById(R.id.partyText);
        _descriptionText = (TextView) v.findViewById(R.id.descriptionText);
        _phoneText = (TextView) v.findViewById(R.id.phoneText);


        Bundle args = getArguments();
        name = args.getString("name");
        housing = args.getString("housing");
        party = args.getInt("party");
        description = args.getString("description");
        phone = args.getString("phone");
        UserID = args.getString("UID");

        //make sure party variable is within index
        party = (party < 0 || party > getResources().getStringArray(R.array.PartyOptions).length-1) ? 0 : party;

        _nameText.setText(name); //change to first name + " " + last name
        _housingText.setText(housing);
        _partyText.setText(getResources().getStringArray(R.array.PartyOptions)[party]);
        _descriptionText.setText(description);
        _phoneText.setText(phone);
        if (this.privatePhone) {
            _phoneText.setVisibility(View.GONE);
            ((TextView) v.findViewById(R.id.phone_title)).setVisibility(View.GONE);
            ((Button) v.findViewById(R.id.contactUser)).setVisibility(View.GONE);
        } else {
            _phoneText.setVisibility(View.VISIBLE);
            ((TextView) v.findViewById(R.id.phone_title)).setVisibility(View.VISIBLE);
            if (this.showContact) {
                ((Button) v.findViewById(R.id.contactUser)).setVisibility(View.VISIBLE);
                ((Button) v.findViewById(R.id.contactUser)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                        sendIntent.setData(Uri.parse("smsto:" + phone));
                        sendIntent.putExtra("sms_body", "Hello " + name + " I have matched with you!");
                        startActivity(sendIntent);
                    }
                });
            }
        }

        loadImage(this.UserID, ((ImageView) v.findViewById(R.id.user_image)));


        return v;
    }

    private void loadImage(String UID, final ImageView image) {
        new PublicDBHelper().getUserImage(UID, new PublicDBHelper.getUserImageFileCallBack() {
            @Override
            public void Result(boolean result, File file, Uri uri, FileDownloadTask.TaskSnapshot taskSnapshot) {
                image.setImageURI(uri);
            }
        });
    }

    public boolean isPrivatePhone() {
        return privatePhone;
    }

    public void setPrivatePhone(boolean privatePhone) {
        this.privatePhone = privatePhone;
    }

    public boolean isShowContact() {
        return showContact;
    }

    public void setShowContact(boolean showContact) {
        this.showContact = showContact;
    }
}
