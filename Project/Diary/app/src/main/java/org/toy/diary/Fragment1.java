package org.toy.diary;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import lib.kingja.switchbutton.SwitchMultiButton;


public class Fragment1 extends Fragment {
    RecyclerView recyclerView;
    NoteAdapter adapter;

    Context context;
    onTabItemSelectedListener listener;
    @Override
    public void onDetach() {
        super.onDetach();
        if(context!=null){
            context =null;
            listener = null;
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof TabLayout.OnTabSelectedListener){
            listener = (onTabItemSelectedListener)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1,container,false);
        initUI(rootView);
        Log.d("Adapter","CreateFragment1");
        return rootView;
    }
    public void initUI(ViewGroup rootView){
        Button todayWriteButton = rootView.findViewById(R.id.todayWriteButton);
        todayWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTabSelected(0);
            }
        });

        SwitchMultiButton switchMultiButton = rootView.findViewById(R.id.switchButton);
        switchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                Toast.makeText(getContext(),tabText,Toast.LENGTH_SHORT).show();
                Log.d("Frag1","switch"+" "+position);
                adapter.switchLayout(position);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        adapter = new NoteAdapter();
        adapter.items.add(new Note(0,"0","진주시 ","","","하하","0","capture1.jpg","1월19일" ));
        adapter.addItem(new Note(1,"0","진주시 ","","","하하","0","capture1.jpg","1월19일" ));
        adapter.addItem(new Note(2,"0","진주시 ","","","하하","0","capture1.jpg","1월19일" ));

        Log.d("Adapter", String.valueOf(adapter.items.get(0)._id));
        recyclerView.setAdapter(adapter);

        Log.d("Adapter","SetAdapter");

        adapter.setOnItemClickListener(new OnNoteItemClickListener() {
            @Override
            public void onItemClick(NoteAdapter.ViewHolder holder, View view, int position) {
                Note item = adapter.getItem(position);
                Toast.makeText(getContext(),"selected",Toast.LENGTH_SHORT).show();
            }
        });

    }
}