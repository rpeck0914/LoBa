package lobaspecials.loba.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lobaspecials.loba.Fragments.BarSpecialsFragment;
import lobaspecials.loba.Fragments.SingleBarDetailsFragment;
import lobaspecials.loba.Interfaces.LoadSpecialsCommunicator;
import lobaspecials.loba.Objects.DayOfWeek;
import lobaspecials.loba.R;
import lobaspecials.loba.Singletons.SpecialCollection;
import lobaspecials.loba.Utilities.ErrorMessage;
import lobaspecials.loba.Utilities.UserLocalStore;

public class SingleBarDetailsActivity extends FragmentActivity implements View.OnClickListener, LoadSpecialsCommunicator {
    //Single bar details activity displays the selected bar's details for the user.

    //Layout variables.
    private TextView mBackButton, mAddSpecialButton;
    private ViewPager mViewPager;

    //Class level variables.
    private List<DayOfWeek> mDayOfWeeks;
    private boolean backBoolean = false;
    private static int mBarId;

    //Class variables.
    private UserLocalStore mUserLocalStore;
    private ErrorMessage mErrorMessage;

    //Static key for starting a new intent for this activity.
    private static final String EXTRA_BAR_ID = "lobaspecials.loba.bar_id";

    //Public method for making an new intent when starting this activity.
    public static Intent newIntent(Context context, String barId) {
        Intent intent = new Intent(context, SingleBarDetailsActivity.class);
        intent.putExtra(EXTRA_BAR_ID, barId);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bar_details);

        //Pulls out the key that was used to start this activity.
        mBarId = Integer.parseInt(getIntent().getStringExtra(EXTRA_BAR_ID));

        //Loads fragment 1 which contains the bar's name, address and phone number.
        FragmentManager fm1 = getSupportFragmentManager();
        Fragment fragment1 = fm1.findFragmentById(R.id.single_bar_details_top_fragment_container);

        //Inserts fragment 1 into the container.
        if(fragment1 == null) {
            fragment1 = new SingleBarDetailsFragment();
            fm1.beginTransaction()
                    .add(R.id.single_bar_details_top_fragment_container, fragment1)
                    .commit();
        }

        //Sets the layout variables to the objects on the layout.
        mViewPager = (ViewPager) findViewById(R.id.activity_bar_special_pager_view_pager);
        mDayOfWeeks = SpecialCollection.get().getDaysOfWeek();
        mBackButton = (TextView) findViewById(R.id.back_button);
        mAddSpecialButton = (TextView) findViewById(R.id.add_bar_special);

        //Instantiates the user local store variable.
        mUserLocalStore = new UserLocalStore(this);

        //Sets the on click listener to all clickable objects on the layout.
        mBackButton.setOnClickListener(this);
        mAddSpecialButton.setOnClickListener(this);
    }

    //Override method that's called when the activity resumes from being paused.
    @Override
    protected void onResume() {
        super.onResume();
        if(backBoolean) {
            clearSpecialList();
            FragmentManager manager = getSupportFragmentManager();
            SingleBarDetailsFragment f1 = (SingleBarDetailsFragment) manager.findFragmentById(R.id.single_bar_details_top_fragment_container);
            f1.pullBarSpecials(f1.getBar());
            backBoolean = true;
        }
    }

    //Override method that's called when the activity is destroyed.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSpecialList();
    }

    //Private method to load the bar specials.
    private void loadBarSpecials() {
        //Locates the fragment that holds the bar specials.
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Sets the adapter to the view pager.
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                //Override method to get the position on the view pager.
                DayOfWeek dayOfWeek = mDayOfWeeks.get(position);
                return BarSpecialsFragment.newInstance(dayOfWeek.getDate());
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public int getCount() {
                return mDayOfWeeks.size();
            }
        });

        //creates a new date to find the current date.
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = month + "/" + day;

        //Boolean to show a date is found.
        boolean foundDate = false;

        //Loops through all the specials based on their date to find the one that matches the current date.
        for(int i = 0; i < mDayOfWeeks.size(); i++) {
            if(mDayOfWeeks.get(i).getDate().equals(dateString)) {
                mViewPager.setCurrentItem(i);
                foundDate = true;
                break;
            }
        }
        //If there is no specials for the current date, the special is set to the most recent special.
        if(foundDate == false) {
            mViewPager.setCurrentItem(mDayOfWeeks.size());
        }
    }

    //Override method for the on click listener.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bar_special:
                //Starts the activity to add a special to the selected bar.
                backBoolean = true;
                Intent intent = AddBarSpecialActivity.newIntent(this, mBarId + "");
                startActivity(intent);
                break;
            case R.id.back_button:
                //Calls for the activity to finish if back button is clicked.
                this.finish();
                break;
        }
    }

    //Override method to load the view pager.
    @Override
    public void loadViewPager(String name) {
        switch (name) {
            //If there was an error loading the specials an error message will displayed to the user saying so.
            case "ErrorLoadingSpecials":
                String title = "No Bar Specials Listed";
                String errorMessage =  "If You Would Like To Add A Special,\nPlease Hit The Add Special Link";
                mErrorMessage = new ErrorMessage(this, errorMessage, title);
                break;
            //If there are no errors the load bar specials method is called.
            case "LoadBarSpecials":
                loadBarSpecials();
                break;
        }
    }

    //Private method to clear the list of bar specials that is being stored in the specials singleton.
    private void clearSpecialList() {
        SpecialCollection specialCollection = SpecialCollection.get();
        specialCollection.removeList();
    }

    //Public method to return the bar id.
    public static int getBarId() {
        return mBarId;
    }
}
