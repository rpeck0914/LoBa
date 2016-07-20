package lobaspecials.loba.Objects;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class City {
    //City class creates and stores a cities data of it's name and id in the database.

    //Variables to hold the city names and their ids.
    private int mStateId, mCityId;
    private String mCityName;

    //Constructor to create a city with it's stateId, name, and cityId.
    public City(int stateId, String cityName, int cityId) {
        mStateId = stateId;
        mCityName = cityName;
        mCityId = cityId;
    }

    //Constructor to create a city with just the stateId. Used to retrieve data from the database.
    public City(int stateId) {
        mStateId = stateId;
    }

    //Getter methods to retrieve the data from a City.
    public int getStateId() {
        return mStateId;
    }

    public String getCityName() {
        return mCityName;
    }

    public int getCityId() {
        return mCityId;
    }
}
