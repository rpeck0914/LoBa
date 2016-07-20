package lobaspecials.loba.Utilities;

import android.os.AsyncTask;
import lobaspecials.loba.Interfaces.EmailCallback;

/**
 * Created by Robert Peck on 12/4/2015.
 */
public class EmailRequest {
    //Email Request class is to send an email out to loba support.

    //Empty constructor.
    public EmailRequest() { }

    //Public method that executes an async task to send an email.
    public void sendEmailRequestAsyncTask(String subject, String body, EmailCallback emailCallback) {
        new SendMail(subject, body, emailCallback).execute();
    }

    //Public class to to send the mail, extends the async task.
    public class SendMail extends AsyncTask<Void, Void, Boolean> {
        //Class level variables.
        private String mSubject, mBody;
        private EmailCallback mEmailCallback;

        //Constructor that takes the email subject, body and email callback.
        public SendMail(String subject, String body, EmailCallback emailCallback) {
            //Sets the sent over variables to the class variables.
            mSubject = subject;
            mBody = body;
            mEmailCallback = emailCallback;
        }

        //Override method to execute code on a different thread.
        @Override
        protected Boolean doInBackground(Void... params) {
            //Try to catch all errors.
            try {
                //Sets the details of the email.
                GMailSender sender = new GMailSender("lobaspecials@gmail.com", "LoBa721Dev");
                sender.sendMail(mSubject,
                        mBody,
                        "lobaspecials@gmail.com",
                        "lobaspecials@gmail.com");
            //All exceptions will be caught in the catch.
            } catch (Exception e) {
                //Prints the exception to the stack trace.
                e.printStackTrace();
                //Returns false to show there was an error.
                return false;
            }
            //If email is sent successfully true is returned.
            return true;
        }

        //Override method for when the background task has completed.
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //Calls the interface and sends over the returned boolean.
            mEmailCallback.emailSent(aBoolean);
        }
    }
}
