package lobaspecials.loba.Utilities;

import android.os.AsyncTask;
import lobaspecials.loba.Interfaces.EmailCallback;

/**
 * Created by Robert Peck on 1/31/2016.
 */
public class EmailUser {
    //Email User class is to send a email out to the user for password recover.

    //Empty constructor.
    public EmailUser(){ }

    //Public method to execute an async task.
    public void sendEmailToUserAsyncTask(String userEmail,String subject, String body, EmailCallback emailCallback) {
        new SendMailToUser(userEmail, subject, body, emailCallback).execute();
    }

    //Public class to email the user, class extends async task.
    public class SendMailToUser extends AsyncTask<Void, Void, Boolean> {
        private String mUserEmail, mSubject, mBody;
        private EmailCallback mEmailCallback;

        //Constructor for the send mail to user class.
        public SendMailToUser(String userEmail, String subject, String body, EmailCallback emailCallback) {
            //Sets all the sent over variables to the class variables.
            mUserEmail = userEmail;
            mSubject = subject;
            mBody = body;
            mEmailCallback = emailCallback;
        }

        //Override method to execute the background tasks.
        @Override
        protected Boolean doInBackground(Void... params) {
            //Try catch to catch any errors that may occur in sending the email.
            try {
                //Sets all the details of the email.
                GMailSender sender = new GMailSender("lobaspecials@gmail.com", "LoBa721Dev");
                sender.sendMail(mSubject,
                        mBody,
                        "lobaspecials@gmail.com",
                        mUserEmail);

            //Catches all exceptions that may occur.
            } catch (Exception e) {
                //Prints all errors to the stack trace.
                e.printStackTrace();
                //Returns false to show there was an error.
                return false;
            }
            //If email is sent successfully true is returned.
            return true;
        }

        //Override method for when background tasks are completed.
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //Calls the interface and sends over the returned boolean.
            mEmailCallback.emailSent(aBoolean);
        }
    }
 }
