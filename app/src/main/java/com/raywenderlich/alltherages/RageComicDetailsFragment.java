package com.raywenderlich.alltherages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RageComicDetailsFragment extends Fragment {
    private static final String ARGUMENT_NAME = "name";
    private static final String ARGUMENT_CITY = "city";
    private static final String ARGUMENT_HOUSE = "house";
    private static final String ARGUMENT_YEAR = "year";

    public static RageComicDetailsFragment newInstance( String name,
                                                       String city, String house,String year) {

        final Bundle args = new Bundle();
        args.putString(ARGUMENT_NAME, name);
        args.putString(ARGUMENT_CITY, city);
        args.putString(ARGUMENT_HOUSE, house);
        args.putString(ARGUMENT_YEAR, year);
        final RageComicDetailsFragment fragment = new RageComicDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RageComicDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rage_comic_details, container, false);
        final TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView descriptionTextView = (TextView) view.findViewById(R.id.description);

        final Bundle args = getArguments();
        nameTextView.setText(args.getString(ARGUMENT_NAME));
        final String text = String.format(getString(R.string.description_format), args.getString
                (ARGUMENT_CITY), args.getString(ARGUMENT_YEAR),args.getString(ARGUMENT_HOUSE));
        descriptionTextView.setText(text);
        return view;
    }
}