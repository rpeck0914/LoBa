package lobaspecials.loba.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import lobaspecials.loba.Activities.SingleBarDetailsActivity;
import lobaspecials.loba.Objects.Bar;
import lobaspecials.loba.R;
import lobaspecials.loba.Singletons.BarCollection;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarNameFragment extends Fragment {
    //Bar name fragment holds the bar names for the selected city.

    //Layout variables.
    private RecyclerView mBarRecyclerView;
    private BarAdapter mAdapter;

    //Empty constructor.
    public BarNameFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflates the fragment view.
        View v = inflater.inflate(R.layout.fragment_bar_name, container, false);

        //Sets the recycler view to the variable.
        mBarRecyclerView = (RecyclerView) v.findViewById(R.id.bar_recycler_view);
        mBarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Calls the update ui method.
        updateUI();
        //Returns the view.
        return v;
    }

    //Override method that is called when the fragment is resumed.
    @Override
    public void onResume() {
        super.onResume();
        //Calls the update ui method.
        updateUI();
    }

    //Public method that calls the update ui method. This allows the update ui method to be called from the activity.
    public void runUpdate(){
        updateUI();
    }

    //Private method that updates the ui.
    private void updateUI() {
        //Gets the collection of bars stored in the singleton.
        BarCollection barCollection = BarCollection.get();
        //Sets the bar collection to the list of bars.
        List<Bar> mBar = barCollection.getBars();

        //Creates a new adapter and sets it to the recycler view.
        mAdapter = new BarAdapter(mBar);
        mBarRecyclerView.setAdapter(mAdapter);
    }

    //Private class for the holder of the recycler view.
    private class BarHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Sets the text view for each bar.
        private TextView mBarNameTextView;

        //Creates a new bar object.
        private Bar mBar;

        //Constructor that takes the view.
        public BarHolder(View itemView) {
            super(itemView);
            //Sets the on click listener for each bar name object.
            itemView.setOnClickListener(this);

            //Links the text view variable with the layout.
            mBarNameTextView = (TextView) itemView.findViewById(R.id.bar_name);
            Typeface myFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/blzee.ttf");
            mBarNameTextView.setTypeface(myFont);
        }

        //Public method to bind the bar
        public void bindBar(Bar bar) {
            //Sets the the bar with the sent over bar.
            mBar = bar;
            //Sets the text for the current bar.
            mBarNameTextView.setText(mBar.getBarName());
        }

        //Override method for the on click listener.
        @Override
        public void onClick(View v) {
            //Starts the single bar details activity when a bar is clicked.
            Intent intent = SingleBarDetailsActivity.newIntent(getActivity(), mBar.getBarId() + "");
            startActivity(intent);
        }
    }

    //Private class that is the adapter for the recycler view.
    private class BarAdapter extends RecyclerView.Adapter<BarHolder> {
        //Variable to hold a list of bars.
        private List<Bar> mBar;

        //Constructor for BarAdapter.
        public BarAdapter(List<Bar> bar) {
            mBar = bar;
        }

        //Override method for creating the view of each bar in the recycler view.
        @Override
        public BarHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_bar, viewGroup, false);
            return new BarHolder(v);
        }

        //Override method to bind the bar to the holder.
        @Override
        public void onBindViewHolder(BarHolder holder, int position) {
            Bar bar = mBar.get(position);
            holder.bindBar(bar);
        }

        //Override method to get the size of the list of bars.
        @Override
        public int getItemCount() {
            return mBar.size();
        }
    }
}
