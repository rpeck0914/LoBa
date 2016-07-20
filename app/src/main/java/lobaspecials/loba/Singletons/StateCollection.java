package lobaspecials.loba.Singletons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lobaspecials.loba.Objects.State;

/**
 * Created by Robert Peck on 12/2/2015.
 */
public class StateCollection {
    //StateCollection is a singleton to store the data of all the states from the database.

    //Private static instance of the class StateCollection.
    private static StateCollection sStateCollection;

    //List to store the state objects.
    private List<State> mStates;

    //Public static method to create/get the single instance of StateCollection.
    public static StateCollection get() {
        //Checks to see if an instance of the class exists.
        if(sStateCollection == null) {
            //If it doesn't exist a new one is created.
            sStateCollection = new StateCollection();
        }
        //Returns the newly created or already existing instance of StateCollection.
        return sStateCollection;
    }

    //Constructor to instantiate the list so it can be used to add new states.
    private StateCollection() {
        mStates = new ArrayList<>();
    }

    //Public method to add a state to the list.
    public void addState(State state) {
        //Adds state to the list.
        mStates.add(state);
        //Calls sort method to sort the states alphabetically.
        sortArrayListByStateName();
    }

    //Private method to sort the list of states based on their name.
    private void sortArrayListByStateName(){
        Collections.sort(mStates, new Comparator<State>() {
            @Override
            public int compare(State lhs, State rhs) {
                return lhs.getStateName().compareTo(rhs.getStateName());
            }
        });
    }

    public void removeList(){
        mStates.clear();
    }

    //Getter to get the list of states.
    public List<State> getStates() {
        return mStates;
    }
}
