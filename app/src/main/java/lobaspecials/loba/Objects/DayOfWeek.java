package lobaspecials.loba.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class DayOfWeek {
    //DayOfWeek class creates and stores the specials for a certain day of the week.

    //Variables to store the data for a DayOfWeekObject.
    private String mDayOfWeekString, mDate;
    private int mBarId;
    private List<Special> mSpecials = new ArrayList<>();

    //Constructor to create a DayOfWeek with just the barId.
    public DayOfWeek(int barId) {
        mBarId = barId;
    }

    //Constructor to create a DayOfWeek with the name of the day, and date.
    public DayOfWeek(String day, String date) {
        mDayOfWeekString = day;
        mDate = date;
    }

    //Constructor to create a DayOfWeek with the name of the day, date, and barId.
    public DayOfWeek(String day, String date, String barId) {
        mDayOfWeekString = day;
        mDate = date;
        mBarId = Integer.parseInt(barId);
    }

    //Public method to add a special to the day of the week.
    public void addSpecial(Special special) {
        mSpecials.add(special);
    }

    //Getter methods to retrieve the data from a DayOfWeek Object.
    public String getDayOfWeekString() {
        return mDayOfWeekString;
    }

    public String getDate() {
        return mDate;
    }

    public int getBarId() {
        return mBarId;
    }

    public List<Special> getSpecials() {
        return mSpecials;
    }
}
