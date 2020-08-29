package com.alhafeez.assignment;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alhafeez.assignment.adapters.DateTimeAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hrusikesh swain on 8/27/2020.
 * Be U Salons
 * hrusikeshswain@beusalons.com
 */
public class ApptDialogFragment extends DialogFragment {

    private CalendarView calendarView;
    private ImageView close_activity;
    private RecyclerView recycler_slot;
    private ArrayList<String> list_timeslots = new ArrayList<>();
    private ArrayList<String> list_timeslotsodd = new ArrayList<>();
    private List<String> third_dateslist;
    private DateTimeAdapter dateTimeAdapter;
    private LinearLayout linear_slots;
    private TextView txt_confirm;
    public boolean isThirdday = false;
    public int selectedpos = -1;
    public String selected_dt = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linear_= (LinearLayout) inflater.inflate(R.layout.layout_appt, container, false);
        initViews(linear_);
        calenderViews();
        getTodaydate();
        thirdDaylist();
        return linear_;
    }

    public void initViews(View linear_){
        dateTimeAdapter = new DateTimeAdapter(getActivity().getApplicationContext(),list_timeslots,this);
        calendarView = linear_.findViewById(R.id.calenderview);
        close_activity = linear_.findViewById(R.id.close_activity);
        linear_slots = linear_.findViewById(R.id.linear_slots);
        txt_confirm = linear_.findViewById(R.id.textView);
        recycler_slot = linear_.findViewById(R.id.recycler_slot);
        GridLayoutManager layoutManager= new GridLayoutManager(getActivity(), 4);
        recycler_slot.setHasFixedSize(true);
        recycler_slot.setLayoutManager(layoutManager);
        recycler_slot.setAdapter(dateTimeAdapter);
        close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedpos!=-1&& !selected_dt.equals("")){
                    showToast("Appointment booked on "+ selected_dt+" at "+list_timeslots.get(selectedpos));
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    };
                    new Handler().postDelayed(runnable,300);

                }else {
                    showToast("Please select date and time to book an appointment");
                }
            }
        });
    }

    public void calenderViews(){
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                selectedpos = -1;

                Calendar c=Calendar.getInstance();
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);


                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String selectedDate = sdf.format(c.getTime());
//                String Date = dayOfMonth + "/" + (month + 1) + "/" + year;
               if (third_dateslist.contains(selectedDate)){
                   isThirdday = true;
               }else {
                   isThirdday = false;
               }
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                System.out.println("select dayOfWeek => " + dayOfWeek);
                if (dayOfWeek==1){
                    showToast("No timeslot available");
                    list_timeslotsodd.clear();
                    list_timeslots.clear();
                    dateTimeAdapter.notifyDataSetChanged();
                    linear_slots.setVisibility(View.INVISIBLE);
                }else {
                    linear_slots.setVisibility(View.VISIBLE);
                    selected_dt = selectedDate;
                    if (dayOfMonth%2==0){
                        System.out.println("even date =>");
                        list_timeslots.clear();
                        getTimeslots("09:00","14:30",true);
                        dateTimeAdapter.notifyDataSetChanged();
                    }else {
                        System.out.println("odd date =>");
                        list_timeslotsodd.clear();
                        list_timeslots.clear();
                        getTimeslots("10:00","19:00",false);
                        updateTimeslot();
                        dateTimeAdapter.notifyDataSetChanged();
                    }
                }


            }
        });

        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        calendarView.setMaxDate(calendar.getTimeInMillis());
    }

    public void updateTimeslot(){
      for (int j=0;j<list_timeslotsodd.size();j++){
          if (j%2==0){
              list_timeslots.add(list_timeslotsodd.get(j));
          }
      }
    }

    public void getTodaydate(){
        Calendar cd  = Calendar.getInstance();
        Date c = cd.getTime();
        if (cd.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            System.out.println("Sunday time => " + c);
            showToast("No timeslot available");
            list_timeslotsodd.clear();
            list_timeslots.clear();
            dateTimeAdapter.notifyDataSetChanged();
            linear_slots.setVisibility(View.INVISIBLE);
        }else {
            linear_slots.setVisibility(View.VISIBLE);
            SimpleDateFormat df_selectted = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate_selected = df_selectted.format(c);
            selected_dt = formattedDate_selected;
            System.out.println("Other day time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd", Locale.getDefault());
            String formattedDate = df.format(c);
            System.out.println("formattedDate date => " + formattedDate);
            if (Integer.parseInt(formattedDate)%2==0){
                System.out.println("even date =>");
                list_timeslots.clear();
                getTimeslots("09:00","14:30",true);
                dateTimeAdapter.notifyDataSetChanged();
            }else {
                System.out.println("odd date =>");
                list_timeslotsodd.clear();
                list_timeslots.clear();
                getTimeslots("10:00","19:00",false);
                updateTimeslot();
                dateTimeAdapter.notifyDataSetChanged();
            }
        }


    }

    private void thirdDaylist(){
        Calendar cd  = Calendar.getInstance();
        Date c = cd.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        cd.add(Calendar.DAY_OF_YEAR,30);
        Date resultdate = cd.getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate1 = df1.format(resultdate);

        List<Date> dates = getDates(formattedDate, formattedDate1);
        third_dateslist = new ArrayList<>();
        for (int k=0;k<dates.size();k++){
            int j = k+1;
            if (j%3==0){
                third_dateslist.add(df1.format(dates.get(j)));
                System.out.println("third day list"+df1.format(dates.get(j)));
            }

        }
    }

    private void showToast(String msg){
        Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
    }

    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

            /*val window = dialog!!.window
            window!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTimeslots(String starttime,String endtime,boolean iseven){

        try{
            String string1 = starttime;
            Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);

            String string2 = endtime;
            Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            DateFormat df = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            int startDate = cal.get(Calendar.DATE);

            while (cal.get(Calendar.DATE) == startDate) {
                if (iseven){
                    cal.add(Calendar.MINUTE, 30);
                }else {
                    cal.add(Calendar.MINUTE, 30);
                }

//                System.out.println(df.format(cal.getTime()));
                Randomchk(df.format(cal.getTime()),calendar1,calendar2,iseven);
            }
        }catch (Exception e){
            System.out.println("exceptiopn");
        }

    }



    public void Randomchk(String str,Calendar calendar1,Calendar calendar2,boolean iseven){
        Date d = null;
        try {
            d = new SimpleDateFormat("HH:mm").parse(str);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);
            DateFormat df = new SimpleDateFormat("HH:mm");
            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                System.out.println(df.format(calendar3.getTime()));
                if (iseven){
                    list_timeslots.add(df.format(calendar3.getTime()));
                }else {
                    list_timeslotsodd.add(df.format(calendar3.getTime()));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
