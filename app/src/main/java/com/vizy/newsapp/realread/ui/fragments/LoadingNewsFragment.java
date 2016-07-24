package com.vizy.newsapp.realread.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vizy.newsapp.realread.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoadingNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoadingNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadingNewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView description,headline;
    ImageView image;
    Button share1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoadingNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoadingNewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoadingNewsFragment newInstance(String param1, String param2) {
        LoadingNewsFragment fragment = new LoadingNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_loading_news, container, false);
        share1=(Button)v.findViewById(R.id.share);
        headline=(TextView)v.findViewById(R.id.headline);
        description=(TextView)v.findViewById(R.id.description);
        image=(ImageView)v.findViewById(R.id.image);

        String json=getArguments().getString("json");
        int index=getArguments().getInt("index");

        try {
            JSONObject obj=new JSONObject(json);
            JSONArray arr=obj.getJSONArray("articles");
            JSONObject obj2=arr.getJSONObject(index);
            headline.setText(obj2.getString("title"));
            description.setText(obj2.getString("description"));


            Picasso.with(getActivity()).load(obj2.getString("urlToImage")).into(image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        share1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String json=getArguments().getString("json");
                    int index=getArguments().getInt("index");

                    JSONObject obj = new JSONObject(json);
                    JSONArray arr = obj.getJSONArray("articles");
                    JSONObject obj2 = arr.getJSONObject(index);

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Here is the share content body";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, obj2.getString("title"));
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, obj2.getString("description"));
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MainActivity", e.getMessage().toString());
                }
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
