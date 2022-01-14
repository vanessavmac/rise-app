package com.example.rise;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addRoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addRoutineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String UID;

    public addRoutineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment addRoutineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addRoutineFragment newInstance(String param1) {
        addRoutineFragment fragment = new addRoutineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);

        Log.d("addRoutineFragment", "UID Added: " + param1);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("addRoutineFragment", "I'm here");
        if (getArguments() != null) {
            UID = getArguments().getString(ARG_PARAM1);
            Log.d("addRoutineFragment", "UID getted: " + UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_routine, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.submitNewRoutine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextName = getActivity().findViewById(R.id.editRoutineName);
                TimePicker timePicker = getActivity().findViewById(R.id.timePicker);
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String routineName = editTextName.getText().toString();

                ArrayList<ArrayList<UserInfo>> routineList = UserOptions.readListFromPref(getContext());
                Log.d("addRoutineFragment", "Before: " + routineList.toString());

                UserOptions.addRoutine(UID, getActivity(), routineName, hour, minute, routineList);

                ArrayList<ArrayList<UserInfo>> routineListTest = UserOptions.readListFromPref(getContext());
                Log.d("addRoutineFragment", routineListTest.toString());

                homeFragment newFragment = new homeFragment();
                Log.d("addRoutineFragment", "TEST UID: " + UID);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, newFragment.newInstance(UID), null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit();
            }
        });

    }

}