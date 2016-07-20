package lobaspecials.loba.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import lobaspecials.loba.Objects.User;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class UserLocalStore {
    //UserLocalStore class stores the user's details locally on the device.

    //Key string for getting into shared preferences.
    public static final String SP_NAME = "userDetails";

    //Creates a variable for the class SharedPreferences.
    SharedPreferences userLocalDatabase;

    //Constructor that takes the context. Then uses the context to get The SharedPreferences.
    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    //Public method to store the users data into SharedPreferences.
    public void storeUserData(User user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("username", user.getUserName());
        userLocalDatabaseEditor.putString("email", user.getEmail());
        userLocalDatabaseEditor.putString("password", user.getPassword());
        userLocalDatabaseEditor.putString("city", user.getCity());
        userLocalDatabaseEditor.putString("state", user.getState());
        userLocalDatabaseEditor.putInt("cityId", user.getCityId());
        userLocalDatabaseEditor.commit();
    }

    //Public method to set the user as being logged in.
    public void setUserLoggedIn(boolean loggedIn){
        //Sets the user's log in status true.
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    //Public method to clear out the user's data from the phone when the user logs out.
    public void clearUserData() {
        SharedPreferences.Editor userLocalDataBaseEditor = userLocalDatabase.edit();
        userLocalDataBaseEditor.clear();
        userLocalDataBaseEditor.commit();
    }

    //Public method that retrieves the logged in user's details and returns them.
    public User getLoggedInUser() {
        //Checks to see if user is logged in. If not null is returned.
        if(userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        //Pulls all the user's data out of the database and stores each part in local variables.
        String username = userLocalDatabase.getString("username", "");
        String email = userLocalDatabase.getString("email", "");
        String password = userLocalDatabase.getString("password", "");
        String city = userLocalDatabase.getString("city", "");
        String state = userLocalDatabase.getString("state", "");
        int cityId = userLocalDatabase.getInt("cityId", 1);

        //New user is created with the data that was stored in the phone.
        User user = new User(username, email, password, city, state, cityId);
        //Returns the newly created user.
        return user;
    }

    //Public method that stores the selected state and city locally.
    public void setSelectedCityAndState(String name, String type) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        switch (type) {
            case "State":
                userLocalDatabaseEditor.putString("selectedState", name);
                break;
            case "City":
                userLocalDatabaseEditor.putString("selectedCity", name);
                break;
        }
        userLocalDatabaseEditor.commit();
    }

    //Public method that retrieves the selected city and state from the local database on the phone.
    public String getSelectedCityAndState(String type) {
        String name = null;
        switch (type) {
            case "selectedState":
                name = userLocalDatabase.getString(type, "");
                break;
            case "selectedCity":
                name = userLocalDatabase.getString(type, "");
                break;
        }
        return name;
    }
}
