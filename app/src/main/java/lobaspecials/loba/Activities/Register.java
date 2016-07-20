package lobaspecials.loba.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import lobaspecials.loba.Fragments.DatePickerFragment;
import lobaspecials.loba.Interfaces.CityTrigger;
import lobaspecials.loba.Interfaces.GetCityCallback;
import lobaspecials.loba.Interfaces.GetStateCallback;
import lobaspecials.loba.Interfaces.GetUserCallback;
import lobaspecials.loba.Interfaces.OnDatePickerCompleteListener;
import lobaspecials.loba.Interfaces.StateTrigger;
import lobaspecials.loba.Objects.City;
import lobaspecials.loba.Objects.State;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.R;
import lobaspecials.loba.Singletons.CityCollection;
import lobaspecials.loba.Singletons.StateCollection;
import lobaspecials.loba.Utilities.Encryption;
import lobaspecials.loba.Utilities.ErrorMessage;
import lobaspecials.loba.Utilities.ServerRequests;
import lobaspecials.loba.Utilities.ServerRequestsConnector;

public class Register extends FragmentActivity implements View.OnClickListener, OnDatePickerCompleteListener {

    //Private Variables For The Register Activity Layout
    private Button mButtonRegister;
    private EditText mEnterUserName, mEnterEmail, mEnterPassword, mEnterConfirmPassword;
    private Spinner mSelectState, mSelectCity;
    private TextView mAddCityButton, mUsernameText, mEmailText, mCreatePassText, mConfirmPassText, mStateText, mCityText;
    private ImageView mRegisterImage;

    private int mSelectedCityID;

    private Encryption mEncryption;
    private StateCollection mStateCollection;
    private CityCollection mCityCollection;
    private State mState;
    private ServerRequestsConnector mServerRequestsConnector;

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadBirthDateVerifier();

        //Sets The Layout Objects To Their Variables
        mUsernameText = (TextView) findViewById(R.id.username_title_register_class);
        mEnterUserName = (EditText) findViewById(R.id.enter_userName);
        mEmailText = (TextView) findViewById(R.id.email_title_register_class);
        mEnterEmail = (EditText) findViewById(R.id.enter_email);
        mCreatePassText = (TextView) findViewById(R.id.create_password_title_register_class);
        mEnterPassword = (EditText) findViewById(R.id.enter_Password);
        mConfirmPassText = (TextView) findViewById(R.id.confirm_password_title_register_class);
        mEnterConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mStateText = (TextView) findViewById(R.id.state_title_register_class);
        mSelectState = (Spinner) findViewById(R.id.enter_state);
        mCityText = (TextView) findViewById(R.id.city_title_register_class);
        mSelectCity = (Spinner) findViewById(R.id.enter_city);
        mButtonRegister = (Button) findViewById(R.id.button_register);
        mAddCityButton = (TextView) findViewById(R.id.add_city_button);
        mRegisterImage = (ImageView)findViewById(R.id.loba_register);

        //On Click Listeners For Clicks On Buttons
        mRegisterImage.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
        mAddCityButton.setOnClickListener(this);

        //Instantiates A Variable For The States Class
        mState = new State();

        mServerRequestsConnector = new ServerRequestsConnector();

        //Calls pullStates Method While Passing The states Class Variable
        pullStates(mState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Switch Case For Reading The On Click Listeners
            case R.id.button_register:

                //Grabs All The User Input And Creates A New User Based On The Input Information
                String username = mEnterUserName.getText().toString();
                String email = mEnterEmail.getText().toString();
                String password = mEnterPassword.getText().toString();
                mEncryption = new Encryption(password);
                password = mEncryption.getResult();
                //password = md5(password);
                String confirmPassword = mEnterConfirmPassword.getText().toString();
                mEncryption = new Encryption(confirmPassword);
                confirmPassword = mEncryption.getResult();
                //confirmPassword = md5(confirmPassword);
                String state = mSelectState.getSelectedItem().toString();
                String city = mSelectCity.getSelectedItem().toString();

                if (validateRegisteringUser(username, email, password, confirmPassword)) {
                    mEncryption = new Encryption(email);
                    email = mEncryption.getResult();
                    //Creates A New User With Valid Input
                    User user = new User(username, email, password, city, state, mSelectedCityID);

                    //Sends The Newly Created User Over To The registerUser Method
                    registerUser(user);
                }
                break;

            case R.id.add_city_button:
                startActivity(new Intent(this, AddCityActivity.class));
                break;

            case R.id.loba_register:
                InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    private void loadBirthDateVerifier(){
        Date date = new Date();

        FragmentManager manager = getSupportFragmentManager();
        Fragment datePickerHolder = manager.findFragmentById(R.id.date_picker_container);

        DatePickerFragment dialog = DatePickerFragment.newInstance(date);
        dialog.setTargetFragment(datePickerHolder, REQUEST_DATE);
        dialog.show(manager, DIALOG_DATE);
    }

    @Override
    public void datePickerCompleted(int requestCode, int resultCode, Date date) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_DATE) {
            if(validBirthDate(date) == false) {
                //Creates A New AlertDialog.Builder Variable And Sets It's Properties
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setCancelable(false);
                dialogBuilder.setTitle("Under Age");
                dialogBuilder.setMessage("Sorry, But You Must Be 21 To Use This App");
                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();
                    }
                });
                dialogBuilder.show();
            }
        }
    }

    private boolean validBirthDate(Date date) {
        Date todayDate = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(todayDate);

        int todayYear = todayCalendar.get(Calendar.YEAR);
        int todayMonth = todayCalendar.get(Calendar.MONTH);
        int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

        Date oldEnoughDate = new GregorianCalendar(todayYear - 21, todayMonth, todayDay).getTime();

        if(date.before(oldEnoughDate)) {
            return true;
        } else {
            return false;
        }
    }

    //loadStateSpinner Method To Load The State Spinner With The States Passed Over From The Database
    private void loadStateSpinner() {

        mStateCollection = StateCollection.get();
        List<String> mStateNames = new ArrayList<>();
        for(int i = 0; i < mStateCollection.getStates().size(); i++) {
            mStateNames.add(mStateCollection.getStates().get(i).getStateName());
        }

        //Loads The State Spinner With The Array Of States
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStateNames);
        mSelectState.setAdapter(adapter);
        mSelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                //Override Method For Selected A Different State In The State Spinner

                //Finds The Selected State's ID To Fetch The Cities Within That State
                String selectedState = mSelectState.getSelectedItem().toString();
                for (int i = 0; i < mStateCollection.getStates().size(); i++) {
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

    //loadCitySpinner Method To Load The Cities Spinner With The Cities Passed Over From The Database Based On The Selected State
    private void loadCitySpinner() {

        mCityCollection = CityCollection.get();
        List<String> mCityNames = new ArrayList<>();
        for(int i = 0; i < mCityCollection.getCities().size(); i++) {
            mCityNames.add(mCityCollection.getCities().get(i).getCityName());
        }

        //Loads The City Spinner With The Array Of Cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCityNames);
        mSelectCity.setAdapter(adapter);
        mSelectCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                String selectedCity = mSelectCity.getSelectedItem().toString();
                for (int i = 0; i < mCityCollection.getCities().size(); i++) {
                    if (mCityCollection.getCities().get(i).getCityName().equals(selectedCity)) {
                        mSelectedCityID = mCityCollection.getCities().get(i).getCityId();
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

    //registerUser Method Takes The Passed Over User And Sends It Over To Server Requests To Be Added To The Database
    private void registerUser(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                //Override Method That's Called After The User Is Added To The Database
                startActivity(new Intent(Register.this, Login.class));
            }
        });

    }

    //pullStates Method Sends A Request To ServerRequests To Pull The State Information From The Database
    private void pullStates(State state){
        //Calls connector class to make a server request to pull the states.
        mServerRequestsConnector.pullStates(this, state, new StateTrigger() {
            //Override that triggers once the state data has been pulled from the database.
            @Override
            public void stateTrigger(State returnedState) {
                //Calls the load state spinner method.
                loadStateSpinner();
            }
        });
    }

    //pullCities Method Sends A Request To ServerRequests With The StateID To Pull The Cites Information From The Database
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

    //Private method to validate the user's input when registering.
    private boolean validateRegisteringUser(String username, String email, String password, String confirmPassword) {
        //Sets the boolean to false.
        boolean userValid = false;

        //Checks each input from the user, if there is nothing entered a toast is made to let the user know where the error is.
        if (!username.isEmpty() && username != null) {
            if (isValidEmail(email)) {
                if (!password.isEmpty() && password != null) {
                    if (!confirmPassword.isEmpty() && confirmPassword != null) {
                        if (password.equals(confirmPassword)) {
                            //If all validation is good the boolean is set to true.
                            userValid = true;
                        } else {
                            makeAToast("Passwords Do Not Match");
                        }
                    } else {
                        makeAToast("Please ReEnter Your Password");
                    }
                } else {
                    makeAToast("Please Enter A Password");
                }
            } else {
                makeAToast("Please Enter A Email!");
            }
        } else {
            makeAToast("Please Enter A Username!");
        }
        //Returns the boolean.
        return userValid;
    }

    //Private method to makes sure the input email is an actual email.
    private boolean isValidEmail(CharSequence target) {
        if(target == null) {
            //If the edit text is null false is returned.
            return false;
        } else {
            //Validates the email and returns either true or false.
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //Private method to make a toast while accepting a string.
    private void makeAToast(String message) {
        //Makes a toast with passed in string.
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
