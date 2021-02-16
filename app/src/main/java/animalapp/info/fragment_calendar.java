package animalapp.info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragment_calendar extends Fragment {
    View view;

    public fragment_calendar() {
        // Required empty public constructor
    }

    public static fragment_calendar newInstance(){
        fragment_calendar frg_cal=new fragment_calendar();
        return frg_cal;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
}