package lobaspecials.loba.Singletons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lobaspecials.loba.Objects.City;
import lobaspecials.loba.Objects.State;

/**
 * Created by Robert Peck on 12/2/2015.
 */
public class CityCollection {
    //CityCollection is a singleton to store the data of all the cities from a selected state.

    //Private static instance of the class CityCollection.
    private static CityCollection sCityCollection;

    //List to store all the city objects.
    private List<City> mCities;

    //Public static method to create/get the single instance of CityCollection.
    public static CityCollection get() {
        //Checks to see if an instance of the class exists.
        if(sCityCollection == null) {
            //If it doesn't exist a new one is created.
            sCityCollection = new CityCollection();
        }
        //Returns the newly created or already existing instance of CityCollection.
        return sCityCollection;
    }

    //Constructor to instantiate the list so it can be used to add a new city.
    private CityCollection() {
        mCities = new ArrayList<>();
    }

    //public method to add a city to the list
    public void addCity(City city) {
        //Adds city to the list.
        mCities.add(city);
        //Calls sort method to sort the cities alphabetically.
        sortArrayListByCityName();
    }

    //Private method to sort the list of cities based on their name.
    private void sortArrayListByCityName() {
        Collections.sort(mCities, new Comparator<City>() {
            @Override
            public int compare(City lhs, City rhs) {
                return lhs.getCityName().compareTo(rhs.getCityName());
            }
        });
    }

    public void removeList() {
        mCities.clear();
    }

    //Getter to get the list of cities
    public List<City> getCities() {
        return mCities;
    }
}
