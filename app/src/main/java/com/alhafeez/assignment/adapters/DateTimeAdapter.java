package com.alhafeez.assignment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alhafeez.assignment.ApptDialogFragment;
import com.alhafeez.assignment.R;

import java.util.ArrayList;


/**
 * Created by myMachine on 11/21/2016.
 */

public class DateTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;


    private int selected_position  = 0;

    private String apptId, home_response;
    private boolean isReschedule;
    public String DepartmentId="";
    public String departmentName="";
    public int index=-1;
    public String gender="";
    private boolean isReorder;
    private boolean homeSrvice;
    private ProgressBar progressBar;
    private ArrayList<String> list_timeslots = new ArrayList<>();
    private ApptDialogFragment fragment;

    public DateTimeAdapter(Context context,ArrayList<String> list_timeslots,ApptDialogFragment fragment){
        this.context= context;
        this.list_timeslots = list_timeslots;
        this.fragment = fragment;
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_time;
        public TimeViewHolder(View itemView) {
            super(itemView);
            txt_time= (TextView)itemView.findViewById(R.id.txt_time_slot_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_timeslot, parent, false);
        return new TimeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position){
        if (fragment.isThirdday){
            if (position==0||position==(list_timeslots.size()-1)){
                ((TimeViewHolder)holder).txt_time.setAlpha(0.5f);
                ((TimeViewHolder)holder).txt_time.setEnabled(false);
            }else {
                ((TimeViewHolder)holder).txt_time.setAlpha(1.0f);
                ((TimeViewHolder)holder).txt_time.setEnabled(true);
            }
        }else {
            ((TimeViewHolder)holder).txt_time.setAlpha(1.0f);
            ((TimeViewHolder)holder).txt_time.setEnabled(true);
        }
        if (position==fragment.selectedpos){
            ((TimeViewHolder)holder).txt_time.setBackgroundResource(R.drawable.timeslotbg_selected);
            ((TimeViewHolder)holder).txt_time.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }else {
            ((TimeViewHolder)holder).txt_time.setBackgroundResource(R.drawable.timeslotbg);
            ((TimeViewHolder)holder).txt_time.setTextColor(context.getResources().getColor(R.color.fontColor));
        }
        ((TimeViewHolder)holder).txt_time.setText(list_timeslots.get(position));

        ((TimeViewHolder)holder).txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.selectedpos = position;
                notifyDataSetChanged();
//                ((TimeViewHolder)holder).txt_time.setBackgroundColor(context.getResources().getColor(R.color.colorAppt));
            }
        });
    }


    @Override
    public int getItemCount() {
        if (list_timeslots.size()>0){
            return list_timeslots.size();
        }else {
            return 0;
        }

    }



}
