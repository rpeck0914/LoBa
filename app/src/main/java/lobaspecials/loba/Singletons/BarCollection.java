package lobaspecials.loba.Singletons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lobaspecials.loba.Objects.Bar;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class BarCollection {
    //BarCollection is a singleton to store the data of all the bars from a selected city.

    //Variable to create an instance of the singleton BarCollection.
    private static BarCollection sBarCollection;
    //List to hold the list of bars.
    private List<Bar> mBars;

    //Public method to see if a instance of the singleton exists.
    public static BarCollection get() {
        if(sBarCollection == null) {
            //If not then a new instance is created.
            sBarCollection = new BarCollection();
        }
        //Returns the instance of the singleton.
        return sBarCollection;
    }

    //Private Constructor to create a new instance of the singleton.
    private BarCollection() {
        mBars = new ArrayList<>();
    }

    //Public method to add a bar to the list.
    public void addBar(Bar bar) {
        //Adds the bar to the list.
        mBars.add(bar);
        //Calls a sort method to sort the list.
        sortArrayListByBarName();
    }

    //Private method to sort the list of bars based on their name.
    private void sortArrayListByBarName(){
        Collections.sort(mBars, new Comparator<Bar>() {
            @Override
            public int compare(Bar lhs, Bar rhs) {
                return lhs.getBarName().compareTo(rhs.getBarName());
            }
        });
    }

    //Public method to retrieve a specific bar in the list based on the barId.
    public Bar getBar(int barId) {
        for (Bar bar : mBars) {
            if(bar.getBarId() == barId) {
                //If bar exists the bar is then returned.
                return bar;
            }
        }
        //If bar does not exist then null is returned.
        return null;
    }

    //Public method to clear out the list of bars.
    public void removeList() {
        mBars.clear();
    }

    //Getter method to return the list of bars.
    public List<Bar> getBars() {
        return mBars;
    }
}
