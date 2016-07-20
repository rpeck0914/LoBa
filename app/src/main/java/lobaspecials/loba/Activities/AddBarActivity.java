package lobaspecials.loba.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import lobaspecials.loba.Interfaces.EmailCallback;
import lobaspecials.loba.Interfaces.GetCityCallback;
import lobaspecials.loba.Objects.City;
import lobaspecials.loba.R;
import lobaspecials.loba.Singletons.CityCollection;
import lobaspecials.loba.Singletons.StateCollection;
import lobaspecials.loba.Utilities.EmailRequest;
import lobaspecials.loba.Utilities.ErrorMessage;
import lobaspecials.loba.Utilities.ServerRequests;
import lobaspecials.loba.Utilities.ServerRequestsConnector;

public class AddBarActivity extends Activity implements View.OnClickListener {
//Add bar activity is to allow the user to submit a bar to be added to the database.

    //Layout variables.
    private EditText mBarName, mBarAddress;
    private Button mSubmitBarButton;
    private Spinner mStateSpinner, mCitySpinner;
    private ImageView mAddBarImage;

    //Variables to get the singleton items over to this activity.
    private StateCollection mStateCollection;
    private CityCollection mCityCollection;
    private ServerRequestsConnector mServerRequestsConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bar);

        //Variables are attached to the layout items for this activity.
        mBarName = (EditText) findViewById(R.id.add_bar_bar_name);
        mBarAddress = (EditText) findViewById(R.id.add_bar_bar_address);

        mStateSpinner = (Spinner) findViewById(R.id.add_bar_state_spinner);
        mCitySpinner = (Spinner) findViewById(R.id.add_bar_city_spinner);

        mAddBarImage = (ImageView)findViewById(R.id.loba_add_bar);
        mSubmitBarButton = (Button) findViewById(R.id.add_bar_submit_button);
        mAddBarImage.setOnClickListener(this);
        mSubmitBarButton.setOnClickListener(this);

        mServerRequestsConnector = new ServerRequestsConnector();

        //Runs the method to load the state spinner with the states.
        loadStateSpinner();
    }

    @Override
    public void onClick(View v) {
        //Override for the click of objects on the layout.

        //Switch statement checks to see which id is touched in the layout.
        switch (v.getId()) {
            case R.id.add_bar_submit_button:
                //Sends the user's input to be validated that a bar name was actually input
                if(validateInputData(mBarName.getText().toString())) {
                    //If validation is successful the send bar submission method is called.
                    sendBarSubmission();
                } else {
                    //If no bar name was added a dialog will pop up alerting the user to enter a bar name.
                    String errorMessage = "Please Enter A Name And Address For The Bar";
                    new ErrorMessage(this, errorMessage);
                }
                break;

            case R.id.loba_add_bar:
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
        for(int i = 0; i < mCityCollection.getCities().size(); i++) {
            //Adds the name of each city from the CityCollection's list into the newly created string list.
            mCityNames.add(mCityCollection.getCities().get(i).getCityName());
        }

        //Loads The City Spinner With The Array Of Cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCityNames);
        mCitySpinner.setAdapter(adapter);
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                ////Override Method For When An Item Is Selected In The State Spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner
            }
        });
    }

    //Private method to validate the input data.
    private boolean validateInputData(String name) {
        //Checks to makes sure there is something entered in the variable and it is not null.
        if(!name.isEmpty() && name != null) {
            //If validation is good true is returned.
            return true;
        }
        //If validation is bad false is returned.
        return false;
    }

    //Private method to send and email out to lobaspecials displaying the user's input.
    private void sendBarSubmission() {
        //Sets the subject of the email.
        final String subject = "Add Bar Submission";
        //Sets the body of the email.
        String body = mBarName.getText().toString() + ", " + mBarAddress.getText().toString() + "\n\n" +
                    "State: " + mStateSpinner.getSelectedItem().toString() + "  City: " + mCitySpinner.getSelectedItem().toString();

        //Calls the EmailRequest class to send the email out.
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.sendEmailRequestAsyncTask(subject, body, new EmailCallback() {
            @Override
            public void emailSent(boolean sent) {
                //Override that is called after the email request has been made.

                //If email request was a success message saying so will be displayed to the user.
                if (sent) {
                    String title = "Add Bar Request:";
                    String message = "Your Request Has Been Received,\n" +
                            "The Bar Will Be Added Once It\n" +
                            "Has Been Verified.\n\n" +
                            "Thank You!";
                    submissionResult(title, message);

                //If email request was a failure for some reason an error message will display to the user.
                } else {
                    String errorTitle = "Add Bar Request:";
                    String errorMessage = "Your Request To Add A Bar Didn't\n" +
                            "Go Through Properly, Please Try\n" +
                            "Again.";
                    submissionResult(errorTitle, errorMessage);
                }
            }
        });
    }

    //private method for displaying the alert dialog to show if the bar request was a success or not.
    private void submissionResult(String title, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddBarActivity.this.finish();
            }
        });
        dialogBuilder.show();
    }
}
