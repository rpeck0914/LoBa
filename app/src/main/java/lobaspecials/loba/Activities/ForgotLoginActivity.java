package lobaspecials.loba.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import lobaspecials.loba.Interfaces.EmailCallback;
import lobaspecials.loba.Interfaces.GetUserCallback;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.R;
import lobaspecials.loba.Utilities.EmailUser;
import lobaspecials.loba.Utilities.Encryption;
import lobaspecials.loba.Utilities.ErrorMessage;
import lobaspecials.loba.Utilities.ServerRequests;

public class ForgotLoginActivity extends Activity implements View.OnClickListener {
    //Forgot login activity is so the user can make a request to change their password if they have forgotten it.

    //Layout variables.
    private EditText mEnteredEmail;
    private Button mRecoverLogin;
    private ImageView mForgotLoginImage;

    //Class level variables.
    private String mEnteredEmailString, mEncryptedEmail;

    //Class variables.
    private Encryption mEncryption;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_login);

        //Sets the layout variables to the layout objects.
        mEnteredEmail = (EditText) findViewById(R.id.recover_login_entered_email);
        mRecoverLogin = (Button) findViewById(R.id.recover_login_button);
        mForgotLoginImage = (ImageView) findViewById(R.id.loba_forgot_login);
        mRecoverLogin.setOnClickListener(this);
        mForgotLoginImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Override for the click of objects on the layout.

        //Switch statement checks to see which id is touched in the layout.
        switch (v.getId()) {
            case R.id.recover_login_button:
                //Validates the user's email that was input.
                if(recoverLoginInfo()) {
                    //Calls the pull user login method.
                    pullUserLogin();
                }
                break;

            case R.id.loba_forgot_login:
                InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    //Private method to take the user's email and make sure it's a valid email then encrypts it.
    private boolean recoverLoginInfo() {
        //Validates the user's email address
        if(mEnteredEmail.getText() != null && Patterns.EMAIL_ADDRESS.matcher(mEnteredEmail.getText()).matches()) {
            mEnteredEmailString = mEnteredEmail.getText().toString();
            //Encrypts the user's email.
            mEncryption = new Encryption(mEnteredEmailString);
            mEncryptedEmail = mEncryption.getResult();
            //Creates a new user with the input email.
            mUser = new User(mEncryptedEmail);
            //Returns true.
            return true;
        } else {
            //If the input was not valid an error message will be displayed and false is returned.
            new ErrorMessage(this, "Please Enter A Valid Email Address Please");
            return false;
        }
    }

    //Private method to pull the user's username from the database.
    private void pullUserLogin() {
        //Makes a server request to fetch the user's username.
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchForgotLoginUserDataAsyncTask(mUser, new GetUserCallback() {
            //Override method that is called after the server request is completed.
            @Override
            public void done(User returnedUser) {
                //If returned user is null
                if(returnedUser == null) {
                    //Error message is displayed to user saying no user exists for that email.
                    String errorMessage = "No User Exists For The Entered Email";
                    new ErrorMessage(ForgotLoginActivity.this, errorMessage);
                } else {
                    //If a valid user is returned the returned user is sent over to the send email to user method.
                    sendEmailToUser(returnedUser);
                }
            }
        });
    }

    //Private method to send an email out to the user with details on resetting their password.
    private void sendEmailToUser(User user) {
        //Sets the subject for the email.
        String subject = "User Login Recovery";
        //Sets the body for the email.
        String body = "Thank you for using LoBa Password Recovery \n\n" +
                "Email: " + mEnteredEmailString + "\n" +
                "UserName: " + user.getUserName().toString() + "\n\n" +
                "Please use this link to reset your password: http://loba.hostoi.com/reset_password.html \n\n" +
                "Thanks For Using Our App! \n\n" +
                "For any questions or concerns please contact us at lobaspecials@gmail.com";

        //Make a email request to send the email out to the user.
        EmailUser emailUser = new EmailUser();
        emailUser.sendEmailToUserAsyncTask(mEnteredEmailString, subject, body, new EmailCallback() {
            //Override method that is called after the email request.
            @Override
            public void emailSent(boolean sent) {
                //If email is successfully sent a message is displayed to check your email.
                if (sent) {
                    //Sets the title and message text.
                    String title = "Recover Login";
                    String message = "Please Check Email To Reset Your Password";
                    //Calls the email recovery result method sending over the message details.
                    emailRecoveryResult(title, message);
                //If email is not sent successfully an error message is displayed to the user.
                } else {
                    //Sets the title and message text.
                    String title = "Recover Login";
                    String errorMessage = "No User Is Linked To The Provided Email";
                    //Calls the email recovery result method sending over the message details.
                    emailRecoveryResult(title, errorMessage);
                }
            }
        });
    }

    //Private method to display a dialog to the user.
    private void emailRecoveryResult(String title, String message) {
        //Builds the alert dialog and sets all it's variables then is displayed.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ForgotLoginActivity.this.finish();
            }
        });
        dialogBuilder.show();
    }
}
