package lobaspecials.loba.Singletons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lobaspecials.loba.Objects.DayOfWeek;
import lobaspecials.loba.Objects.Special;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class SpecialCollection {
    //SpecialCollection is a singleton to store the data of all the specials from a selected bar.

    //Variable to create an instance of the singleton SpecialCollection.
    private static SpecialCollection sSpecialCollection;
    //List to hold the list of DayOfWeek.
    private List<DayOfWeek> mDaysOfWeek;

    //Public method to see if a instance of the singleton exists.
    public static SpecialCollection get(){
        if(sSpecialCollection == null) {
            //If not then a new instance is created.
            sSpecialCollection = new SpecialCollection();
        }
        //Returns the instance of the singleton.
        return sSpecialCollection;
    }

    //Private Constructor to create a new instance of the singleton.
    private SpecialCollection() {
        mDaysOfWeek = new ArrayList<>();
    }

    //Public method to add a DayOfWeek to the list.
    public void addDayToWeek(DayOfWeek dayOfWeek) {
        //Adds the DayOfWeek to the list.
        mDaysOfWeek.add(dayOfWeek);
        //Calls a sort method to sort the list based on the date.
        sortArrayListByDate();
    }

    //Public method to add the dayOfWeek if the date already exists in the list.
    public void addSpecialToExistingDay(DayOfWeek sentDayOfWeek, Special special) {
        //For each loop that loops through all the dates to find the matching date.
        for(DayOfWeek dayOfWeek : mDaysOfWeek) {
            //Checks to find the date that matches the one sent over.
            if(dayOfWeek.getDate().equals(sentDayOfWeek.getDate())) {
                //Adds the special to the matching date found.
                dayOfWeek.addSpecial(special);
            }
        }
        //Sort method is called to sort the list based on the date.
        sortArrayListByDate();
    }

    //Private method to sort the arrayList by the date.
    private void sortArrayListByDate() {
        Collections.sort(mDaysOfWeek, new Comparator<DayOfWeek>() {
            @Override
            public int compare(DayOfWeek lhs, DayOfWeek rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
    }

    //Public method to retrieve a specific day of the week in the list based on the date.
    public DayOfWeek getSpecialDay(String date) {
        for(DayOfWeek dayOfWeek : mDaysOfWeek) {
            //If date exists the DayOfWeek is then returned.
            if(dayOfWeek.getDate().equals(date)) {
                return dayOfWeek;
            }
        }
        //If date does not exist then null is returned.
        return null;
    }

    //Public method to clear out the list.
    public void removeList() {
        mDaysOfWeek.clear();
    }

    //Getter method to return the list of DayOfWeek.
    public List<DayOfWeek> getDaysOfWeek() {
        return mDaysOfWeek;
    }
}
