package com.example.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.alarmapp.DbOpenHelper.TIME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int hour = 0;
    private int minute = 0;
    private int[] time = new int[2];
    private ListViewAdapter listViewAdapter;
    private ItemRemoveDialogFragment itemRemoveDialogFragment;

    private DbOpenHelper dbOpenHelper;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public AlarmFragment() {
        // Required empty public constructor
    }

    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

        final View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);

        listViewAdapter = new ListViewAdapter();
        final ListView listView = (ListView) rootView.findViewById(R.id.listview1);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                itemRemoveDialogFragment = new ItemRemoveDialogFragment();
                itemRemoveDialogFragment.show(getActivity().getSupportFragmentManager(), "tag");
                getFragmentManager().executePendingTransactions();
                itemRemoveDialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(itemRemoveDialogFragment.getReply() == true){
                            Log.d("시간", listViewAdapter.getRawTime(position) + "");
                            Log.d("시간2", dbOpenHelper.getTime(listViewAdapter.getRawTime(position)) + "");

                            listViewAdapter.removeData(position);
                            if(dbOpenHelper.getIdFromTime(listViewAdapter.getRawTime(position)) != -1){
                                dbOpenHelper.deleteColumn(dbOpenHelper.getIdFromTime(listViewAdapter.getRawTime(position)));
                            }
                            deleteAlarm(listViewAdapter.getRawTime(position));
                        }
                        itemRemoveDialogFragment.setReply(false);
                    }
                });
            }
        });

        Button newAlarmButton = (Button)rootView.findViewById(R.id.newAlarm);
        newAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forTimeSettingIntent = new Intent(getContext(), TimeSettingActivity.class);
                getActivity().startActivityForResult(forTimeSettingIntent, 0);
            }
        });

        dbOpenHelper = new DbOpenHelper(getContext());
        setListOnCreate(dbOpenHelper);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        dbOpenHelper.close();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        hour = args.getInt("time hour");
        minute = args.getInt("time minute");
        time[0] = hour;
        time[1] = minute;

        int rawTime = hour * 100 + minute;

        if(dbOpenHelper.getIdFromTime(rawTime) == -1){
            dbOpenHelper.insertColumn(rawTime);
            listViewAdapter.addItem(time);
            listViewAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getActivity(), "이미 있음", Toast.LENGTH_SHORT).show();
        }
    }

    public void setListOnCreate(DbOpenHelper dbOpenHelper){
        dbOpenHelper.open();
        dbOpenHelper.create();

        Cursor cursor = dbOpenHelper.selectColumns();
        try{
            while(cursor.moveToNext()){
                int rawTime = cursor.getInt(cursor.getColumnIndex(TIME));
                time[0] = rawTime / 100;
                time[1] = rawTime % 100;
                listViewAdapter.addItem(time);
                listViewAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void deleteAlarm(int rawTime){
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), rawTime, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d("akdjf", "알람 제거됨");
    }
}