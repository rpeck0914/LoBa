package lobaspecials.loba.Utilities;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class ErrorMessage {
    //Class to create an error message.

    //Creates an error message with no title.
    public ErrorMessage(Context context, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    //Creates an error message with a title.
    public ErrorMessage(Context context, String message, String title) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
