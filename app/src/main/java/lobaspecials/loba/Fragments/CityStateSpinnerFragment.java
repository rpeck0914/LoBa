package lobaspecials.loba.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

import lobaspecials.loba.Interfaces.CityTrigger;
import lobaspecials.loba.Interfaces.GetBarCallback;
import lobaspecials.loba.Interfaces.GetCityCallback;
import lobaspecials.loba.Interfaces.GetStateCallback;
import lobaspecials.loba.Interfaces.LoadRecyclerViewCommunicator;
import lobaspecials.loba.Interfaces.StateTrigger;
import lobaspecials.loba.Objects.Bar;
import lobaspecials.loba.Objects.City;
import lobaspecials.loba.Objects.State;
import lobaspecials.loba.Objects.User;
import lobaspecials.loba.R;
import lobaspecials.loba.Singletons.BarCollection;
import lobaspecials.loba.Singletons.CityCollection;
import lobaspecials.loba.Singletons.StateCollection;
import lobaspecials.loba.Utilities.ErrorMessage;
import lobaspecials.loba.Utilities.ServerRequests;
import lobaspecials.loba.Utilities.ServerRequestsConnector;
import lobaspecials.loba.Utilities.UserLocalStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityStateSpinnerFragment extends Fragment {
    //CityStateSpinnerFragment holds spinners to display spinners of the state and their cities.

    //Class Level Variables.

    //Variables for the layout.
    private Spinner mStateSpinner, mCitySpinner;
    //Variables for code.
    private int mSelectedCityId;
    private boolean mStatesLoadedFlag = false, mCitiesLoadedFlag = false;
    //Variables for class objects.
    private UserLocalStore mUserLocalStore;
    private Bar mBar;
    private BarCollection mBarCollection;
    private StateCollection mStateCollection;
    private CityCollection mCityCollection;
    private ServerRequestsConnector mServerRequestsConnector;
    //Variable for interface.
    private LoadRecyclerViewCommunicator mLoadRecyclerViewCommunicator;

    public CityStateSpinnerFragment() {
        // Required empty public constructor
    }

    //Override for creating the fragments view and declaring variables.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Creates a view variable to inflate the fragments view.
        View v = inflater.inflate(R.layout.fragment_city_state_spinner, container, false);

        //Sets spinner variables to the fragment layout.
        mStateSpinner = (Spinner) v.findViewById(R.id.enter_state);
        mCitySpinner = (Spinner) v.findViewById(R.id.enter_city);

        mServerRequestsConnector = new ServerRequestsConnector();

        //Declares a new dummy state to pull the states from the database.
        State state = new State();
        //Calls pullStates method sending over the dummy state object.
        pullStates(state);

        //Instantiates the userLocalStore to pull the user's data from the phone.
        mUserLocalStore = new UserLocalStore(getActivity());

        //Instantiates the interface to let the activity know once the spinners are loaded.
        mLoadRecyclerViewCommunicator = (LoadRecyclerViewCommunicator) getActivity();

        //Returns the view.
        return v;
    }

    public void updateUI() {
        mStatesLoadedFlag = false;
        mCitiesLoadedFlag = false;
        loadStateSpinner();
    }

    //Private method to make a server request and pull the states data from the database.
    private void pullStates(State state) {
        //Calls connector class to make a server request to pull the states.
        mServerRequestsConnector.pullStates(getActivity(), state, new StateTrigger() {
            //Override that triggers once the state data has been pulled from the database.
            @Override
            public void stateTrigger(State returnedState) {
                //Calls the load state spinner method.
                loadStateSpinner();
            }
        });
    }

    //Private method to make a server request and pull the cities data from the database.
    private void pullCities(City city) {
        //Calls connector class to make a server request to pull the cities based on the selected state.
        mServerRequestsConnector.pullCities(getActivity(), city, new CityTrigger() {
            //Override that triggers once the city data has been pulled from the database.
            @Override
            public void cityTrigger(City returnedCities) {
                //Calls the load city spinner method.
                loadCitySpinner();
            }
        });
    }

    //Private method to load the state spinner.
    private void loadStateSpinner() {
        //Gets the singleton's data.
        mStateCollection = StateCollection.get();
        //Creates a string type list to store the state names.
        List<String> mStateNames = new ArrayList<>();
        //Loops through each state object in the StateCollection list.
        for(int i = 0; i < mStateCollection.getStates().size(); i++) {
            //Adds the name of each state from the StateCollection's list into the newly created string list.
            mStateNames.add(mStateCollection.getStates().get(i).getStateName());
        }

        //Loads The State Spinner With The Array Of States
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mStateNames);
        mStateSpinner.setAdapter(adapter);
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                //Override Method For Selected A Different State In The State Spinner

                //Finds The Selected State's ID To Fetch The Cities Within That State
                String selectedState = mStateSpinner.getSelectedItem().toString();
                //Loops through each state to find the selected state from the spinner.
                for (int i = 0; i < mStateCollection.getStates().size(); i++) {
                    //Checks to see if the selected state name matches the index in the StateCollection's list.
                    if (mStateCollection.getStates().get(i).getStateName().equals(selectedState)) {
                        //Creates A New City With The StateID
                        City city = new City(mStateCollection.getStates().get(i).getStateId());
                        //Stores the selected state.
                        mUserLocalStore.setSelectedCityAndState(selectedState, "State");
                        //Calls The pullCites Method And Sends The Cities Object Over
                        pullCities(city);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });
        //Calls the setUsersStateAndCitySpinners method to set state to the user's saved state.
        setUsersStateAndCitySpinners(mStateNames, "state");
    }

    //Private method to load the city spinner.
    private void loadCitySpinner() {
        //Gets the singleton's data.
        mCityCollection = CityCollection.get();
        //Creates a string type list to store the cities names.
        List<String> mCityNames = new ArrayList<>();
        //Loops through each city object in the CityCollection list.
        for(int i = 0; i < mCityCollection.getCities().size(); i++) {
            //Adds the name of each city from the CityCollection's list into the newly created string list.
            mCityNames.add(mCityCollection.getCities().get(i).getCityName());
        }

        //Loads The City Spinner With The Array Of Cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mCityNames);
        mCitySpinner.setAdapter(adapter);
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> aro0, View arg1, int arg2, long arg3) {
                String selectedCity = mCitySpinner.getSelectedItem().toString();
                for (int i = 0; i < mCityCollection.getCities().size(); i++) {
                    if (mCityCollection.getCities().get(i).getCityName().equals(selectedCity)) {
                        //Gets the current cities id and stores it in the mSelectedCityId.
                        mSelectedCityId = mCityCollection.getCities().get(i).getCityId();
                        //Clears out the BarCollection list to update it with new data from the newly selected city.
                        mBarCollection = BarCollection.get();
                        mBarCollection.removeList();
                        //Calls pullBars method to pull the bars from that city.
                        pullBars();
                        //Calls the interface to tell the activity to update the other fragment with the bar data.
                        mLoadRecyclerViewCommunicator.loadRecyclerView("updateUI");
                        //Stores the selected city.
                        mUserLocalStore.setSelectedCityAndState(selectedCity, "City");
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Override Method For When Nothing Is Selected In The State Spinner??
            }
        });
        //Calls the setUsersStateAndCitySpinners method to set city to the user's saved state.
        setUsersStateAndCitySpinners(mCityNames, "city");
    }

    //Private method to set the spinners to the user's saved state and city data.
    private void setUsersStateAndCitySpinners(List<String> name, String id) {

        //Creates a user and retrieves the user's data.
        User user = mUserLocalStore.getLoggedInUser();

        //check to make sure their is a user logged in and makes sure the flag is false. This if statement should only run once.
        if(user != null && id == "state" && mStatesLoadedFlag == false) {
            //Gathers the state from the user's stored data and prepares it to be compared with another value.
            String stateWithNulls = user.getState();
            String state = stateWithNulls.replaceAll("\\u0000", "");
            //Loop that goes through all the states and finds the one saved in the user's data.
            for (int i = 0; i < name.size(); i++) {
                if (state.compareToIgnoreCase(name.get(i)) == 0) {
                    //Once a match is found the spinner is set to that state.
                    mStateSpinner.setSelection(i);
                    //Sets the flag to true so this if statement never executes again.
                    mStatesLoadedFlag = true;
                    //sets i to max to exit loop.
                    i = name.size();
                }
            }
        }

        //check to make sure their is a user logged in and makes sure the flag is false. This if statement should only run once.
        if(user != null && id == "city" && mCitiesLoadedFlag == false) {
            //Gathers the city from the user's stored data and prepares it to be compared with another value.
            String cityWithNulls = user.getCity();
            String city = cityWithNulls.replaceAll("\\u0000", "");
            //Loop that goes through all the cities and finds the one saved in the user's data.
            for (int i = 0; i < name.size(); i++) {
                if (city.compareToIgnoreCase(name.get(i).toString()) == 0) {
                    //Once a match is found the spinner is set to that city.
                    mCitySpinner.setSelection(i);
                    //Sets the flag to true so this if statement never executes again.
                    mCitiesLoadedFlag = true;
                    //sets i to max to exit loop.
                    i = name.size();
                }
            }
        }
    }

    //private method to pull the bar's name and id's.
    private void pullBars() {
        //Creates a new bar with the selected cities id.
        mBar = new Bar(mSelectedCityId);

        //Makes server request.
        ServerRequests serverRequests = new ServerRequests(getActivity());
        serverRequests.fetchBarNamesDataAsyncTask(mBar, new GetBarCallback() {
            @Override
            public void done(Bar returnedBar) {
                //Checks to see if the returned value is null.
                if (returnedBar == null) {
                    //Creates the error message.
                    String title = "No Bars Found For This City";
                    String errorMessage = "If You Would Like To Request A Bar To Be \n" +
                                          "Added, Please Send A Request From The \n" +
                                          "Profile Screen";
                    //Sends the error message to the ErrorMessage class to be displayed.
                    new ErrorMessage(getActivity(), errorMessage, title);
                } else {
                    //mBar = returnedBar;
                    //Calls the interface to tell the activity to update the other fragment with the bar data.
                    mLoadRecyclerViewCommunicator.loadRecyclerView("updateUI");
                }
            }
        });
    }
}
