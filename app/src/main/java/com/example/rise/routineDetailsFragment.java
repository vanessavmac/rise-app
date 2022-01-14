package com.example.rise;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link routineDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class routineDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String routineName;
    private String uid;
    private UserOptions userOptions = new UserOptions();


    public routineDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment routineDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static routineDetailsFragment newInstance(String param1, String param2) {
        routineDetailsFragment fragment = new routineDetailsFragment();
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
            routineName = getArguments().getString(ARG_PARAM1);
            uid = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine_details, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView fragmentHeader = view.findViewById(R.id.routineDetailsHeader);
        Log.d("routineDetailsFragment", "Weird routine header: " + routineName);
        fragmentHeader.setText(routineName);

        view.findViewById(R.id.buttonEditRoutine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, R.menu.popup_menu);
            }
        });

        ArrayList<ArrayList<UserInfo>> routineList = UserOptions.readListFromPref(getContext());
        Log.d("routineDetailsFragment", routineList.toString());
        UserOptions.displayEvents(uid, getActivity(), routineList, routineName);
    }

    private void showMenu(View view, int popup_menu) {
        PopupMenu popup = new PopupMenu(new ContextThemeWrapper(getContext(), R.style.popupMenuStyle), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                switch (item.getItemId()) {
                    case R.id.option_1:
                        Log.d("routineDetails", "option_1");
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_view, editRoutineFragment.newInstance(routineName, uid), null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null) // name can be null
                                .commit();
                        break;
                    case R.id.option_2:
                        ArrayList<ArrayList<UserInfo>> routineList = UserOptions.readListFromPref(getContext());
                        userOptions.deleteRoutine(uid, getActivity(), routineName, routineList);
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_view, homeFragment.newInstance(uid), null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null) // name can be null
                                .commit();
                        break;
                    default:
                        Log.d("routineDetails", "default");
                        break;
                }
                return true;
            }
        });

        popup.show();

//
//        ConstraintLayout parentLayout = getActivity().findViewById(R.id.fragment_routine_details);
//        ConstraintSet set = new ConstraintSet();
//        set.clone(parentLayout);
//        set.connect(popup_menu, ConstraintSet.TOP, getActivity().findViewById(R.id.buttonEditRoutine).getId(), ConstraintSet.BOTTOM, 30);
//        set.connect(popup_menu, ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 1000);
//        set.applyTo(parentLayout);
//        set.clone(parentLayout);

//        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
//            // Respond to menu item click.
//        }
//        popup.setOnDismissListener {
//            // Respond to popup being dismissed.
//        }
        // Show the popup menu.
    }

}