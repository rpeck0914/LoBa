package lobaspecials.loba.Utilities;

import android.content.Context;

import lobaspecials.loba.Interfaces.CityTrigger;
import lobaspecials.loba.Interfaces.GetCityCallback;
import lobaspecials.loba.Interfaces.GetStateCallback;
import lobaspecials.loba.Interfaces.StateTrigger;
import lobaspecials.loba.Objects.City;
import lobaspecials.loba.Objects.State;

/**
 * Created by Robert Peck on 2/6/2016.
 */
public class ServerRequestsConnector {

    public ServerRequestsConnector() {

    }

    public void pullStates(final Context context, State state, final StateTrigger stateTrigger) {
        ServerRequests serverRequests = new ServerRequests(context);
        serverRequests.fetchStateDataAsyncTask(state, new GetStateCallback() {
            @Override
            public void done(State returnedState) {
                if(returnedState == null) {
                    String errorMessage = "Error Loading States";
                    new ErrorMessage(context, errorMessage);
                } else {
                    stateTrigger.stateTrigger(returnedState);
                    //trigger the override to kick back out. Send the returned state back.
                }
            }
        });
    }

    public void pullCities(final Context context, final City city, final CityTrigger cityTrigger) {
        ServerRequests serverRequests = new ServerRequests(context);
        serverRequests.fetchCityDataAsyncTask(city, new GetCityCallback() {
            @Override
            public void done(City returnedCities) {
                if(returnedCities == null) {
                    String errorMessage = "Error Loading Cities";
                    new ErrorMessage(context, errorMessage);
                } else {
                    cityTrigger.cityTrigger(returnedCities);
                    //trigger the override to kick back out. send the returned city back.
                }
            }
        });
    }
}
