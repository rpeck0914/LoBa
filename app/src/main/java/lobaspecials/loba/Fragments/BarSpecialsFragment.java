package lobaspecials.loba.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import lobaspecials.loba.Objects.DayOfWeek;
import lobaspecials.loba.Objects.Special;
import lobaspecials.loba.R;
import lobaspecials.loba.Singletons.SpecialCollection;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarSpecialsFragment extends Fragment {
    //Bar Specials Fragment holds the specials for the selected bar.

    //Layout variables.
    private TextView mDayOfTheWeek, mDate;
    private View mLinearLayoutHolder;

    //Class variables.
    private DayOfWeek mDayOfWeek;

    //Static key for starting the fragment.
    private static final String ARG_BAR_SPECIAL_DAY = "day_of_week";

    //Public method for when creating a new instance of the fragment.
    public static BarSpecialsFragment newInstance(String date) {
        Bundle args = new Bundle();
        args.putString(ARG_BAR_SPECIAL_DAY, date);
        BarSpecialsFragment fragment = new BarSpecialsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieves the arguments that were stored when creating the instance of the fragment.
        String date = getArguments().getString(ARG_BAR_SPECIAL_DAY);
        //Gets the collection of specials from the singleton.
        mDayOfWeek = SpecialCollection.get().getSpecialDay(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Creates the view of the fragment.
        View v = inflater.inflate(R.layout.fragment_bar_specials,container, false);

        //Finds the linear layout that will hold the bar speicals.
        mLinearLayoutHolder = v.findViewById(R.id.special_holder_for_text_views);

        //Set the text view that will display the day of week for the specials.
        mDayOfTheWeek = (TextView) v.findViewById(R.id.day_of_the_week_title);
        mDayOfTheWeek.setText(mDayOfWeek.getDayOfWeekString());

        //Sets the text view that will display the date for the speicals.
        mDate = (TextView) v.findViewById(R.id.month_and_date_title);
        mDate.setText(mDayOfWeek.getDate());

        //Creates a new list of specials and gets the specials for the day of week.
        List<Special> mSpecials = mDayOfWeek.getSpecials();

        //Loops through all the specials in the list.
        for (int i = 0; i < mDayOfWeek.getSpecials().size(); i++){

            //The text for each special is dynamically made.
            //The title for the special is output along with the user that uploaded the special.

            Special special = mSpecials.get(i);
            String details = special.getSpecialDescription();
            String addedBy = special.getAddedBy();

            TextView detailsHolder = new TextView(getActivity());
            detailsHolder.setId(i);
            detailsHolder.setText(details);
            detailsHolder.setTextSize(20);
            detailsHolder.setTypeface(Typeface.DEFAULT_BOLD);
            detailsHolder.setGravity(Gravity.RIGHT);
            detailsHolder.setTextColor(getResources().getColor(R.color.single_bar_text_color));
            LinearLayout.LayoutParams detailsHolderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            detailsHolderParams.setMargins(0,10,0,0);
            ((LinearLayout) mLinearLayoutHolder).addView(detailsHolder);

            TextView addedByHolder = new TextView(getActivity());
            addedByHolder.setId(i);
            addedByHolder.setText(addedBy);
            addedByHolder.setTextSize(14);
            addedByHolder.setGravity(Gravity.RIGHT);
            addedByHolder.setTextColor(getResources().getColor(R.color.single_bar_text_color));
            LinearLayout.LayoutParams addedByHolderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            addedByHolderParams.setMargins(0,0,0,10);
            ((LinearLayout) mLinearLayoutHolder). addView(addedByHolder);
        }
        return v;
    }
}
