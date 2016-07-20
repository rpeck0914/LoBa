package lobaspecials.loba.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import lobaspecials.loba.Interfaces.GetUserCallback;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.R;
import lobaspecials.loba.Utilities.Encryption;
import lobaspecials.loba.Utilities.ErrorMessage;
import lobaspecials.loba.Utilities.ServerRequests;
import lobaspecials.loba.Utilities.UserLocalStore;

public class Login extends Activity implements View.OnClickListener {

    //Private Variables For The Login Activity Layout
    private Button mButtonLogin, mRegisterLink;
    private EditText mEnterUserName, mEnterPassword;
    private TextView mForgotLoginLink, mUsernameText, mPasswordText;
    private ImageView mLoginImage;

    //Creates A Variable For UserLocalStore To Sore The Users Data Locally On Device
    UserLocalStore userLocalStore;
    //Creates a variable to run the encryption class
    Encryption mEncryption;
    ErrorMessage mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameText = (TextView) findViewById(R.id.username_title_login_class);

        //Sets The Layout Objects To Their Variables
        mEnterUserName = (EditText) findViewById(R.id.enter_username);
        mPasswordText = (TextView) findViewById(R.id.password_title_login_class);
        mEnterPassword = (EditText) findViewById(R.id.enter_Password);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mForgotLoginLink = (TextView) findViewById(R.id.forgot_login);
        mRegisterLink = (Button) findViewById(R.id.register_link);

        mLoginImage = (ImageView)findViewById(R.id.loba_login);

        //On Click Listeners For Clicks On Buttons
        mLoginImage.setOnClickListener(this);
        mForgotLoginLink.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        mRegisterLink.setOnClickListener(this);

        //Instantiates UserLocalStore Variable To Save Logged In Users Data
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        //Switch Case For Reading The On Click Listeners
        switch (v.getId()) {
            case R.id.forgot_login:
                startActivity(new Intent(this, ForgotLoginActivity.class));
                break;

            case  R.id.button_login:
                //Captures User Input When Clicking LogIn Button
                String username = mEnterUserName.getText().toString();
                String password = mEnterPassword.getText().toString();
                mEncryption = new Encryption(password);
                password = mEncryption.getResult();

                //Creates A User Based On The User's Input
                User user = new User(username, password);

                //Sends User's Inputted Info Over To Be Verified
                authenticate(user);
                break;

            case R.id.register_link:
                //Starts Register Activity If User Clicks Register Button
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.loba_login:
                InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    private void authenticate(User user) {
        //Creates A New Instance Of ServerRequests To Send Over The User's Inputted Info
        ServerRequests serverRequests = new ServerRequests(this);
        //Calls The FetchUserDataAsyncTask Method That Will Start The User Validation On The Database End
        serverRequests.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                //When The FetchUserDataAsyncTask Method Is Completed This Override Method Is Called
                if (returnedUser == null) {
                    //A Null User Returned Will Lead To An Error Message Popping Up By Calling The Method showErrorMessage
                    String errorMessage = "Incorrect User Detail";
                    mErrorMessage = new ErrorMessage(Login.this, errorMessage);
                } else {
                    //If User Exists The logUserIn Method Is Called By Sending Over The User Info
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void logUserIn(User userToLogIn) {
        //logUserIn Method Takes The User And Stores It Locally In The Devices Data,
        //Then Starts The MainActivity
        try {
            userLocalStore.storeUserData(userToLogIn);
            userLocalStore.setUserLoggedIn(true);
            startActivity(new Intent(this, LobaMainActivity.class));
        } catch (Exception e) {
            Log.i("BOOM", e.toString());
        }
    }

    //Override To Display A Dialog Asking If The User Wants To Exit The App
    @Override
    public void onBackPressed() {
        //Creates A New AlertDialog.Builder Variable And Sets It's Properties
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Exit App");
        dialogBuilder.setMessage("Are You Sure You Want To Exit The App?");
        //If The Exit Button Is Clicked An OnClickListener Is Called To Move The App To The Back (Takes It Off The Screen)
        dialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                finish();
            }
        });
        //If The No Button Is Clicked The Dialog Will Just Go Away With Nothing Else Happening
        dialogBuilder.setNegativeButton("No", null);
        dialogBuilder.show();
    }
}
