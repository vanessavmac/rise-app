package com.example.rise;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loadingFragment extends Fragment {
    Thread welcomeThread;
    UserOptions options;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public loadingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment loadingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static loadingFragment newInstance() {
        loadingFragment fragment = new loadingFragment();
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
        return inflater.inflate(R.layout.fragment_loading, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //Check if user exists
        options = new UserOptions();
        options.checkUserExists();

        welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(500);  //Delay of 100 milliseconds
                } catch (Exception e) {

                } finally {
                    startHomeFragment();
                    options.writeListInPref(getContext());
                }
            }
        };
        welcomeThread.start();
    }

    public void startHomeFragment() {
        //PROBLEM -- Need to run this and change the fragment only after all the database information is retrieved (aka the array is initialized with all the routines)
        String UID = getUID();

        if (options.checkArrayInitialized()) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, homeFragment.newInstance(UID), null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null) // name can be null
                    .commit();
        } else {
            welcomeThread.start();
        }
    }

    public String getUID() {
        String UID = options.getUID();
        return options.getUID();
    }

}