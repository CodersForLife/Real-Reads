package com.vizy.newsapp.realread.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vizy.newsapp.realread.R;

public class OnBoardingFragment extends Fragment implements View.OnClickListener{

    private int position;

    public OnBoardingFragment() {
        // Required empty public constructor
    }

    public void setPosition(int i) {
        position = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_on_boarding, container, false);
        ImageView image = (ImageView) rootView.findViewById(R.id.onboarding_image);
        image.setImageDrawable(getResources().obtainTypedArray(R.array.onBoarding_images).getDrawable(position));
        rootView.setBackgroundColor(getResources().obtainTypedArray(R.array.onBoarding_colors).getColor(position, Color.TRANSPARENT));
        TextView onboardingHeading = (TextView) rootView.findViewById(R.id.onboard_text_head);
        onboardingHeading.setText(getResources().obtainTypedArray(R.array.onBoarding_headings).getText(position));
        TextView onboardSubHeading = (TextView) rootView.findViewById(R.id.onboard_sub_text);
        onboardSubHeading.setText(getResources().obtainTypedArray(R.array.onBoarding_sub_headings).getText(position));
        return rootView;
    }

    @Override
    public void onClick(View view) {

    }
}
