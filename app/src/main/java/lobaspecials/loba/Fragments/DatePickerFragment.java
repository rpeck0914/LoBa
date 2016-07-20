package lobaspecials.loba.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import lobaspecials.loba.Interfaces.OnDatePickerCompleteListener;
import lobaspecials.loba.R;

public class DatePickerFragment extends DialogFragment {
    //Date picker fragment is to validate the registering user's date of birth.

    //Static key for starting the instance of this fragment.
    private static final String ARG_DATE = "date";

    //Variable to hold the date picker.
    private DatePicker mDatePicker;

    //Listener for when the date picker is completed.
    private OnDatePickerCompleteListener mOnDatePickerCompleteListener;

    //Public method to start a new instance of the date picker fragment.
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Gets the date that was used to start the fragment.
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        //Sets the listener for the date picker.
        mOnDatePickerCompleteListener = (OnDatePickerCompleteListener) getActivity();

        //Sets the calendar to the date that was used to start the fragment.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //Picks out the year, month, and day from the date.
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Creates the view and inflates it.
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        //Sets the layout object to the variable.
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);

        //Sets the date picker to the date extracted.
        mDatePicker.init(year, month, day, null);

        //Return that returns the selected date input by the user.
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();

                        Date date = new GregorianCalendar(year, month, day).getTime();

                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    //Private method that sends the user's input date back to the activity.
    private void sendResult(int resultCode, Date date) {
        mOnDatePickerCompleteListener.datePickerCompleted(getTargetRequestCode(), resultCode, date);
    }
}
