package lobaspecials.loba.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import lobaspecials.loba.Activities.SingleBarDetailsActivity;
import lobaspecials.loba.Interfaces.GetBarCallback;
import lobaspecials.loba.Interfaces.GetSpecialCallback;
import lobaspecials.loba.Interfaces.LoadSpecialsCommunicator;
import lobaspecials.loba.Objects.Bar;
import lobaspecials.loba.Objects.DayOfWeek;
import lobaspecials.loba.R;
import lobaspecials.loba.Utilities.ErrorMessage;
import lobaspecials.loba.Utilities.ServerRequests;
import lobaspecials.loba.Utilities.StateAbbreviations;
import lobaspecials.loba.Utilities.UserLocalStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleBarDetailsFragment extends Fragment implements View.OnClickListener {
    //Single bar details fragment holds the selected bar's name, address, and phone number.

    //Layout variables.
    private TextView mBarName, mBarAddress, mBarCity, mBarState, mBarZipCode, mBarPhone;
    private ImageView mMapsImage, mPhoneImage;

    //Class variables.
    private Bar mBar;
    private UserLocalStore mUserLocalStore;
    private LoadSpecialsCommunicator mLoadSpecialsCommunicator;

    //Empty constructor.
    public SingleBarDetailsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Creates the view to be inflated.
        View v = inflater.inflate(R.layout.fragment_single_bar_details, container, false);

        //Links all the layout objects to the variables.
        mBarName = (TextView) v.findViewById(R.id.single_bar_name_title);
        mBarAddress = (TextView) v.findViewById(R.id.single_bar_address);
        mBarCity = (TextView) v.findViewById(R.id.single_bar_city);
        mBarState = (TextView) v.findViewById(R.id.single_bar_state);
        mBarZipCode = (TextView) v.findViewById(R.id.single_bar_zip_code);
        mBarPhone = (TextView) v.findViewById(R.id.single_bar_phone);

        mMapsImage = (ImageView) v.findViewById(R.id.map_image);
        mPhoneImage = (ImageView) v.findViewById(R.id.phone_image);

        //Sets the on click listener for all clickable objects on the layout.
        mMapsImage.setOnClickListener(this);
        mPhoneImage.setOnClickListener(this);
        mBarAddress.setOnClickListener(this);
        mBarPhone.setOnClickListener(this);

        //Instantiates the user local store to pull user data.
        mUserLocalStore = new UserLocalStore(getActivity());

        //Abbreviates the user's state for when the bar address is output.
        String state = new StateAbbreviations(mUserLocalStore.getSelectedCityAndState("selectedState")).getStateAbbrev();
        //Creates a new bar object.
        mBar = new Bar(SingleBarDetailsActivity.getBarId(), state, mUserLocalStore.getSelectedCityAndState("selectedCity"));
        //Calls the pull bar info method.
        pullBarInfo(mBar);

        //Instantiates the load specials communicator.
        mLoadSpecialsCommunicator = (LoadSpecialsCommunicator) getActivity();

        return v;
    }

    @Override
    public void onClick(View v) {
        //Override for the click of objects on the layout.

        //Switch statement checks to see which id is touched in the layout.
        switch (v.getId()) {
            case R.id.map_image:
                //Calls the google maps method if image is clicked.
                googleMapsIntentCall();
                break;

            case R.id.phone_image:
                //Calls the phone intent method if image is clicked.
                phoneIntentCall();
                break;

            case R.id.single_bar_address:
                //Calls the google maps method if address is clicked.
                googleMapsIntentCall();
                break;

            case R.id.single_bar_phone:
                //Calls the phone intent method if the phone number is clicked.
                phoneIntentCall();
                break;
        }
    }

    //Private method to pull the bar's info.
    private void pullBarInfo(Bar bar) {
        //Makes a server request to pull the bar's info.
        ServerRequests serverRequests = new ServerRequests(getActivity());
        serverRequests.fetchBarDetailsDataAsyncTask(bar, new GetBarCallback() {
            @Override
            public void done(Bar returnedBar) {
                //If the returned data is null an error message will be displayed to the user.
                if(returnedBar == null) {
                    String errorMessage = "Error Collecting Bar Details";
                    new ErrorMessage(getActivity(), errorMessage);
                //If the returned data is successfully returned.
                } else {
                    //Fill bar data to layout method is called using the returned bar data.
                    fillBarDataToLayout(returnedBar);
                    //Pull bar specials method is called using the returned bar data.
                    pullBarSpecials(returnedBar);
                }
            }
        });
    }

    //Private method to fill the bar data to the layout.
    private void fillBarDataToLayout(Bar bar) {
        //Sets all the text view's to the selected bar data.
        mBarName.setText(bar.getBarName());
        mBarAddress.setText(bar.getBarAddress());
        mBarState.setText(bar.getBarState() + " ");
        mBarCity.setText(bar.getBarCity() + ", ");
        mBarZipCode.setText(bar.getBarZipCode());
        mBarPhone.setText(bar.getBarPhone());
    }

    //Public method to pull the bar specials.
    public void pullBarSpecials(Bar bar) {
        //Creates a day of week object with the selected bar id.
        DayOfWeek dayOfWeek = new DayOfWeek(bar.getBarId());
        //Makes a server request to collect the specials of the selected bar.
        ServerRequests serverRequests = new ServerRequests(getActivity());
        serverRequests.fetchBarSpecialsDataAsyncTask(dayOfWeek, new GetSpecialCallback() {
            @Override
            public void done(DayOfWeek returnedDayOfWeek) {
                //If the returned object is null then there are no specials for the bar.
                if (returnedDayOfWeek == null) {
                    //Sends an error string over through the communicator.
                    mLoadSpecialsCommunicator.loadViewPager("ErrorLoadingSpecials");
                } else {
                    //Sends a success string over through the communicator.
                    mLoadSpecialsCommunicator.loadViewPager("LoadBarSpecials");
                }
            }
        });
    }

    //Private method that calls an intent to open google maps with the bar's address sent over.
    private void googleMapsIntentCall() {
        //Creates an Intent with the address string to open google maps.
        String addressString = (mBarAddress.getText().toString() + " " + mBarCity.getText().toString() + mBarState.getText().toString() + mBarZipCode.getText().toString());
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + addressString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        //Starts the activity with the created intent.
        startActivity(mapIntent);
    }

    //Private method that calls an intent
    private void phoneIntentCall() {
        //Creates an intent to open the phone with the bar's phone number.
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
        phoneIntent.setData(Uri.parse("tel:" + mBarPhone.getText().toString()));
        //Starts activity with the created intent.
        startActivity(phoneIntent);
    }

    //Public method to return the bar.
    public Bar getBar() {
        return mBar;
    }
}
