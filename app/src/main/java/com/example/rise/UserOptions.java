package com.example.rise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class UserOptions {
    private static final String TAG = "UserOptions.java";
    private DocumentReference mDocRef;
    private String value;

    private UserInfo userInfo;
    private ArrayList<ArrayList<UserInfo>> routineList;
    private ArrayList<UserInfo> newRoutine;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String UID;
    private String test;
    private int counter = 0;
    private int counterGoal = 0;
    private boolean arrayInitialized = false;

    // first element in the inner array will be the routine name and time

    public UserOptions() {
        routineList = new ArrayList<ArrayList<UserInfo>>();
    }

    //Testing
    public void printArray() {
        Log.d(TAG, routineList.toString());
    }

    public void writeListInPref(Context context) {
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(routineList);
//
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("list_key", jsonString);
//        editor.apply();

        Map<String, String> unSortedMap = new HashMap<>();
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mma");
        Date parseDate = new Date();

        for (int i = 0; i < routineList.size(); i++) {
            ArrayList<UserInfo> currentRoutine = routineList.get(i);
            UserInfo routineInfo = currentRoutine.get(0);
            try {
                parseDate = parseFormat.parse(routineInfo.getTime());
                unSortedMap.put(String.valueOf(i), outputFormat.format(parseDate));
            } catch (ParseException e) {
                Log.d(TAG, "DateFormat parse exception.");
                e.printStackTrace();
            }
            unSortedMap.put(String.valueOf(i), outputFormat.format(parseDate));

        }

        Log.d(TAG, "unSortedMap: " + unSortedMap.toString());

        // Now let's sort the HashMap by values
        // there is no direct way to sort HashMap by values but you
        // can do this by writing your own comparator, which takes
        // Map.Entry object and arrange them in order increasing
        // or decreasing by values.

        Set<Entry<String, String>> entries = unSortedMap.entrySet();

        Comparator<Entry<String, String>> valueComparator
                = new Comparator<Entry<String, String>>() {

            @Override
            public int compare(Entry<String, String> e1, Entry<String, String> e2) {
                String v1 = e1.getValue();
                String v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };

        // Sort method needs a List, so let's first convert Set to List in Java
        List<Entry<String, String>> listOfEntries
                = new ArrayList<Entry<String, String>>(entries);

        // sorting HashMap by values using comparator
        Collections.sort(listOfEntries, valueComparator);

        LinkedHashMap<String, String> sortedByValue
                = new LinkedHashMap<String, String>(listOfEntries.size());

        // copying entries from List to Map
        for (Entry<String, String> entry : listOfEntries) {
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        Log.d(TAG, "sortedByValue: " + sortedByValue.toString());

        //Sort the original list into sortedArrayList
        ArrayList<ArrayList<UserInfo>> sortedArrayList = new ArrayList<ArrayList<UserInfo>>();
        for (Entry<String, String> entry : listOfEntries) {
            sortedArrayList.add(routineList.get(Integer.valueOf(entry.getKey())));
        }

        Log.d(TAG, "sortedArrayList: " + sortedArrayList.toString());

        Gson gson = new Gson();
        String jsonString = gson.toJson(sortedArrayList);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().commit();
        editor.putString("list_key", jsonString);
        editor.apply();
    }

    public static void writeListInPref(Context context, ArrayList<ArrayList<UserInfo>> list) {
        Map<String, String> unSortedMap = new HashMap<>();
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mma");
        Date parseDate = new Date();

        for (int i = 0; i < list.size(); i++) {
            ArrayList<UserInfo> currentRoutine = list.get(i);
            UserInfo routineInfo = currentRoutine.get(0);
            try {
                parseDate = parseFormat.parse(routineInfo.getTime());
                unSortedMap.put(String.valueOf(i), outputFormat.format(parseDate));
            } catch (ParseException e) {
                Log.d(TAG, "DateFormat parse exception.");
                e.printStackTrace();
            }
            unSortedMap.put(String.valueOf(i), outputFormat.format(parseDate));

        }

        Log.d(TAG, "unSortedMap: " + unSortedMap.toString());

        // Now let's sort the HashMap by values
        // there is no direct way to sort HashMap by values but you
        // can do this by writing your own comparator, which takes
        // Map.Entry object and arrange them in order increasing
        // or decreasing by values.

        Set<Entry<String, String>> entries = unSortedMap.entrySet();

        Comparator<Entry<String, String>> valueComparator
                = new Comparator<Entry<String, String>>() {

            @Override
            public int compare(Entry<String, String> e1, Entry<String, String> e2) {
                String v1 = e1.getValue();
                String v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };

        // Sort method needs a List, so let's first convert Set to List in Java
        List<Entry<String, String>> listOfEntries
                = new ArrayList<Entry<String, String>>(entries);

        // sorting HashMap by values using comparator
        Collections.sort(listOfEntries, valueComparator);

        LinkedHashMap<String, String> sortedByValue
                = new LinkedHashMap<String, String>(listOfEntries.size());

        // copying entries from List to Map
        for (Entry<String, String> entry : listOfEntries) {
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        Log.d(TAG, "sortedByValue: " + sortedByValue.toString());

        //Sort the original list into sortedArrayList
        ArrayList<ArrayList<UserInfo>> sortedArrayList = new ArrayList<ArrayList<UserInfo>>();
        for (Entry<String, String> entry : listOfEntries) {
            sortedArrayList.add(list.get(Integer.valueOf(entry.getKey())));
        }

        Log.d(TAG, "sortedArrayList: " + sortedArrayList.toString());

        Gson gson = new Gson();
        String jsonString = gson.toJson(sortedArrayList);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().commit();
        editor.putString("list_key", jsonString);
        editor.apply();
    }

    public static ArrayList<ArrayList<UserInfo>> readListFromPref(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString("list_key", "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ArrayList<UserInfo>>>() {
        }.getType();
        ArrayList<ArrayList<UserInfo>> list = gson.fromJson(jsonString, type);
        return list;
    }

    //Database Reading

    public String getUID() {
        return UID;
    }

    public boolean checkArrayInitialized() {
        return arrayInitialized;
    }

    public void checkUserExists() {
        UID = user.getUid();
        mDocRef = db.collection("users").document(UID);
        Log.d(TAG, String.valueOf(UID));
        test = "this works!";

        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        createNewUser();
                    } else {
                        //Start intializing array
                        getCounterGoal();
                    }
                } else {
                    Log.d(TAG, "Task Unsuccessful 1.");
                }
            }
        });
    }

    public void getCounterGoal() {
        UID = user.getUid();
        mDocRef = db.collection("users").document(UID).collection("counters").document("totalNumEvents");

        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        Log.d(TAG, "totalNumEvents counter does not exist.");
                    } else {
                        Map<String, Object> counterDoc = document.getData();
                        String counterGoalStr = counterDoc.get("value").toString();
                        counterGoal = Integer.valueOf(counterGoalStr);
                        initializeRoutineList();
                    }
                } else {
                    Log.d(TAG, "Task Unsuccessful 2.");
                }
            }
        });
    }

    public void initializeRoutineList() {
        //Get all the documents in routine list
        //After creating a query object, use the get() function to retrieve the results of the query:
        //In addition, you can retrieve all documents in a collection by omitting the where() filter entirely:
        db.collection("users").document(UID).collection("routine list")
                .get()
                /*A DocumentSnapshot contains data read from a document in your Firestore database.
                The data can be extracted with .data() or .get(<field>) to get a specific field.
                https://firebase.google.com/docs/reference/node/firebase.firestore.DocumentSnapshot*/
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> routine = document.getData();
                            String startTime = routine.get("start time").toString();
                            String processedStartTime = "";

                            for (char c : startTime.toCharArray()) {
                                if (c == 'A' | c == 'P') {
                                    processedStartTime = processedStartTime.substring(0, processedStartTime.length() - 1);
                                    processedStartTime += c;
                                } else if (c == ',') {
                                    processedStartTime += ":";
                                } else if (c != '[' && c != ' ' && c != ']') {
                                    processedStartTime += c;
                                }
                            }
//                            if (processedStartTime.substring(0, 1).equals("0")) {
//                                processedStartTime = processedStartTime.substring(1);
//                            }

                            newRoutine = new ArrayList<UserInfo>();
                            newRoutine.add(new UserInfo((String) routine.get("routine name"), processedStartTime));
                            routineList.add(newRoutine);
                            initializeRoutineEvents(document.getId(), routineList.size() - 1);
                        }

                        Log.d(TAG, "TEST: " + routineList.toString());

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public void initializeRoutineEvents(String docId, int index) {
        db.collection("users").document(UID).collection("routine list").document(docId).collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> event = document.getData();
                            ArrayList<UserInfo> currentRoutine = routineList.get(index);
                            currentRoutine.add(new UserInfo((String) event.get("event name"), (String) event.get("duration")));
                            routineList.set(index, currentRoutine);
                            counter++;

                            if (counter == counterGoal) {
                                arrayInitialized = true;
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    //Database Changes

    public void createNewUser() {
        String name = user.getDisplayName();
        String arr[] = name.split(" ", 2);
        String firstName = arr[0];
        String email = user.getEmail();

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("first name", firstName);

        db.collection("users").document(UID).set(user);
    }

    public void dbSetTodaysMotivation(String UID, String goal, String reason) {
        Log.d(TAG, "Goal: " + goal);
        Log.d(TAG, "Reason: " + reason);

        if (!goal.equals("") && !reason.equals("")) {
            Log.d(TAG, "weird");

            Map<String, Object> motivation = new HashMap<>();
            motivation.put("goal", goal);
            motivation.put("reason", reason);
            db.collection("users").document(UID).collection("other information").document("today's motivation").set(motivation);

            mDocRef = db.collection("users").document(UID).collection("other information").document("today's motivation");
            mDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String value1 = documentSnapshot.getString("goal");
                    String value2 = documentSnapshot.getString("reason");
                    Log.d(TAG, "Goal in database: " + value1);
                    Log.d(TAG, "Reason in database: " + value2);
                }
            });
        }
    }

    public void dbAddRoutine(String UID, FragmentActivity activity, String name, int startHour, int startMinute) {
        //Add start time as an "array" to db - must use Arrays.asList()
        //https://firebase.google.com/docs/firestore/manage-data/add-data#custom_objects
        String[] startTime = new String[3];

        if (startHour > 12) {
            startTime[0] = String.valueOf(startHour - 12);
            startTime[1] = String.valueOf(startMinute);
            startTime[2] = "PM";
        } else if (startHour == 12) {
            startTime[0] = String.valueOf(startHour);
            startTime[1] = String.valueOf(startMinute);
            startTime[2] = "PM";
        } else {
            startTime[0] = String.valueOf(startHour);
            startTime[1] = String.valueOf(startMinute);
            startTime[2] = "AM";
        }

        if (Integer.valueOf(startTime[1]) < 10) {
            startTime[1] = "0" + startTime[1];
        }

        if (Integer.valueOf(startTime[0]) < 10) {
            startTime[0] = "0" + startTime[0];
        }

        Map<String, Object> routineInfo = new HashMap<>();

        /* TODO: Add processing to duration */
        routineInfo.put("duration", 0);
        routineInfo.put("routine name", name);
        Log.d(TAG, "name: " + Arrays.asList(startTime).toString());
        routineInfo.put("start time", Arrays.asList(startTime));

        mDocRef = db.collection("users").document(UID).collection("routine list").document(name);
        mDocRef.set(routineInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "RoutineInfo successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "RoutineInfo writing error", e);
                    }
                });
    }

    private void dbAddTask(String uid, FragmentActivity activity, String routineName, String taskName, int durationHours, int durationMinutes) {
        Map<String, Object> taskInfo = new HashMap<>();
        String duration = String.valueOf(Integer.valueOf(durationHours) * 60 + Integer.valueOf(durationMinutes));

        /* TODO: Add processing to duration */
        taskInfo.put("duration", duration);
        taskInfo.put("event name", taskName);
        Log.d(TAG, "duration test: " + duration);
        Log.d(TAG, "taskname: " + taskName);
        Log.d(TAG, "routineName test: " + routineName);

        mDocRef = db.collection("users").document(uid).collection("routine list").document(routineName).collection("events").document(taskName);
        mDocRef.set(taskInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "EventInfo successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "EventInfo writing error", e);
                    }
                });
    }

    private void dbEditRoutine(String UID, FragmentActivity editRoutineFragment, String oldRoutineName, String newRoutineName, int newRoutineStartHour, int newRoutineStartMinutes) {

        mDocRef = db.collection("users").document(UID).collection("routine list").document(oldRoutineName);
        mDocRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        //Add start time as an "array" to db - must use Arrays.asList()
        //https://firebase.google.com/docs/firestore/manage-data/add-data#custom_objects
        String[] startTime = new String[3];

        if (newRoutineStartHour > 12) {
            startTime[0] = String.valueOf(newRoutineStartHour - 12);
            startTime[1] = String.valueOf(newRoutineStartMinutes);
            startTime[2] = "PM";
        } else if (newRoutineStartHour == 12) {
            startTime[0] = String.valueOf(newRoutineStartHour);
            startTime[1] = String.valueOf(newRoutineStartMinutes);
            startTime[2] = "PM";
        } else {
            startTime[0] = String.valueOf(newRoutineStartHour);
            startTime[1] = String.valueOf(newRoutineStartMinutes);
            startTime[2] = "AM";
        }

        if (Integer.valueOf(startTime[1]) < 10) {
            startTime[1] = "0" + startTime[1];
        }

        if (Integer.valueOf(startTime[0]) < 10) {
            startTime[0] = "0" + startTime[0];
        }

        Map<String, Object> routineInfo = new HashMap<>();

        /* TODO: Add processing to duration */
        routineInfo.put("duration", 0);
        routineInfo.put("routine name", newRoutineName);
        Log.d(TAG, "name: " + Arrays.asList(startTime).toString());
        routineInfo.put("start time", Arrays.asList(startTime));

        mDocRef = db.collection("users").document(UID).collection("routine list").document(newRoutineName);
        mDocRef.set(routineInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "RoutineInfo successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "RoutineInfo writing error", e);
                    }
                });

        Log.d(TAG, "Line 570: oldRoutineName: " + oldRoutineName);
        Log.d(TAG, "Line 570: newRoutineName: " + newRoutineName);

        db.collection("users").document(UID).collection("routine list").document(oldRoutineName).collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> event = document.getData();
                            db.collection("users").document(UID).collection("routine list").document(newRoutineName).collection("events").document(event.get("event name").toString())
                                    .set(event)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Event successfully transferred!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Event successfuly transferred", e);
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });

//
//        if (!newRoutineName.equals(oldRoutineName)) {
//            mDocRef.update("routine name", newRoutineName)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d(TAG, "DocumentSnapshot successfully updated!");
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error updating document", e);
//                        }
//                    });
//        }
//
//        String[] startTime = new String[3];
//
//        if (newRoutineStartHour > 12) {
//            startTime[0] = String.valueOf(newRoutineStartHour - 12);
//            startTime[1] = String.valueOf(newRoutineStartMinutes);
//            startTime[2] = "PM";
//        } else if (newRoutineStartHour == 12) {
//            startTime[0] = String.valueOf(newRoutineStartHour);
//            startTime[1] = String.valueOf(newRoutineStartMinutes);
//            startTime[2] = "PM";
//        } else {
//            startTime[0] = String.valueOf(newRoutineStartHour);
//            startTime[1] = String.valueOf(newRoutineStartMinutes);
//            startTime[2] = "AM";
//        }
//
//        if (Integer.valueOf(startTime[1]) < 10) {
//            startTime[1] = "0" + startTime[1];
//        }
//
//        Map<String, Object> updates = new HashMap<>();
//        Log.d(TAG, "name: " + Arrays.asList(startTime).toString());
//        updates.put("start time", Arrays.asList(startTime));
//
//        mDocRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
//            // [START_EXCLUDE]
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {}
//            // [START_EXCLUDE]
//        });
    }

    public void dbDeleteRoutine(String UID, FragmentActivity activity, String routineName) {
        mDocRef = db.collection("users").document(UID).collection("routine list").document(routineName);
        mDocRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        db.collection("users").document(UID).collection("routine list").document(routineName).collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(UID).collection("routine list").document(routineName).collection("events").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });

    }

    // Local Storage Changes

    public static void addRoutine(String UID, FragmentActivity activity, String name, int startHour, int startMinute, ArrayList<ArrayList<UserInfo>> list) {

        if (list.size() != 0) {
            //for loops add then check
            for (int i = 0; i < list.size(); i++) {
                ArrayList<UserInfo> currentRoutine = list.get(i);
                UserInfo userInfo = new UserInfo(name);
                if (userInfo.equals(currentRoutine.get(0))) {
                    Log.d(TAG, "Routine already exists.\n");
                    i = list.size();
                } else if (i == list.size() - 1) {
                    ArrayList<UserInfo> newRoutine = new ArrayList<UserInfo>();
                    Time tme = new Time(startHour, startMinute, 0);//seconds by default set to zero
                    Format formatter = new SimpleDateFormat("h:mma");
                    String startTime = formatter.format(tme);
                    newRoutine.add(new UserInfo(name, startTime));
                    list.add(newRoutine);
                    writeListInPref(activity, list);
                    //Must add this line; routineList is an array list; size is dynamic
                    //when a new element was added the for loop goes one more time due to new element
                    i = list.size();

                    UserOptions optionsObj = new UserOptions();
                    optionsObj.dbAddRoutine(UID, activity, name, startHour, startMinute);
                }
            }
        } else {
            String startTime = "test";
            ArrayList<UserInfo> newRoutine = new ArrayList<UserInfo>();
            newRoutine.add(new UserInfo(name, startTime));
            list.add(newRoutine);
            Log.d(TAG, "Routine created.\n");

            UserOptions optionsObj = new UserOptions();
            optionsObj.dbAddRoutine(UID, activity, name, startHour, startMinute);
        }
    }

    public static void deleteRoutine(String UID, FragmentActivity activity, String routineName, ArrayList<ArrayList<UserInfo>> routineList) {
        for (int i = 0; i < routineList.size(); i++) {
            ArrayList<UserInfo> currentRoutine = routineList.get(i);
            UserInfo userInfo = new UserInfo(routineName);
            if (userInfo.equals(currentRoutine.get(0))) {
                routineList.remove(i);
                Log.d(TAG, "Routine deleted.\n");
                writeListInPref(activity, routineList);

                UserOptions optionsObj = new UserOptions();
                optionsObj.dbDeleteRoutine(UID, activity, routineName);

                i = routineList.size();
            } else if (i == routineList.size() - 1) {
                System.out.println("Routine does not exist.\n");
                i = routineList.size();
            }
        }
    }

    public void addTask(String UID, FragmentActivity activity, String routineName, String taskName, int durationHours, int durationMinutes, ArrayList<ArrayList<UserInfo>> routineList) {
        String taskDuration = String.valueOf(durationHours * 60 + durationMinutes);

        for (int i = 0; i < routineList.size(); i++) {
            ArrayList<UserInfo> currentRoutine = routineList.get(i);
            userInfo = new UserInfo(routineName);

            if (userInfo.equals(currentRoutine.get(0))) {

                for (int k = 0; k < currentRoutine.size(); k++) {
                    userInfo = new UserInfo(taskName);
                    if (userInfo.equals(currentRoutine.get(k))) {
                        System.out.println("Task already exists.\n");
                        k = currentRoutine.size();
                        i = routineList.size();
                    } else if (k == currentRoutine.size() - 1) {
                        currentRoutine.add(new UserInfo(taskName, taskDuration));
                        routineList.set(i, currentRoutine);
                        Log.d(TAG, "Task added.\n");
                        k = currentRoutine.size();
                        i = routineList.size();
                        writeListInPref(activity, routineList);

                        UserOptions optionsObj = new UserOptions();
                        optionsObj.dbAddTask(UID, activity, routineName, taskName, durationHours, durationMinutes);
                    }

                }
            } else if (i == routineList.size() - 1) {
                System.out.println("Routine does not exist.\n");
            }
        }
    }


   /* public void deleteTask(String routineName, String taskName) {
        for (int i = 0; i < routineList.size(); i++) {
            ArrayList<UserInfo> currentRoutine = routineList.get(i);
            userInfo = new UserInfo(routineName);

            if (userInfo.equals(currentRoutine.get(0))) {
                for (int k = 0; k < currentRoutine.size(); k++) {
                    userInfo = new UserInfo(taskName);
                    if (userInfo.equals(currentRoutine.get(k))) {
                        currentRoutine.remove(currentRoutine.indexOf(currentRoutine.get(k)));
                        System.out.println("Task deleted.\n");
                        k = currentRoutine.size();
                        i = routineList.size();
                    } else if (k == currentRoutine.size() - 1) {
                        System.out.println("Task does not exist.\n");
                        k = currentRoutine.size();
                        i = routineList.size();
                    }
                }
            } else if (i == routineList.size() - 1) {
                System.out.println("Routine does not exist.\n");
            }
        }
    }

    public void editTask(String routineName, String taskName, String newTaskName, int newTaskDuration) {
        for (int i = 0; i < routineList.size(); i++) {
            ArrayList<UserInfo> currentRoutine = routineList.get(i);
            userInfo = new UserInfo(routineName);
            if (userInfo.equals(currentRoutine.get(0))) {
                for (int k = 1; k < currentRoutine.size(); k++) {
                    userInfo = new UserInfo(taskName);
                    if (userInfo.equals(currentRoutine.get(k))) {
                        currentRoutine.set(k, new UserInfo(newTaskName, newTaskDuration));
                        System.out.println("Task edited.\n");
                        k = currentRoutine.size();
                        i = routineList.size();
                    } else if (k == currentRoutine.size() - 1) {
                        System.out.println("Task does not exist.\n");
                        k = currentRoutine.size();
                        i = routineList.size();
                    }
                }
            } else if (i == routineList.size() - 1) {
                System.out.println("Routine does not exist.\n");
            }
        }
    } */

    public void editRoutine(String UID, FragmentActivity editRoutineFragment, String oldRoutineName, String newRoutineName, int newRoutineStartHour, int newRoutineStartMinutes, ArrayList<ArrayList<UserInfo>> routineList) {
        Log.d(TAG, "editRoutine being executed");
        Log.d(TAG, "newRoutineStartHour: " + newRoutineStartHour);
        Log.d(TAG, "newRoutineStartMinutes: " + newRoutineStartMinutes);

        for (int i = 0; i < routineList.size(); i++) {
            ArrayList<UserInfo> currentRoutine = routineList.get(i);
            Log.d(TAG, "TEST 1: " + currentRoutine.get(0).getTaskName());
            Log.d(TAG, "TEST 2: " + oldRoutineName);
            if (currentRoutine.get(0).getTaskName().equalsIgnoreCase(oldRoutineName)) {
                Time tme = new Time(newRoutineStartHour, newRoutineStartMinutes, 0);//seconds by default set to zero
                Format formatter = new SimpleDateFormat("h:mma");
                String startTime = formatter.format(tme);
                currentRoutine.set(0, new UserInfo(newRoutineName, startTime));
                routineList.set(i, currentRoutine);
                System.out.println("Routine edited.\n");
                i = routineList.size();

                writeListInPref(editRoutineFragment, routineList);
                routineList = readListFromPref(editRoutineFragment);
                Log.d(TAG, "updated routineList: " + routineList.toString());

                UserOptions optionsObj = new UserOptions();
                optionsObj.dbEditRoutine(UID, editRoutineFragment, oldRoutineName, newRoutineName, newRoutineStartHour, newRoutineStartMinutes);

            } else if (i == routineList.size() - 1) {
                System.out.println("Routine does not exist.\n");
                i = routineList.size();
            }
        }
    }

    //UI Changes

    public void displayHelloMessage(String UID, Activity activity, int viewID) {
        Log.d(TAG, "TEST UID: " + UID);
        mDocRef = db.collection("users").document(UID);
        mDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                value = documentSnapshot.getString("first name");
                TextView tv = activity.findViewById(viewID);
                /* TODO: Store this in local storage or else get error */
                tv.setText("hello Vanessa!");
            }
        });
    }

    public void displayTodaysMotivation(String UID, FragmentActivity activity, int textViewMyGoal, int editButton) {
        mDocRef = db.collection("users").document(UID).collection("other information").document("today's motivation");
        mDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String value1 = documentSnapshot.getString("goal");
                String value2 = documentSnapshot.getString("reason");
                TextView tv = activity.findViewById(textViewMyGoal);
                Button button = activity.findViewById(editButton);

                if (!value1.equals("") && !value2.equals("")) {
                    tv.setVisibility(tv.VISIBLE);
                    button.setText("edit today's motivation");
                    tv.setText(Html.fromHtml("<strong>" + "Today I want to " + "</strong>" + value1 + "<strong>" + ", and finishing my morning routine will " + "</strong>" + value2 + "."));
                } else {
                    button.setText("+ today's motivation");
                }
            }
        });
    }

    public void displayAll() {
        //UserInfo testUserInfo = (UserInfo) userInfo;
        //Log.d(TAG, "\n-----------\n");
        for (int i = 0; i < routineList.size(); i++) {
            ArrayList<UserInfo> currentRoutine = routineList.get(i);
            for (Object element : currentRoutine) {
                Log.d(TAG, "displayAll: " + element.toString());
            }
            //System.out.println("\n");
        }
    }

    public static void displayRoutineList(String uid, FragmentActivity homeFragment, ArrayList<ArrayList<UserInfo>> list) {
        int uniqueId;
        Button prevButton = null;
        Button nextButton = null;
        String routineButtonText = null;

        Typeface typeface = ResourcesCompat.getFont(homeFragment, R.font.poppins_regular);
        ConstraintLayout parentLayout = homeFragment.findViewById(R.id.frameLayout);
        ConstraintSet set = new ConstraintSet();
        set.clone(parentLayout);

        for (int i = 0; i < list.size(); i++) {
            ArrayList<UserInfo> currentRoutine = list.get(i);
            uniqueId = View.generateViewId();
            UserInfo routineInfo = currentRoutine.get(0);

            if (i == 0) {
                prevButton = new Button(new ContextThemeWrapper(homeFragment, R.style.NoShadowButton), null, 0);
                prevButton.setId(uniqueId);
                parentLayout.addView(prevButton);
                routineButtonText = routineInfo.getTaskName();
                prevButton.setText(routineButtonText);

                /*Styles*/
                prevButton.setTypeface(typeface);
                prevButton.setTextSize(11);
                prevButton.setAllCaps(false);
                prevButton.setGravity(Gravity.LEFT);
                prevButton.setPadding(40, 25, 40, 25);
                prevButton.setBackgroundResource(R.drawable.my_routines_button);
                set.constrainHeight(prevButton.getId(), 90);

                // connect start and end point of views, in this case top of child to top of parent.
                set.connect(prevButton.getId(), ConstraintSet.TOP, homeFragment.findViewById(R.id.textViewHeaderRoutine).getId(), ConstraintSet.BOTTOM, 30);
                set.connect(prevButton.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT, 80);
                set.connect(prevButton.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 80);
                set.applyTo(parentLayout);
                set.clone(parentLayout);
            } else {
                nextButton = new Button(new ContextThemeWrapper(homeFragment, R.style.NoShadowButton), null, 0);
                nextButton.setId(uniqueId);
                parentLayout.addView(nextButton);
                routineButtonText = routineInfo.getTaskName();
                nextButton.setText(routineButtonText);

                /*Styles*/
                nextButton.setTypeface(typeface);
                nextButton.setTextSize(11);
                nextButton.setAllCaps(false);
                nextButton.setGravity(Gravity.LEFT);
                nextButton.setPadding(40, 25, 40, 25);
                nextButton.setBackgroundResource(R.drawable.my_routines_button);
                set.constrainHeight(nextButton.getId(), 90);

                // connect start and end point of views, in this case top of child to top of parent.
                set.connect(nextButton.getId(), ConstraintSet.TOP, prevButton.getId(), ConstraintSet.BOTTOM, 20);
                set.connect(nextButton.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT, 80);
                set.connect(nextButton.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 80);
                set.applyTo(parentLayout);
                set.clone(parentLayout);

                prevButton = nextButton;
            }

            TextView routineStartTime = new TextView(homeFragment);
            routineStartTime.setId(View.generateViewId());
            parentLayout.addView(routineStartTime);

            String time = routineInfo.getTime();
            if (time.substring(0, 1).equals("0")) {
                time = time.substring(1);
            }
            routineStartTime.setText(time);

            /*Styles*/
            routineStartTime.setTypeface(typeface);
            routineStartTime.setTextSize(11);
            routineStartTime.setAllCaps(false);
            routineStartTime.setGravity(Gravity.RIGHT);
            routineStartTime.setPadding(40, 25, 40, 25);
            set.constrainHeight(routineStartTime.getId(), 90);


            String finalRoutineButtonText = routineButtonText;
            prevButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    routineDetailsFragment newFragment = new routineDetailsFragment();

                    FragmentManager fragmentManager = homeFragment.getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, newFragment.newInstance(finalRoutineButtonText, uid), null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null) // name can be null
                            .commit();
                }
            });

            // connect start and end point of views, in this case top of child to top of parent.
            set.connect(routineStartTime.getId(), ConstraintSet.TOP, prevButton.getId(), ConstraintSet.TOP, 0);
            set.connect(routineStartTime.getId(), ConstraintSet.RIGHT, prevButton.getId(), ConstraintSet.RIGHT, 0);
            set.applyTo(parentLayout);
            set.clone(parentLayout);
        }


        Button addRoutine = new Button(new ContextThemeWrapper(homeFragment, R.style.NoShadowButton), null, 0);
        addRoutine.setId(View.generateViewId());
        parentLayout.addView(addRoutine);
        addRoutine.setText("+ new routine");
        addRoutine.setTypeface(typeface);
        addRoutine.setTextSize(11);
        addRoutine.setAllCaps(false);
        addRoutine.setPadding(40, 25, 40, 25);
        addRoutine.setBackgroundResource(R.drawable.my_routines_add_button);
        set.constrainHeight(addRoutine.getId(), 100);


        if (prevButton != null) {
            set.connect(addRoutine.getId(), ConstraintSet.TOP, prevButton.getId(), ConstraintSet.BOTTOM, 20);
        } else {
            set.connect(addRoutine.getId(), ConstraintSet.TOP, homeFragment.findViewById(R.id.textViewHeaderRoutine).getId(), ConstraintSet.BOTTOM, 20);
        }

        parentLayout.setPadding(0, 0, 0, 100);
        set.connect(addRoutine.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT, 80);
        set.connect(addRoutine.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 80);
        set.constrainHeight(addRoutine.getId(), 90);
        set.applyTo(parentLayout);


        addRoutine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Is this the problem?? " + uid);

                FragmentManager fragmentManager = homeFragment.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, addRoutineFragment.newInstance(uid), null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit();
            }
        });

    }

    public static void displayEvents(String uid, FragmentActivity routineDetailsFragment, ArrayList<ArrayList<UserInfo>> list, String routineName) {
        int uniqueId;
        Button prevButton = null;
        Button nextButton = null;
        String eventButtonText = null;
        int clickedRoutinePosition = 0;

        Typeface typeface = ResourcesCompat.getFont(routineDetailsFragment, R.font.poppins_regular);
        ConstraintLayout parentLayout = routineDetailsFragment.findViewById(R.id.fragment_routine_details);
        ConstraintSet set = new ConstraintSet();
        set.clone(parentLayout);

        for (int i = 0; i < list.size(); i++) {
            ArrayList<UserInfo> currentRoutine = list.get(i);
            Log.d(TAG, "routineName: " + routineName);
            Log.d(TAG, "check routineName: " + currentRoutine.get(0).getTaskName());
            if (currentRoutine.get(0).getTaskName().equals(routineName)) {
                clickedRoutinePosition = i;
            }
        }

        if (clickedRoutinePosition == 0) {
            Log.d(TAG, "Error. clickedRoutinePosition not set");
        }

        ArrayList<UserInfo> clickedRoutine = list.get(clickedRoutinePosition);
        Log.d(TAG, "displayEvents currentRoutine: " + clickedRoutine.toString());

        for (int i = 1; i < clickedRoutine.size(); i++) {
            uniqueId = View.generateViewId();
            UserInfo eventInfo = clickedRoutine.get(i);

            if (i == 1) {
                prevButton = new Button(new ContextThemeWrapper(routineDetailsFragment, R.style.NoShadowButton), null, 0);
                prevButton.setId(uniqueId);
                parentLayout.addView(prevButton);
                eventButtonText = eventInfo.getTaskName();
                Log.d(TAG, "eventButtonText: " + eventButtonText);
                prevButton.setText(eventButtonText);

                /*Styles*/
                prevButton.setTypeface(typeface);
                prevButton.setTextSize(11);
                prevButton.setAllCaps(false);
                prevButton.setGravity(Gravity.LEFT);
                prevButton.setPadding(40, 25, 40, 25);
                prevButton.setBackgroundResource(R.drawable.my_routines_button);
                set.constrainHeight(prevButton.getId(), 90);

                // connect start and end point of views, in this case top of child to top of parent.
                set.connect(prevButton.getId(), ConstraintSet.TOP, routineDetailsFragment.findViewById(R.id.routineDetailsHeader).getId(), ConstraintSet.BOTTOM, 30);
                set.connect(prevButton.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT, 80);
                set.connect(prevButton.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 80);
                set.applyTo(parentLayout);
                set.clone(parentLayout);
            } else {
                nextButton = new Button(new ContextThemeWrapper(routineDetailsFragment, R.style.NoShadowButton), null, 0);
                nextButton.setId(uniqueId);
                parentLayout.addView(nextButton);
                eventButtonText = eventInfo.getTaskName();
                nextButton.setText(eventButtonText);

                /*Styles*/
                nextButton.setTypeface(typeface);
                nextButton.setTextSize(11);
                nextButton.setAllCaps(false);
                nextButton.setGravity(Gravity.LEFT);
                nextButton.setPadding(40, 25, 40, 25);
                nextButton.setBackgroundResource(R.drawable.my_routines_button);
                set.constrainHeight(nextButton.getId(), 90);

                // connect start and end point of views, in this case top of child to top of parent.
                set.connect(nextButton.getId(), ConstraintSet.TOP, prevButton.getId(), ConstraintSet.BOTTOM, 20);
                set.connect(nextButton.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT, 80);
                set.connect(nextButton.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 80);
                set.applyTo(parentLayout);
                set.clone(parentLayout);

                prevButton = nextButton;
            }

            TextView eventDuration = new TextView(routineDetailsFragment);
            eventDuration.setId(View.generateViewId());
            parentLayout.addView(eventDuration);

            String time = eventInfo.getTime();
            if (time.substring(0, 1).equals("0")) {
                time = time.substring(1);
            }

            if (Integer.valueOf(eventInfo.getTime()) < 60) {
                eventDuration.setText(eventInfo.getTime() + " min");
            } else if ((Integer.valueOf(eventInfo.getTime()) % 60) == 0) {
                String duration = (int) (Integer.valueOf(eventInfo.getTime()) / 60) + " hr";
                eventDuration.setText(duration);
            } else {
                String duration = (int) (Integer.valueOf(eventInfo.getTime()) / 60) + " hr " + (Integer.valueOf(eventInfo.getTime()) - 60 * (int) (Integer.valueOf(eventInfo.getTime()) / 60)) + "min";
                eventDuration.setText(duration);
            }

            /*Styles*/
            eventDuration.setTypeface(typeface);
            eventDuration.setTextSize(11);
            eventDuration.setAllCaps(false);
            eventDuration.setGravity(Gravity.RIGHT);
            eventDuration.setPadding(40, 25, 40, 25);
            set.constrainHeight(eventDuration.getId(), 90);


            String finalEventButtonText = eventButtonText;
            prevButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    routineDetailsFragment newFragment = new routineDetailsFragment();

                    FragmentManager fragmentManager = routineDetailsFragment.getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, newFragment.newInstance(finalEventButtonText, uid), null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null) // name can be null
                            .commit();
                }
            });

            // connect start and end point of views, in this case top of child to top of parent.
            set.connect(eventDuration.getId(), ConstraintSet.TOP, prevButton.getId(), ConstraintSet.TOP, 0);
            set.connect(eventDuration.getId(), ConstraintSet.RIGHT, prevButton.getId(), ConstraintSet.RIGHT, 0);
            set.applyTo(parentLayout);
            set.clone(parentLayout);
        }


        Button addTask = new Button(new ContextThemeWrapper(routineDetailsFragment, R.style.NoShadowButton), null, 0);
        addTask.setId(View.generateViewId());
        parentLayout.addView(addTask);
        addTask.setText("+ new task");
        addTask.setTypeface(typeface);
        addTask.setTextSize(11);
        addTask.setAllCaps(false);
        addTask.setPadding(40, 25, 40, 25);
        addTask.setBackgroundResource(R.drawable.my_routines_add_button);
        set.constrainHeight(addTask.getId(), 100);

        if (prevButton != null) {
            set.connect(addTask.getId(), ConstraintSet.TOP, prevButton.getId(), ConstraintSet.BOTTOM, 20);
        } else {
            set.connect(addTask.getId(), ConstraintSet.TOP, routineDetailsFragment.findViewById(R.id.routineDetailsHeader).getId(), ConstraintSet.BOTTOM, 20);
        }

        //set.connect(addTask.getId(), ConstraintSet.BOTTOM, parentLayout.getId(), ConstraintSet.BOTTOM, 100);
        parentLayout.setPadding(0, 0, 0, 100);
        set.connect(addTask.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT, 80);
        set.connect(addTask.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT, 80);
        set.constrainHeight(addTask.getId(), 90);
        set.applyTo(parentLayout);

        addTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Is this the problem?? " + uid);

                FragmentManager fragmentManager = routineDetailsFragment.getSupportFragmentManager();
                Log.d(TAG, "routineName= " + routineName);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, addTaskFragment.newInstance(uid, routineName), null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null) // name can be null
                        .commit();
            }
        });
    }

    public String getRoutineStartTime(String routineName, ArrayList<ArrayList<UserInfo>> routineList) {
        for (int i = 0; i < routineList.size(); i++) {
            ArrayList<UserInfo> currentRoutine = routineList.get(i);
            userInfo = new UserInfo(routineName);
            if (userInfo.equals(currentRoutine.get(0))) {
                Log.d(TAG, "executed");
                UserInfo routineInfo = currentRoutine.get(0);
                return routineInfo.getTime();
            } else if (i == routineList.size() - 1) {
                System.out.println("Routine does not exist.\n");
                i = routineList.size();
            }
        }

        return null;
    }

}