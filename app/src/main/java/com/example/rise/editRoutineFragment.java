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
 * Use the {@link editRoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editRoutineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String originalRoutineName;
    private String uid;

    public editRoutineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment editRoutineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static editRoutineFragment newInstance(String param1, String param2) {
        editRoutineFragment fragment = new editRoutineFragment();
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
            originalRoutineName = getArguments().getString(ARG_PARAM1);
            uid = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_routine, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ArrayList<ArrayList<UserInfo>> routineList = UserOptions.readListFromPref(getContext());
        UserOptions userOptions = new UserOptions();
//        String originalRoutineStartTime = userOptions.getRoutineStartTime(originalRoutineName, routineList);

        EditText inputRoutineName = getActivity().findViewById(R.id.editRoutineName);
        inputRoutineName.setText(originalRoutineName);
        TimePicker timePicker = getActivity().findViewById(R.id.timePicker);
//        timePicker.setCurrentHour(Integer.valueOf(originalRoutineStartTime.substring(0,2)));
//        timePicker.setCurrentMinute(Integer.valueOf(originalRoutineStartTime.substring(3,5)));


        view.findViewById(R.id.submitNewRoutine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userOptions.editRoutine(uid, getActivity(), originalRoutineName, String.valueOf(inputRoutineName.getText()), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), routineList);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, homeFragment.newInstance(uid), null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit();
            }
        });
    }
}