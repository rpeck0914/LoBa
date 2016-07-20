package lobaspecials.loba.Objects;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class Special {
    //Special class creates and stores the data of a special.

    //Variables to hold the special data.
    private String mSpecialDescription, mAddedBy;

    //Constructor to create a special with its description and who added it.
    public Special(String specialDescription, String addedBy) {
        mSpecialDescription = specialDescription;
        mAddedBy = addedBy;
    }

    //Getter methods to retrieve the data from a special.
    public String getSpecialDescription() {
        return mSpecialDescription;
    }

    public String getAddedBy() {
        return mAddedBy;
    }
}
