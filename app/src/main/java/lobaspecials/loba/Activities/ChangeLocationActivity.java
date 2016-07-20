package lobaspecials.loba.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import lobaspecials.loba.Interfaces.CityTrigger;
import lobaspecials.loba.Interfaces.GetCityCallback;
import lobaspecials.loba.Interfaces.GetUserCallback;
import lobaspecials.loba.Objects.City;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.R;
import lobaspecials.loba.Singletons.CityCollection;
import lobaspecials.loba.Singletons.StateCollection;
import lobaspecials.loba.Utilities.Encryption;
import lobaspecials.loba.Utilities.ErrorMessage;
import lobaspecials.loba.Utilities.ServerRequests;
import lobaspecials.loba.Utilities.ServerRequestsConnector;
import lobaspecials.loba.Utilities.UserLocalStore;

public class ChangeLocationActivity extends Activity implements View.OnClickListener {
    //Change location activity allows the user to change the default location on their account.

    //Layout variables.
    private Spinner mStateSpinner, mCitySpinner;
    private EditText mEnteredPassword;
    private Button mSubmitButton;
    private ImageView mChangeLocationImage;

    //Variable to hold the selected cities id.
    private int mSelectedCityId;
    //Variable to hold the logged in user's data.
    private User mUser;

    //Class variables
    private UserLocalStore mUserLocalStore;
    private StateCollection mStateCollection;
    private CityCollection mCityCollection;
    private Encryption mEncryption;
    private ServerRequestsConnector mServerRequestsConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        //Sets the layout variables to the layout objects.
        mStateSpinner = (Spinner) findViewById(R.id.change_location_state_spinner);
        mCitySpinner = (Spinner) findViewById(R.id.change_location_city_spinner);

        mEnteredPassword = (EditText) findViewById(R.id.change_location_entered_password);

        mChangeLocationImage = (ImageView) findViewById(R.id.loba_change_location);
        mSubmitButton = (Button) findViewById(R.id.change_location_submit_button);
        mSubmitButton.setOnClickListener(this);
        mChangeLocationImage.setOnClickListener(this);

        mUserLocalStore = new UserLocalStore(this);

        mServerRequestsConnector = new ServerRequestsConnector();
        //Calls the load state spinner method.
        loadStateSpinner();
    }

    @Override
    public void onClick(View v) {
        //Override for the click of objects on the layout.

        //Switch statement checks to see which id is touched in the layout.
        switch (v.getId()) {
            case R.id.change_location_submit_button:
                //Validates the user's input.
                if (validateInputData(mEnteredPassword.getText().toString())) {
                    //If validation is successful the submit updated location method is called.
                    submitUpdatedLocation();
                } else {
                    //If validation is unsuccessful an error message will appear to let the user know they need to input their password.
                    String errorMessage = "Please Enter Your Password";
                    new ErrorMessage(ChangeLocationActivity.this, errorMessage);
                }
                break;

            case R.id.loba_change_location:
                InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    //Private method to load the state spinner.
    private void loadStateSpinner() {
        //Gets the singleton's data.
        mStateCollection = StateCollection.get();
        //Creates a string type list to store the state names.
        List<String> mStateNames = new ArrayList<>();
        //Loops through each state object in the StateCollection list.
        for (int i = 0; i < mStateCollection.getStates().size(); i++) {
            //Adds the name of each state from the StateCollection's list into the newly created string list.
            mStateNames.add(mStateCollection.getStates().get(i).getStateName());
        }

        //Loads The State Spinner With The Array Of States
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStateNames);
        mStateSpinner.setAdapter(adapter);
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                //Override Method For Selected A Different State In The State Spinner

                //Finds The Selected State's ID To Fetch The Cities Within That State
                String selectedState = mStateSpinner.getSelectedItem().toString();
                //Loops through each state to find the selected state from the spinner.
                for (int i = 0; i < mStateCollection.getStates().size(); i++) {
                    //Checks to see if the selected state name matches the index in the StateCollection's list.
                    if (mStateCollection.getStates().get(i).getStateName().equals(selectedState)) {
                        //Creates A New City With The StateID
                        City city = new City(mStateCollection.getStates().get(i).getStateId());
                        //Calls The pullCites Method And Sends The Cities Object Over
                        pullCities(city);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });
    }

    //Private method to make a server request and pull the cities data from the database.
    private void pullCities(City city) {
        //Calls connector class to make a server request to pull the cities based on the selected state.
        mServerRequestsConnector.pullCities(this, city, new CityTrigger() {
            //Override that triggers once the city data has been pulled from the database.
            @Override
            public void cityTrigger(City returnedCities) {
                //Calls the load city spinner method.
                loadCitySpinner();
            }
        });
    }

    //Private method to load the state spinner.
    private void loadCitySpinner() {
        //Gets the singleton's data.
        mCityCollection = CityCollection.get();
        //Creates a string type list to store the cities names.
        List<String> mCityNames = new ArrayList<>();
        //Loops through each city object in the CityCollection list.
        for (int i = 0; i < mCityCollection.getCities().size(); i++) {
            //Adds the name of each city from the CityCollection's list into the newly created string list.
            mCityNames.add(mCityCollection.getCities().get(i).getCityName());
        }

        //Loads The City Spinner With The Array Of Cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCityNames);
        mCitySpinner.setAdapter(adapter);
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                String selectedCity = mCitySpinner.getSelectedItem().toString();
                for (int i = 0; i < mCityCollection.getCities().size(); i++) {
                    if (mCityCollection.getCities().get(i).getCityName().equals(selectedCity)) {
                        //Gets the current cities id and stores it in the mSelectedCityId.
                        mSelectedCityId = mCityCollection.getCities().get(i).getCityId();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });
    }

    //Private method to validate the user's input.
    private boolean validateInputData(String password) {
        if (!password.isEmpty() && password != null) {
            //If the user input a password and didn't leave it null true will be returned.
            return true;
        }
        //If the input is empty or null false will be returned.
        return false;
    }

    //Private method to send the updated location and update the user's account.
    private void submitUpdatedLocation() {
        //Gets the logged in user's current data.
        User loggedInUser = mUserLocalStore.getLoggedInUser();

        //Takes the user's password and encrypts it.
        String password = mEnteredPassword.getText().toString();
        mEncryption = new Encryption(password);
        password = mEncryption.getResult();

        //Creates a new user with the new location they have selected.
        mUser = new User(loggedInUser.getUserName(), password, mCitySpinner.getSelectedItem().toString(), mStateSpinner.getSelectedItem().toString(), mSelectedCityId);

        //Makes a server request to update the user's profile.
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.updateUserLocationDataAsyncTask(mUser, new GetUserCallback() {
            //Override method that is called once the server request is completed.
            @Override
            public void done(User returnedUser) {
                //If the returned user is null
                if (returnedUser == null) {
                    //update user data method is called.
                    updateUserData();
                //If the returned user isn't null it will mean an error occurred and will prompt the user to try again.
                } else {
                    String errorMessage = "Error Updating Your Location. Please Try Again.";
                    String title = "Update Profile Error";
                    new ErrorMessage(ChangeLocationActivity.this, errorMessage, title);
                }
            }
        });
    }

    //Private method to update the user's logged in data by updating the user in the local storage of the phone.
    private void updateUserData() {
        //Makes a server request to fetch the new data from the database.
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataAsyncTask(mUser, new GetUserCallback() {
            //Override method that is called after the server request is completed.
            @Override
            public void done(User returnedUser) {
                //Stores the returned user into the local store.
                mUserLocalStore.storeUserData(returnedUser);
                //calls for the activity to finish.
                ChangeLocationActivity.this.finish();
            }
        });
    }
}
