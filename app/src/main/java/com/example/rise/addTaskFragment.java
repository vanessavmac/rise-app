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
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String UID;
    private String routineName;

    public addTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment addTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addTaskFragment newInstance(String param1, String param2) {
        addTaskFragment fragment = new addTaskFragment();
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
            UID = getArguments().getString(ARG_PARAM1);
            routineName = getArguments().getString(ARG_PARAM2);
            Log.d("addTaskFragment", "routineName= " + routineName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        NumberPicker picker1 = view.findViewById(R.id.hourNumberPicker);
        NumberPicker picker2 = view.findViewById(R.id.minuteNumberPicker);
        picker1.setMaxValue(24);
        picker1.setMinValue(0);
        picker2.setMaxValue(60);
        picker2.setMinValue(0);

        view.findViewById(R.id.buttonSubmitTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextName = getActivity().findViewById(R.id.addTaskName);
                String taskName = editTextName.getText().toString();
                int durationHours = picker1.getValue();
                int durationMinutes = picker2.getValue();

                ArrayList<ArrayList<UserInfo>> routineList = UserOptions.readListFromPref(getContext());

                Log.d("addTaskFragment", "Before: " + routineList.toString());

                UserOptions options = new UserOptions();
                options.addTask(UID, getActivity(), routineName, taskName, durationHours, durationMinutes, routineList);


                ArrayList<ArrayList<UserInfo>> routineListTest = UserOptions.readListFromPref(getContext());
                Log.d("addTaskFragment", routineListTest.toString());


                Log.d("addRoutineFragment", "TEST UID: " + UID);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, routineDetailsFragment.newInstance(routineName, UID), null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit();

            }
        });
    }
}