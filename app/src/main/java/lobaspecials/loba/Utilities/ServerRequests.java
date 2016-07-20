package lobaspecials.loba.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lobaspecials.loba.Interfaces.GetBarCallback;
import lobaspecials.loba.Interfaces.GetCityCallback;
import lobaspecials.loba.Interfaces.GetSpecialCallback;
import lobaspecials.loba.Interfaces.GetStateCallback;
import lobaspecials.loba.Interfaces.GetUserCallback;
import lobaspecials.loba.Objects.Bar;
import lobaspecials.loba.Objects.City;
import lobaspecials.loba.Objects.DayOfWeek;
import lobaspecials.loba.Objects.Special;
import lobaspecials.loba.Objects.State;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.Singletons.BarCollection;
import lobaspecials.loba.Singletons.CityCollection;
import lobaspecials.loba.Singletons.SpecialCollection;
import lobaspecials.loba.Singletons.StateCollection;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class ServerRequests {
    //ServerRequests class makes calls to the database and relays data to the app.

    //ProgressDialog variable for displaying a progress dialog box.
    ProgressDialog mProgressDialog;

    //Constant for setting the timeout of the connection to the database.
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    //Constant for holding the server address that hosts the database.
    public static final String SERVER_ADDRESS = "http://www.lobaspecials.com/";

    //Constructor that sets the parameters of the progress dialog for processing the server request.
    public ServerRequests(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Processing...");
        mProgressDialog.setMessage("Please Wait...");
    }

    //Public method to take in the user's data and executes a background task to store the data in the database.
    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        mProgressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    //Public method to take the user's userName and password and find their data on the database.
    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
        mProgressDialog.show();
        new FetchUserDataAsyncTask(user, userCallback).execute();
    }

    //Public method to take the user's email and gather their username from the database.
    public void fetchForgotLoginUserDataAsyncTask(User user, GetUserCallback userCallback) {
        mProgressDialog.show();
        new FetchForgotLoginUserDataAsyncTask(user, userCallback).execute();
    }

    //Public method to update the user's location on the database end.
    public void updateUserLocationDataAsyncTask(User user, GetUserCallback userCallback) {
        mProgressDialog.show();
        new UpdateUserLocationDataAsyncTask(user, userCallback).execute();
    }

    //Public method to retrieve the states from the database.
    public void fetchStateDataAsyncTask(State state, GetStateCallback stateCallback) {
        mProgressDialog.show();
        new FetchStateDataAsyncTask(state, stateCallback).execute();
    }

    //Public method to retrieve the cities of a selected state.
    public void fetchCityDataAsyncTask(City city, GetCityCallback cityCallback) {
        mProgressDialog.show();
        new FetchCityDataAsyncTask(city, cityCallback).execute();
    }

    //Public method to retrieve the bars in a selected city.
    public void fetchBarNamesDataAsyncTask(Bar bar, GetBarCallback barCallback) {
        mProgressDialog.show();
        new FetchBarNamesDataAsyncTask(bar, barCallback).execute();
    }

    //Public method to retrieve the selected bar details.
    public void fetchBarDetailsDataAsyncTask(Bar bar, GetBarCallback barCallback) {
        mProgressDialog.show();
        new FetchBarDetailsDataAsyncTask(bar, barCallback).execute();
    }

    //Public method to retrieve the selected bar's specials.
    public void fetchBarSpecialsDataAsyncTask(DayOfWeek dayOfWeek, GetSpecialCallback specialCallback) {
        mProgressDialog.show();
        new FetchBarSpecialsDataAsyncTask(dayOfWeek, specialCallback).execute();
    }

    //Public method to store a special in the selected bar.
    public void storeSpecialDataAsyncTask(DayOfWeek dayOfWeek, GetSpecialCallback specialCallback) {
        mProgressDialog.show();
        new StoreSpecialDataAsyncTask(dayOfWeek, specialCallback).execute();
    }

    //Class to make the server request for storing a user's data.
    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        //Class level variables.
        User mUser;
        GetUserCallback mUserCallback;

        //Constructor that accepts the user and user call back and sets them in the class variables.
        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback) {
            mUser = user;
            mUserCallback = userCallback;
        }

        //Override method to do background tasks.
        @Override
        protected Void doInBackground(Void... voids) {
            //Creates an arrayList to store the user's data to be sent over the database.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", mUser.getUserName()));
            dataToSend.add(new BasicNameValuePair("email", mUser.getEmail()));
            dataToSend.add(new BasicNameValuePair("password", mUser.getPassword()));
            dataToSend.add(new BasicNameValuePair("city", mUser.getCity()));
            dataToSend.add(new BasicNameValuePair("state", mUser.getState()));
            dataToSend.add(new BasicNameValuePair("cityid", mUser.getCityId() + ""));

            //Sets the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                //Sends the user's data over to the database.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                //Catches any exceptions that may occur and prints them to the stack trace.
                e.printStackTrace();
            }
            return null;
        }

        //Override method that is called once the background tasks are done.
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mUserCallback.done(null);
        }
    }

    //Class to make a server request to retrieve a user's data that is trying to log in.
    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        //Class variables
        User mUser;
        GetUserCallback mUserCallback;

        //Constructor that accepts the user and user call back and sets them in the class variables.
        public FetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            mUser = user;
            mUserCallback = userCallback;
        }

        //Override method to do background tasks.
        @Override
        protected User doInBackground(Void... params) {
            //Creates an arrayList to store the user's username and password to be sent over.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", mUser.getUserName()));
            dataToSend.add(new BasicNameValuePair("password", mUser.getPassword()));

            //Sets all the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            //Instantiates a new user to be returned and sets it to null.
            User returnedUser = null;

            try {
                //Executes the connection to the server.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                //Pulls the retrieved data and stores it in a JSONObject.
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                //the JSONObject is then separated into it's parts and stored locally.
                if (jObject.length() != 0) {
                    String email = jObject.getString("email");
                    String city = jObject.getString("city");
                    String state = jObject.getString("state");
                    int cityId = jObject.getInt("cityid");

                    //Creates a new user with the returned data from the database.
                    returnedUser = new User(mUser.getUserName(), email, mUser.getPassword(), city, state, cityId);
                }

            } catch (Exception e) {
                //Catches any exceptions that may occur and prints them to the stack trace.
                e.printStackTrace();
            }
            //Returns the newly created user.
            return returnedUser;
        }

        //Override method that is called once the background tasks are done.
        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mUserCallback.done(returnedUser);
        }
    }

    //Public class to fetch the user's username for a password change.
    public class FetchForgotLoginUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User mUser;
        GetUserCallback mUserCallback;

        public FetchForgotLoginUserDataAsyncTask(User user, GetUserCallback userCallback) {
            mUser = user;
            mUserCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            //Creates an array list to send over the entered data from the user to fetch the remaining data.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email", mUser.getEmail()));

            //Sets all the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            //Sets the client and post variables to execute the call to the database.
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchForgotLoginUserData.php");

            //Creates a new user to hold the returning data from the database.
            User returnedUser = null;

            try {
                //Executes the connection to the server.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                //Pulls the data from the server and stores it in a JSONObject.
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                //Loads the user's data from the JSONObject into local variables.
                if(jObject.length() != 0) {
                    String username = jObject.getString("username");
                    String password = jObject.getString("password");

                    //Creates a new user with the returned data.
                    returnedUser = new User(username, password);
                }

            } catch (Exception e) {
                //Catches all errors and prints them to the stack trace.
                e.printStackTrace();
            }
            //Returns the new user to the activity.
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mUserCallback.done(returnedUser);
        }
    }

    //Public class to update the user's location on the database end.
    public class UpdateUserLocationDataAsyncTask extends AsyncTask<Void, Void, User> {
        User mUser;
        GetUserCallback mUserCallback;

        //Constructor that accepts a user and user call back.
        public UpdateUserLocationDataAsyncTask(User user, GetUserCallback userCallback) {
            //Sets the sent over variables to the class variables.
            mUser = user;
            mUserCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            //Creates an array list with all the sent over data.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", mUser.getUserName()));
            dataToSend.add(new BasicNameValuePair("password", mUser.getPassword()));
            dataToSend.add(new BasicNameValuePair("state", mUser.getState()));
            dataToSend.add(new BasicNameValuePair("city", mUser.getCity()));
            dataToSend.add(new BasicNameValuePair("cityid", mUser.getCityId() + ""));

            //Sets the connection parameters.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            //Creates the client and post.
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateUserDetails.php");

            try {
                //Sets the data to the post then executes it on the client.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

            //Catches all exceptions.
            } catch (Exception e) {
                //Prints the exception to the stack trace.
                e.printStackTrace();
            }
            //Returns null.
            return null;
        }

        //Override method thats called after the background task is completed.
        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mUserCallback.done(returnedUser);
        }
    }

    //Class to retrieve the list of states from the database.
    public class FetchStateDataAsyncTask extends AsyncTask<Void, Void, State> {
        //Class variables.
        State mState;
        GetStateCallback mStateCallback;
        StateCollection mStateCollection;

        //Constructor that takes a placeholder state and stateCallback.
        public FetchStateDataAsyncTask(State state, GetStateCallback stateCallback) {
            mState = state;
            mStateCallback = stateCallback;
            mStateCollection = StateCollection.get();
            mStateCollection.removeList();
        }

        //Override method to do background tasks.
        @Override
        protected State doInBackground(Void... params) {
            //Sets all the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchStateData.php");

            //Instantiates a new state and sets it to null.
            State returnedState = null;

            try {
                //Executes the connection.
                HttpResponse httpResponse = client.execute(httpGet);

                //Pulls the retrieved data and stores it in a JSONObject.
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                //Loops through each state in the jObject.
                for(int i = 0; i < jObject.length(); i++) {
                    //Stores each state in local variables.
                    String stateName = jObject.getString((i + 1) + "");
                    int stateId = (i + 1);

                    //Creates a new state with the current index.
                    returnedState = new State(stateName, stateId);
                    //Sends new state over to be added to the arrayList.
                    mStateCollection.addState(returnedState);
                    //mStateArrayList.addState(returnedState);
                }

            }catch (Exception e) {
                //Catches any exceptions that may occur and prints them to the stack trace.
                e.printStackTrace();
            }
            //Returns the last state to show their was no null return.
            return returnedState;
        }

        //Override method that is called once the background tasks are done.
        @Override
        protected void onPostExecute(State returnedState) {
            super.onPostExecute(returnedState);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mStateCallback.done(returnedState);
        }
    }

    //Class to retrieve the list of cities based on selected state from the database.
    public class FetchCityDataAsyncTask extends AsyncTask<Void, Void, City> {
        //Class variables.
        City mCity;
        GetCityCallback mCityCallback;
        CityCollection mCityCollection;

        //Constructor that takes a placeholder city which contains a stateId and cityCallback.
        public FetchCityDataAsyncTask(City city, GetCityCallback cityCallback) {
            mCity = city;
            mCityCallback = cityCallback;
            mCityCollection = CityCollection.get();
            mCityCollection.removeList();
        }

        //Override method to do background tasks.
        @Override
        protected City doInBackground(Void... params) {
            //Creates an arrayList to store the stateId to be sent over to retrieve the cities of that state.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("stateid", mCity.getStateId() + ""));

            //Sets all the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchCitiesFromState.php");

            //Instantiates a new city and sets it to null.
            City returnedCity = null;

            try {
                //Executes the connection.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                //Pulls the retrieved data and stores it in a JSONObject.
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                //Creates a variable to collect the keys of each city.
                Iterator<String> keys = jObject.keys();

                //Loops through each city in the jObject.
                for(int i = 0; i < jObject.length(); i++) {
                    //Retrieves the next key and stores it in a string.
                    String key = keys.next();
                    //Stores each city in local variables.
                    int cityId = Integer.parseInt(key);
                    String cityName = jObject.getString(key);

                    //Creates a new city with the current index.
                    returnedCity = new City(mCity.getStateId(), cityName, cityId);
                    //Sends new city over to be added to the arrayList.
                    mCityCollection.addCity(returnedCity);
                }

            } catch (Exception e) {
                //Catches any exceptions that may occur and prints them to the stack trace.
                e.printStackTrace();
            }
            //Returns the last city to show their was no null return.
            return returnedCity;
        }

        //Override method that is called once the background tasks are done.
        @Override
        protected void onPostExecute(City city) {
            super.onPostExecute(city);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mCityCallback.done(city);
        }
    }

    //Class to retrieve the list of bars based on selected city from the database.
    public class FetchBarNamesDataAsyncTask extends AsyncTask<Void, Void, Bar> {
        //Class variables.
        Bar mBar;
        GetBarCallback mBarCallback;
        BarCollection mBarCollection;

        //Constructor that takes a placeholder bar which contains a cityId and barCallback.
        public FetchBarNamesDataAsyncTask(Bar bar, GetBarCallback barCallback) {
            mBar = bar;
            mBarCallback = barCallback;
            mBarCollection = BarCollection.get();
            mBarCollection.removeList();
        }

        //Override method to do background tasks.
        @Override
        protected Bar doInBackground(Void... params) {
            //Creates an arrayList to store the citId to be sent over to retrieve the bars of that city.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("cityid", mBar.getCityId() + ""));

            //Sets all the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchBarIdAndName.php");

            //Instantiates a new bar and sets it to null.
            Bar returnedBar = null;

            try {
                //Executes the connection.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                //Pulls the retrieved data and stores it in a JSONObject.
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                //Creates a variable to collect the keys of each bar.
                Iterator<String> keys = jObject.keys();

                //Loops through each bar in the jObject.
                for (int i = 0; i < jObject.length(); i++) {
                    //Retrieves the next key and stores it in a string.
                    String key = keys.next();
                    //Stores each bar in local variables.
                    int barId = Integer.parseInt(key);
                    String barName = jObject.getString(key);

                    //Creates a new bar with the current index.
                    returnedBar = new Bar(barId, barName);
                    //Sends new bar over to be added to the arrayList.
                    mBarCollection.addBar(returnedBar);
                }

            } catch (Exception e) {
                //Catches any exceptions that may occur and prints them to the stack trace.
                e.printStackTrace();
            }
            //Returns the last bar to show their was no null return.
            return returnedBar;
        }

        //Override method that is called once the background tasks are done.
        @Override
        protected void onPostExecute(Bar returnedBar) {
            super.onPostExecute(returnedBar);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mBarCallback.done(returnedBar);
        }
    }

    //Class to retrieve the details of a selected bar.
    public class FetchBarDetailsDataAsyncTask extends AsyncTask<Void, Void, Bar> {
        //Class variables.
        Bar mBar;
        GetBarCallback mBarCallback;

        //Constructor that takes a bar and barCallback.
        public FetchBarDetailsDataAsyncTask(Bar bar, GetBarCallback barCallback) {
            mBar = bar;
            mBarCallback = barCallback;
        }

        //Override method to do background tasks.
        @Override
        protected Bar doInBackground(Void... params) {
            //Creates an arrayList to store the barId to be sent over to retrieve that bar's details.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("barid", mBar.getBarId() + ""));

            //Sets all the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchBarData.php");

            //Instantiates a new bar and sets it to null.
            Bar returnedBar = null;

            try {
                //Executes the connection.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                //Pulls the retrieved data and stores it in a JSONObject.
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                //Checks to make sure the jObject isn't null.
                if(jObject.length() != 0) {
                    //Sets each part of the bar details into local variables.
                    String name = jObject.getString("name");
                    String address = jObject.getString("address");
                    String zipCode = jObject.getString("zipcode");
                    String phone = jObject.getString("phone");

                    //Creates a new bar with the data sent over from the database.
                    returnedBar = new Bar(mBar.getBarId(), name, address, mBar.getBarState(), mBar.getBarCity(), zipCode, phone);
                }

            } catch (Exception e) {
                //Catches any exceptions that may occur and prints them to the stack trace.
                e.printStackTrace();
            }
            //Returns the bar's details to be displayed on the screen.
            return returnedBar;
        }

        //Override method that is called once the background tasks are done.
        @Override
        protected void onPostExecute(Bar returnedBar) {
            super.onPostExecute(returnedBar);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mBarCallback.done(returnedBar);
        }
    }

    //Class to retrieve the specials of a selected bar.
    public class FetchBarSpecialsDataAsyncTask extends AsyncTask<Void, Void, DayOfWeek> {
        //Class variables.
        DayOfWeek mDayOfWeek;
        GetSpecialCallback mSpecialCallback;
        Special mSpecial;
        SpecialCollection mSpecialCollection;
        private boolean dayExists = false;

        //Constructor that accepts a dayOfWeek and specialCallback and stores them in class variables.
        public FetchBarSpecialsDataAsyncTask(DayOfWeek dayOfWeek, GetSpecialCallback specialCallback) {
            mDayOfWeek = dayOfWeek;
            mSpecialCallback = specialCallback;
            mSpecialCollection = SpecialCollection.get();
            mSpecialCollection.removeList();
        }

        //Override method to do background tasks.
        @Override
        protected DayOfWeek doInBackground(Void... params) {
            //Creates an arrayList to store the barId to be sent over to retrieve that bar's specials.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("barid", mDayOfWeek.getBarId() + ""));

            //Sets all the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchBarSpecials.php");

            try {
                //Executes the connection.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                //Pulls the retrieved data and stores it in a JSONObject
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(URLDecoder.decode(result, "UTF-8"));

                //Creates a variable to collect the keys of each bar.
                Iterator<String> keys = jObject.keys();

                //Loop that continues as long as there is a next key.
                while(keys.hasNext()) {
                    //Gets next key and sets it in the string key.
                    String key = keys.next();
                    //Gets the record of the current key.
                    JSONObject record = jObject.getJSONObject(key);

                    //Stores the current record into local variables.
                    String day = record.getString("dayofweek");
                    String special = record.getString("special");
                    String addedBy = record.getString("addedby");
                    String dateForSpecial = record.getString("dateforspecial");

                    //Checks to see if an instance of mSpecialArrayList exists.
                    if(mSpecialCollection.getDaysOfWeek() != null) {
                        //Loops through each index of the dayOfWeek arrayList.
                        for(DayOfWeek dayOfWeek : mSpecialCollection.getDaysOfWeek()){
                            //Checks to see if the current index's data matches the current sent over record.
                            if(dayOfWeek.getDate().equals(dateForSpecial)) {
                                //Creates a new special with the sent over data.
                                mSpecial = new Special(special, addedBy);
                                //Adds the newly created special into the current indexed day.
                                mSpecialCollection.addSpecialToExistingDay(dayOfWeek, mSpecial);
                                //Sets the dayExists boolean to true.
                                dayExists = true;
                                //breaks from loop since the special has been stored in it's appropriate place.
                                break;
                            }
                        }
                    }
                    //Checks to see if the dayExists boolean is false.
                    if(dayExists == false) {
                        //Creates a new special with the sent over data.
                        mSpecial = new Special(special, addedBy);
                        //Creates a new day in the week and creates it from the send over data.
                        mDayOfWeek = new DayOfWeek(day, dateForSpecial);
                        //Adds the newly created special into the newly created day of the week.
                        mDayOfWeek.addSpecial(mSpecial);
                        //Adds the newly created day into the list of days in the week.
                        mSpecialCollection.addDayToWeek(mDayOfWeek);
                    }
                    //Resets dayExists to false for the next record.
                    dayExists = false;
                }

            } catch (Exception e) {
                //Catches any exceptions that may occur and prints them to the stack trace.
                e.printStackTrace();
                //If there is an error mDayOfWeek is set to null so it's returned that way.
                mDayOfWeek = null;
            }
            //Returns the last dayOfWeek to show their was no null return.
            return mDayOfWeek;
        }

        //Override method that is called once the background tasks are done.
        @Override
        protected void onPostExecute(DayOfWeek returnedDayOfWeek) {
            super.onPostExecute(returnedDayOfWeek);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mSpecialCallback.done(returnedDayOfWeek);
        }
    }

    //Class to make the server request for storing a new special.
    public class StoreSpecialDataAsyncTask extends AsyncTask<Void, Void, Void> {
        //Class Variables.
        DayOfWeek mDayOfWeek;
        GetSpecialCallback mSpecialCallback;
        String mDescription;
        String mAddedBy;

        //Constructor that accepts the dayOfWeek and specialCallback and sets them in the class variables.
        public StoreSpecialDataAsyncTask(DayOfWeek dayOfWeek, GetSpecialCallback specialCallback) {
            mDayOfWeek = dayOfWeek;
            mSpecialCallback = specialCallback;
            //Calls method to extract the special data.
            extractSpecialData();
        }

        //Private method that extracts the special data from mDayOfWeek.
        private void extractSpecialData() {
            //Creates a new list and stores the special data into it.
            List<Special> extractSpecial = mDayOfWeek.getSpecials();
            //Extracts the single special and stores it in a special class variable.
            Special extractSingleSpecial = extractSpecial.get(0);

            //Extracts the description from the special variable.
            mDescription = extractSingleSpecial.getSpecialDescription();
            //Extracts the addedBy data from the special variable.
            mAddedBy = extractSingleSpecial.getAddedBy();
        }

        //Override method to do background tasks.
        @Override
        protected Void doInBackground(Void... params) {
            //Creates an arrayList to store the special details to be sent over to the database.
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("barid", mDayOfWeek.getBarId() + ""));
            dataToSend.add(new BasicNameValuePair("day", mDayOfWeek.getDayOfWeekString()));
            dataToSend.add(new BasicNameValuePair("description", mDescription));
            dataToSend.add(new BasicNameValuePair("addedby", mAddedBy));
            dataToSend.add(new BasicNameValuePair("date", mDayOfWeek.getDate()));

            //Sets all the HTTP variables to connect to the database.
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "AddSpecial.php");

            try {
                //Executes the connection.
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                //Catches any exceptions that may occur and prints them to the stack trace.
                e.printStackTrace();
            }
            //Returns null
            return null;
        }

        //Override method that is called once the background tasks are done.
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Dismisses the progress dialog.
            mProgressDialog.dismiss();
            //Triggers interface method to trigger done override method in activity class.
            mSpecialCallback.done(null);
        }
    }
}
