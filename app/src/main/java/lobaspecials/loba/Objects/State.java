package lobaspecials.loba.Objects;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class State {
    //State class creates and stores a states data of it's name and id in the database.

    //Variables to hold the state names and their ids.
    private String mStateName;
    private int mStateId;

    //Empty constructor for instantiating the class when pulling state data from the database.
    public State() { }

    //Constructor to make a state with the name and id of that state.
    public State(String stateName, int stateId) {
        mStateName = stateName;
        mStateId = stateId;
    }

    //Getter methods to retrieve the data from a State.
    public String getStateName() {
        return mStateName;
    }

    public int getStateId() {
        return mStateId;
    }
}
