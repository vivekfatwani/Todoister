package com.bawp.todoister;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
private EditText enterTodo;
private ImageButton calenderButton;
private ImageButton priorityButton;
private RadioGroup priorityRadioGroup;
private RadioButton selectedRadioButton;
private int selectedButttonid;
private ImageView saveButton;
private CalendarView calendarView;
private Group calenderGroup;
private Date dueDate;
private Calendar calendar=Calendar.getInstance();
private SharedViewModel sharedViewModel;
private boolean isEdit;
private Priority priority;
// this gives us the instance of the calendar or gives a full calender
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view=   inflater.inflate(R.layout.bottom_sheet, container, false);
        calenderGroup=view.findViewById(R.id.calendar_group);
        calendarView=view.findViewById(R.id.calendar_view);
        calenderButton=view.findViewById(R.id.today_calendar_button);
        enterTodo=view.findViewById(R.id.enter_todo_et);
        saveButton=view.findViewById(R.id.save_todo_button);
        priorityButton=view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup=view.findViewById(R.id.radioGroup_priority);

        Chip todayChip=view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip=view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip=view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(sharedViewModel.getSelectedItem().getValue()!=null)
        {  isEdit= sharedViewModel.getIsEdit();
            Task task=sharedViewModel.getSelectedItem().getValue();
            enterTodo.setText(task.getTask());
            Log.d("my1", "onViewCreated: " + task.getTask());
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel=new ViewModelProvider(requireActivity()).get(SharedViewModel.class);



        // now we use calendar button for due date
        calenderButton.setOnClickListener(v -> {
          // here we change the visibility of calender group
          calenderGroup.setVisibility(calenderGroup.getVisibility()==View.GONE
                  ? View.VISIBLE : View.GONE);
            Utils.hideSoftKeyboard(v);
        });
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // here we get the date
           // Log.d("dueDate", "month "+(month+1)+"day"+dayOfMonth);
           calendar.clear(); // to give clean slate
            calendar.set(year,month,dayOfMonth);
            dueDate=calendar.getTime();
        });

        priorityButton.setOnClickListener(v -> {
          Utils.hideSoftKeyboard(v);
          priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility()==View.GONE
                  ? View.VISIBLE : View.GONE);
          priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
              if(priorityRadioGroup.getVisibility()==View.VISIBLE)
              {
                 selectedButttonid=checkedId;
                 selectedRadioButton=view.findViewById(selectedButttonid);
                 if(selectedRadioButton.getId() == R.id.radioButton_high)
                 {
                     priority=Priority.HIGH;
                 }
                 else if (selectedRadioButton.getId() == R.id.radioButton_med) {
                     priority=Priority.MEDIUM;
                 }
                 else if (selectedRadioButton.getId()==R.id.radioButton_low) {
                     priority=Priority.LOW;
                 }
                 else{
                     priority=Priority.LOW;
                 }

              }
              else{
                  priority=Priority.LOW;
              }

          });
        });


        saveButton.setOnClickListener(v -> {
            String task=enterTodo.getText().toString().trim();
            if(!TextUtils.isEmpty(task) && dueDate!=null && priority!=null)
            {
                Task mytask=new Task(task, priority, dueDate,
                        Calendar.getInstance().getTime(), false);

                //checking update condition i.e. if isEdit is true so that save button will behave as update button
                if(isEdit)
                {
                    Task updateTask=sharedViewModel.getSelectedItem().getValue();
                    Objects.requireNonNull(updateTask).setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);
                    TaskViewModel.update(updateTask);
                    sharedViewModel.setIsEdit(false);
                }
                else {
                    TaskViewModel.insert(mytask);
                }
                enterTodo.setText("");
                //dismissing the bottom sheet
                if(this.isVisible())
                {
                    this.dismiss();
                }
            }
            else
                {
                    Snackbar.make(saveButton,R.string.empty_field, BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                }


        });

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.today_chip)
        {
            calendar.add(Calendar.DAY_OF_YEAR,0);
            dueDate=calendar.getTime();
            Log.d("TIME1", "onClick: "+ dueDate.toString());
        }
        else if (id==R.id.tomorrow_chip)
        {
            calendar.add(Calendar.DAY_OF_YEAR,1);
            dueDate=calendar.getTime();
            Log.d("TIME1", "onClick: "+ dueDate.toString());
        }
        else if (id == R.id.next_week_chip)
        {
            calendar.add(Calendar.DAY_OF_YEAR,7);
            dueDate=calendar.getTime();
            Log.d("TIME1", "onClick: "+ dueDate.toString());
        }

    }
}