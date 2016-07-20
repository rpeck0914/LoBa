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
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import lobaspecials.loba.Interfaces.EmailCallback;
import lobaspecials.loba.R;
import lobaspecials.loba.Singletons.StateCollection;
import lobaspecials.loba.Utilities.EmailRequest;
import lobaspecials.loba.Utilities.ErrorMessage;

public class AddCityActivity extends Activity implements View.OnClickListener {
    //Add city activity submits a user's input to request a city be added to the database.

    //Layout variables.
    private TextView mStateText, mCityText, mAddCityTitle;
    private EditText mCityEntered;
    private Spinner mStateSpinner;
    private Button mSubmitCityButton;
    private ImageView mAddCityImage;

    //String to hold the selected state.
    private String mStateSelected;

    //Pulls the collection of states stored in the singleton.
    private StateCollection mStateCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        //Links the layout variables to the class variables.
        mAddCityTitle = (TextView) findViewById(R.id.add_city_request_title);
        mStateText = (TextView) findViewById(R.id.state_title_add_city_activity);
        mStateSpinner = (Spinner) findViewById(R.id.state_spinner_add_city_activity);
        mCityText = (TextView) findViewById(R.id.city_title_add_city_activity);

        mCityEntered = (EditText) findViewById(R.id.city_enter_add_city_activity);

        mSubmitCityButton = (Button) findViewById(R.id.submit_city_button);

        mAddCityImage = (ImageView) findViewById(R.id.loba_add_city);

        mAddCityImage.setOnClickListener(this);
        mSubmitCityButton.setOnClickListener(this);

        //Calls the loadStateSpinner method to populate the state spinner.
        loadStateSpinner();
    }

    @Override
    public void onClick(View v) {
        //Override for the click of objects on the layout.

        //Switch statement checks to see which id is touched in the layout.
        switch (v.getId()) {
            case R.id.submit_city_button:
                //Validates the input to make sure a user input a city name.
                if(validateInputData(mCityEntered.getText().toString())) {
                    //If validation is successful, send city submission method is called.
                    sendCitySubmission();
                } else {
                    //If validation is not successful an error message will prompt the user to enter a city name.
                    String errorMessage = "Please Enter A City Name";
                    new ErrorMessage(this, errorMessage);
                }
                break;

            case R.id.loba_add_city:
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
                String selectedState = mStateSpinner.getSelectedItem().toString();
                for (int i = 0; i < mStateCollection.getStates().size(); i++) {
                    if (mStateCollection.getStates().get(i).getStateName().equals(selectedState)) {
                        mStateSelected = mStateCollection.getStates().get(i).getStateName();
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

    //Private method to validate the user input.
    private boolean validateInputData(String cityname) {
        //Checks to make sure the user input something.
        if(!cityname.isEmpty() && cityname != null) {
            //If validation is successful true is returned.
            return true;
        }
        //If validation is not successful false is returned.
        return false;
    }

    //Private method to send the input data out in an email to lobaspecials.
    private void sendCitySubmission() {
        //Creates the subject of the email.
        final String subject = "Add City Submission";
        //Concatenates the body of the email.
        String body = mCityEntered.getText().toString() + ", " + mStateSelected;

        //Calls the Email Request to send the email out.
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.sendEmailRequestAsyncTask(subject, body, new EmailCallback() {
            @Override
            public void emailSent(boolean sent) {
                if (sent) {
                    //If email was successfully sent a message will prompt the user that it was a success.
                    String title = "Add City Request:";
                    String message = "Your Request Has Been Received,\n" +
                                     "The City Will Be Added Once It\n" +
                                     "Has Been Verified.\n\n" +
                                     "Thank You!";
                    //Sends the message to the submission result method
                    submissionResult(title, message);
                } else {
                    //If the email was unsuccessful a message will prompt the user that there was an error and to retry.
                    String errorTitle = "Add City Request:";
                    String errorMessage = "Your Request To Add A City Didn't\n" +
                                          "Go Through Properly, Please Try\n" +
                                          "Again.";
                    //Sends the message to the submission result method
                    submissionResult(errorTitle, errorMessage);
                }
            }
        });
    }

    //Private method to display an alert dialog for the user.
    private void submissionResult(String title, String message) {

        //Builds the alert dialog and sets the variables with the sent over title and message.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddCityActivity.this.finish();
            }
        });
        dialogBuilder.show();
    }
}
