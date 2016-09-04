/*
 * Copyright (c) 2015 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.alltherages;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.app.Activity;

import java.util.ArrayList;

public class RageComicListFragment extends Fragment {

    private int[] mImageResIds;
    private String[] mNames;
    private String[] mDescriptions;
    private String[] mUrls;
    private OnRageComicSelected mListener;
    private ArrayList<String> names;
    private ArrayList<String> cities;
    private ArrayList<String> house;
    private ArrayList<String> years;

    public static RageComicListFragment newInstance() {
        return new RageComicListFragment();
    }

    public RageComicListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        names=(ArrayList)getArguments().getSerializable("names");
        cities=(ArrayList)getArguments().getSerializable("country");
        house=(ArrayList)getArguments().getSerializable("house");
        years=(ArrayList)getArguments().getSerializable("years");
        if (context instanceof OnRageComicSelected) {
            mListener = (OnRageComicSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRageComicSelected.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rage_comic_list, container, false);
        final Activity activity = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));
        recyclerView.setAdapter(new RageComicAdapter(activity));
        return view;
    }

    class RageComicAdapter extends RecyclerView.Adapter<ViewHolder> {

        private LayoutInflater mLayoutInflater;

        public RageComicAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return new ViewHolder(mLayoutInflater
                    .inflate(R.layout.recycler_item_rage_comic, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            final String name = names.get(position);
            final String year = years.get(position);
            final String houses = house.get(position);
            final String city = cities.get(position);
            viewHolder.setData(name,year,houses);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRageComicSelected(name, year, city,houses);
                }
            });
        }

        @Override
        public int getItemCount() {
            return names.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Views
        private TextView mNameTextView;
        private TextView mHeader;
        private TextView mDesc;

        private ViewHolder(View itemView) {
            super(itemView);
            // Get references to image and name.
            mNameTextView = (TextView) itemView.findViewById(R.id.name);
            mHeader = (TextView) itemView.findViewById(R.id.header);
            mDesc = (TextView) itemView.findViewById(R.id.description);
        }

        private void setData(String name, String year, String house) {
            mNameTextView.setText(house);
            mHeader.setText(name);
            mDesc.setText(year);
        }
    }

    public interface OnRageComicSelected {
        void onRageComicSelected(String name, String year, String city,String house);
    }
}
