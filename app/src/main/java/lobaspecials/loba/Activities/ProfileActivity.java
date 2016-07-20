package lobaspecials.loba.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.R;
import lobaspecials.loba.Utilities.UserLocalStore;

public class ProfileActivity extends Activity implements View.OnClickListener {
    //Profile activity displays the logged in user's details and gives them options for their profile.

    //Layout variables.
    private TextView mUsername, mUserState, mUserCity;
    private TextView mChangeLocationLink, mAddCityLink, mAddBarLink, mContactUsLink, mBackLink;

    //Class Variables
    private UserLocalStore mUserLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Sets the layout variables to the layout objects
        mUsername = (TextView) findViewById(R.id.profile_username);
        mUserState = (TextView) findViewById(R.id.profile_state);
        mUserCity = (TextView) findViewById(R.id.profile_city);

        mBackLink = (TextView) findViewById(R.id.profile_back_button);
        mChangeLocationLink = (TextView) findViewById(R.id.change_location_link);
        mAddCityLink = (TextView) findViewById(R.id.profile_add_city_link);
        mAddBarLink = (TextView) findViewById(R.id.profile_add_bar_link);
        mContactUsLink = (TextView) findViewById(R.id.profile_contact_us_link);

        //Sets on click listeners to all the clickable items on the layout.
        mBackLink.setOnClickListener(this);
        mChangeLocationLink.setOnClickListener(this);
        mAddCityLink.setOnClickListener(this);
        mAddBarLink.setOnClickListener(this);
        mContactUsLink.setOnClickListener(this);

        //Instantiates the user local store
        mUserLocalStore = new UserLocalStore(this);

        //Calls apply user details to set the output to the user's details.
        applyUserDetails();
    }

    @Override
    public void onClick(View v) {
        //Switch Case For Reading The On Click Listeners
        switch (v.getId()) {
            case R.id.profile_back_button:
                //Calls to finish the activity when the back button is pressed.
                this.finish();
                break;

            case R.id.change_location_link:
                //Starts the activity to change the user's location.
                startActivity(new Intent(this, ChangeLocationActivity.class));
                break;

            case R.id.profile_add_city_link:
                //Starts the activity to make an add city request.
                startActivity(new Intent(this, AddCityActivity.class));
                break;

            case R.id.profile_add_bar_link:
                //Starts the activity to make an add bar request.
                startActivity(new Intent(this, AddBarActivity.class));
                break;

            case R.id.profile_contact_us_link:
                //Starts the activity to send and email to LoBa support.
                String subject = "Support Email";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"lobaspecials@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(intent);
                break;
        }
    }

    //Override method that is called when the activity resumes from being paused.
    @Override
    protected void onResume() {
        super.onResume();
        //Calls the apply user details method
        applyUserDetails();
    }

    //Private method that sets the layout text to the logged in user's details.
    private void applyUserDetails() {
        //Pulls the user's details from the local storage.
        User user = mUserLocalStore.getLoggedInUser();

        //Sets each output object to the user's data.
        mUsername.setText(user.getUserName().toString());
        mUserState.setText(user.getState());
        mUserCity.setText(user.getCity().toString());
    }
}
