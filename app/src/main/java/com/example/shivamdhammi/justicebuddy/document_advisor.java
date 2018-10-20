package com.example.shivamdhammi.justicebuddy.Fragmentstab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shivamdhammi.justicebuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link document_advisor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link document_advisor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class document_advisor extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document_advisor, container, false);

        return view;


    }
}