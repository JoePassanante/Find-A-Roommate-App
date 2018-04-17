package ser210.findaroommate;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateProfileActivity extends Activity {
    EditText _housing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        //Edits
        _housing = (EditText) findViewById(R.id.housingEdit);
    }

    public void onCreateButtonClick(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        //--GET ALL OF THE TEXT FROM THE EDITS PUT INTO DATABASE--//
        String housing = _housing.getText().toString();
        //--LOG USER IN--//

        //start home activity
        startActivity(intent);


    }
}
