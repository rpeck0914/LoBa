package lobaspecials.loba.Objects;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class Bar {
    //Bar class creates and stores a bars data.

    //Variables to hold the bars data.
    private int mBarId, mCityId;
    private String mBarName, mBarAddress, mBarZipCode, mBarPhone, mBarState, mBarCity;

    //Constructor to make a bar just with it's cityId.
    public Bar(int cityID) {
        mCityId = cityID;
    }

    //Constructor to create a bar with just the barId and barName.
    public Bar(int barId, String barName) {
        mBarId = barId;
        mBarName = barName;
    }

    //Constructor that makes a bar with it's id, state, and city.
    public Bar(int barId, String barState, String barCity) {
        mBarId = barId;
        mBarState = barState;
        mBarCity = barCity;
    }

    //Constructor to create a bar with all it's details.
    public Bar(int barId, String barName, String barAddress, String barState, String barCity, String barZipCode, String barPhone) {
        mBarId = barId;
        mBarName = barName;
        mBarAddress = barAddress;
        mBarState = barState;
        mBarCity = barCity;
        mBarZipCode = barZipCode;
        mBarPhone = barPhone;
    }

    //Getter methods to retrieve the data from a bar.
    public int getBarId() {
        return mBarId;
    }

    public String getBarName() {
        return mBarName;
    }

    public String getBarAddress() {
        return mBarAddress;
    }

    public String getBarZipCode() {
        return mBarZipCode;
    }

    public String getBarPhone() {
        return mBarPhone;
    }

    public String getBarState() {
        return mBarState;
    }

    public String getBarCity() {
        return mBarCity;
    }

    public int getCityId() {
        return mCityId;
    }
}
