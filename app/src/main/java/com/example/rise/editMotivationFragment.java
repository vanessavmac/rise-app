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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editMotivationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editMotivationFragment extends Fragment {
    private String UID;
    private UserOptions userOptions = new UserOptions();
    private static final String ARG_PARAM1 = "UID";

    public editMotivationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment editMotivationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static editMotivationFragment newInstance(String param1) {
        editMotivationFragment fragment = new editMotivationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_motivation, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText myGoal = (EditText) getView().findViewById(R.id.editTextTaskName);
        myGoal.setHint("my goal");
        EditText motivationReason = (EditText) getView().findViewById(R.id.editTextTaskDuration);
        motivationReason.setHint("how will it help you accomplish your goal?");

        view.findViewById(R.id.editTextTaskName).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    myGoal.setHint("");
                    myGoal.setTextColor(getResources().getColor(R.color.black));
                } else if (!hasFocus && myGoal.getText().toString().matches("")) {
                    myGoal.setHint("my goal");
                    myGoal.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    myGoal.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        view.findViewById(R.id.editTextTaskDuration).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    motivationReason.setHint("");
                    motivationReason.setTextColor(getResources().getColor(R.color.black));
                } else if (!hasFocus && motivationReason.getText().toString().matches("")) {
                    motivationReason.setHint("how will it help you accomplish your goal?");
                    motivationReason.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    motivationReason.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        view.findViewById(R.id.buttonEditTaskDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goal = myGoal.getText().toString();
                String reason = motivationReason.getText().toString();

                Log.d("editMotivationFragment", goal);
                Log.d("editMotivationFragment", reason);

                //START HERE: Add goal and reason to the database using the UserOptions class
                userOptions.dbSetTodaysMotivation(UID, goal, reason);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, homeFragment.newInstance(UID), null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit();

            }
        });
    }
}