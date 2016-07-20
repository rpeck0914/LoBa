package lobaspecials.loba.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import lobaspecials.loba.Fragments.BarNameFragment;
import lobaspecials.loba.Fragments.CityStateSpinnerFragment;
import lobaspecials.loba.Interfaces.LoadRecyclerViewCommunicator;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.R;
import lobaspecials.loba.Utilities.UserLocalStore;

public class LobaMainActivity extends FragmentActivity implements View.OnClickListener, LoadRecyclerViewCommunicator {

    //Private Variables For The Main Activity Layout
    private TextView mLoggedInName, mLogOutLink;
    private ImageView mMainImage;

    //Creates A Variable For UserLocalStore To Sore The Users Data Locally On Device
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loba_main);

        //Creates fragment managers for the two fragment containers.
        FragmentManager fm1 = getSupportFragmentManager();
        FragmentManager fm2 = getSupportFragmentManager();

        //Creates two fragments and sets them to their containers.
        Fragment fragment1 = fm1.findFragmentById(R.id.top_fragment_container);
        Fragment fragment2 = fm2.findFragmentById(R.id.bottom_fragment_container);

        //Loads fragment one container with it's fragment.
        if (fragment1 == null) {
            fragment1 = new CityStateSpinnerFragment();
            fm1.beginTransaction()
                    .add(R.id.top_fragment_container, fragment1)
                    .commit();
        }

        //Loads fragment two container with it's fragment.
        if (fragment2 == null){
            fragment2 = new BarNameFragment() ;
            fm2.beginTransaction()
                    .add(R.id.bottom_fragment_container, fragment2)
                    .commit();
        }

        //Sets The Layout Objects To Their Variables
        mLoggedInName = (TextView) findViewById(R.id.logged_in_name);

        mLogOutLink = (TextView) findViewById(R.id.log_out_link);

        mMainImage = (ImageView)findViewById(R.id.loba_main);

        //Instantiates UserLocalStore Variable To Save Logged In Users Data
        userLocalStore = new UserLocalStore(this);

        //On Click Listeners For Clicks On Buttons
        mMainImage.setOnClickListener(this);
        mLoggedInName.setOnClickListener(this);
        mLogOutLink.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //onStart Method Checks To See If User's Data Is Stored Locally And Logs Them In
        if(authenticate() == true) {
            displayUserDetails();
        } else {    //If Data Is Not Stored Locally, The Login Activity Is Called
            startActivity(new Intent(LobaMainActivity.this, Login.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onResume override method updates the location in the case it's been changed by calling the update ui method in city state spinner fragment.
        FragmentManager fm = getSupportFragmentManager();
        CityStateSpinnerFragment f1 = (CityStateSpinnerFragment) fm.findFragmentById(R.id.top_fragment_container);
        f1.updateUI();
    }

    //authenticate Method Checks To See If The User's Local Data Is Null, If So Loads The Login Intent
    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            return false;
        }
        return true;
    }

    //displayUserDetails Method Pulls The User Stored Locally And Sets The Layout To The User's Details
    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();

        mLoggedInName.setText(user.getUserName());
    }

    @Override
    public void onClick(View v) {
        //Switch Case For Reading The On Click Listeners
        switch (v.getId()) {
            case R.id.logged_in_name:
                startActivity(new Intent(this, ProfileActivity.class));
                break;

            case R.id.log_out_link:
                //If User Clicks Logout Button The Local Data Stored Is Cleared And Then Starts The Login Activity
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;

            case R.id.loba_main:
                InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    @Override
    public void loadRecyclerView(String data) {
        FragmentManager manager = getSupportFragmentManager();
        BarNameFragment f2 = (BarNameFragment) manager.findFragmentById(R.id.bottom_fragment_container);
        f2.runUpdate();
    }
}
