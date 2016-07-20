package lobaspecials.loba.Objects;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class User {
    //User class creates and holds the data of a user.

    //Variables to hold the user information.
    private String mUserName, mEmail, mPassword, mCity, mState;
    private int mCityId;

    //Constructor to create a user with all the details being passed over. This is for when a user registers.
    public User(String userName, String email, String password, String city, String state, int cityId) {
        mUserName = userName;
        mEmail = email;
        mPassword = password;
        mCity = city;
        mState = state;
        mCityId = cityId;
    }

    //Constructor to create a user with just the userName and password. This is used to create a user for logging in.
    public User(String userName, String password) {
        mUserName = userName;
        mPassword = password;
    }

    public User(String email) {
        mEmail = email;
    }

    public User(String userName, String password, String city, String state, int cityId) {
        mUserName = userName;
        mPassword = password;
        mCity = city;
        mState = state;
        mCityId = cityId;
    }

    //Getter methods to retrieve the data from a user.
    public String getUserName() {
        return mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public int getCityId() {
        return mCityId;
    }
}
