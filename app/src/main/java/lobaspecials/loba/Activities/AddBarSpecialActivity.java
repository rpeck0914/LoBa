package lobaspecials.loba.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import lobaspecials.loba.Interfaces.GetSpecialCallback;
import lobaspecials.loba.Objects.DayOfWeek;
import lobaspecials.loba.Objects.Special;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.R;
import lobaspecials.loba.Utilities.ServerRequests;
import lobaspecials.loba.Utilities.UserLocalStore;

public class AddBarSpecialActivity extends Activity implements View.OnClickListener {
//Add bar special activity is an activity to allow the user to add a special to the selected bar.

    //Class level variables.
    private String mDayOfWeekString, mDescriptionString, mBarid;
    private int mYear, mMonth, mDay, mMaxMonthDays;

    //Layout variables.
    private TextView mSubmitButton, mBackButton;
    private EditText mDescriptionEntered;
    private Spinner mDateEnteredSpinner;
    private ImageView mAddBarSpecial;

    //List variable to hold a list of dates.
    private List<String> mDates;
    //Variable for the class userLocalStore to pull the logged in user's data.
    private UserLocalStore mUserLocalStore;

    //key for creating an intent to start this activity.
    private static final String EXTRA_BAR_ID = "rpeck.loba_login_register.add_special.bar_id";

    //Static method for starting and creating an intent to start up this activity.
    public static Intent newIntent(Context context, String barId) {
        Intent intent = new Intent(context, AddBarSpecialActivity.class);
        intent.putExtra(EXTRA_BAR_ID, barId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bar_special);
        //Get the selected bar id to pull the bar's data into the activity.
        mBarid = getIntent().getStringExtra(EXTRA_BAR_ID);

        //Creates a new date from the current date.
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMaxMonthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //Starts the loadDaySpinner method to load the spinner with dates.
        loadDaySpinner();

        //Sets the spinner to the variable and loads the dates into the spinner
        mDateEnteredSpinner = (Spinner) findViewById(R.id.add_special_date_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mDates);
        mDateEnteredSpinner.setAdapter(adapter);
        mDateEnteredSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                //Override method that sets the selected spinner object to the string variable.
                mDayOfWeekString = mDateEnteredSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });

        //Sets the edit text to the variable and puts a text change listener on the edit text.
        mDescriptionEntered = (EditText) findViewById(R.id.add_special_description_edit_text);
        mDescriptionEntered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Override method for before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Override method for when the text is changed in the edit text.
                mDescriptionString = mDescriptionEntered.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Override method for after the text changes in the edit text.
            }
        });

        //Sets the layout buttons to the class variables.
        mAddBarSpecial = (ImageView) findViewById(R.id.loba_add_bar_special);
        mBackButton = (TextView) findViewById(R.id.add_special_back_button);
        mSubmitButton = (TextView) findViewById(R.id.add_special_submit_button);

        mAddBarSpecial.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);

        mUserLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        //Override for the click of objects on the layout.

        //Switch statement checks to see which id is touched in the layout.
        switch (v.getId()) {
            case R.id.add_special_back_button:
                //If back button is clicked the activity is called to be finished.
                this.finish();
                break;
            case R.id.add_special_submit_button:
                //If submit special button is clicked the submit bar special method is called.
                submitBarSpecial();
                break;

            case R.id.loba_add_bar_special:
                InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    //Private method to load the dates for the date spinner.
    private void loadDaySpinner() {
        String dateToAdd = (mMonth + 1) + "/" + mDay + "/" + mYear;

        //Creates a new array list to hold the dates
        mDates = new ArrayList<>();
        mDates.add(dateToAdd);

        //Loops through and creates 7 dates out from the current date, then adds them to the array list.
        for(int i = 0; i < 6; i++) {
            mDay += 1;
            if(mDay > mMaxMonthDays) {
                if(mMonth + 1 == 12) {
                    mYear += 1;
                    mMonth = 0;
                } else {
                    mMonth += 1;
                }
                mDay = 1;
                Calendar newMonth = new GregorianCalendar(mYear, mMonth, mDay);
                mMaxMonthDays = newMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            dateToAdd = (mMonth + 1) + "/" + mDay + "/" + mYear;
            mDates.add(dateToAdd);
        }
    }

    //Private method to send the added bar special over to the database.
    private void submitBarSpecial() {
        //Method call to get the selected date for the special and formats it for the database.
        getDayOfWeek();

        //Makes server request to add the special to the database.
        ServerRequests serverRequests = new ServerRequests(this);
        //Create special method is called to create the new special.
        serverRequests.storeSpecialDataAsyncTask(createSpecial(), new GetSpecialCallback() {
            @Override
            public void done(DayOfWeek returnedDayOfWeek) {
                //Calls a method to finish the activity.
                finishAddSpecialActivity();
            }
        });

    }

    //Private method to set the selected date to the right format for the database.
    private void getDayOfWeek() {
        String parts[] = (mDateEnteredSpinner.getSelectedItem().toString()).split("/");
        mYear = Integer.parseInt(parts[2]);
        mMonth = Integer.parseInt(parts[0]) - 1;
        mDay = Integer.parseInt(parts[1]);

        //Once the date is created the it is stored in the day of week string variable.
        Calendar newDate = new GregorianCalendar(mYear, mMonth, mDay);
        mDayOfWeekString = newDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
    }

    //Private method to create the special with the return type day of week.
    private DayOfWeek createSpecial() {
        //Gets the logged in user to store the special as that user's upload.
        User user = mUserLocalStore.getLoggedInUser();

        //Creates a new list of type special to store the special.
        List<Special> specialToAddArrayList = new ArrayList<>();

        //Creates a new special add adds the input data.
        Special specialToAdd = new Special(mDescriptionString, user.getUserName());

        //Adds the new special into the list of type special.
        specialToAddArrayList.add(specialToAdd);

        //Gathers the date selected from the user and adds it to the special.
        String parts[] = (mDateEnteredSpinner.getSelectedItem().toString()).split("/");
        mMonth = Integer.parseInt(parts[0]);
        mDay = Integer.parseInt(parts[1]);
        String date = mMonth + "/" + mDay;

        //Creates a new day of week to add the special to the selected date.
        DayOfWeek dayOfWeekToAdd = new DayOfWeek(mDayOfWeekString, date, mBarid);
        dayOfWeekToAdd.addSpecial(specialToAdd);

        //returns the new day of week.
        return dayOfWeekToAdd;
    }

    //Private method to finish the activity.
    private void finishAddSpecialActivity(){
        this.finish();
    }
}
