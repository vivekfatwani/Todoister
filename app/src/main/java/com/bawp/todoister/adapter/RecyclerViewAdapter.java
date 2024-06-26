package com.bawp.todoister.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatTextView$InspectionCompanion;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.R;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
   private final List<Task> taskList;
private final OnTodoClickListener todoClickListener;
    public RecyclerViewAdapter(List<Task> taskList,OnTodoClickListener onTodoClickListener) {
        this.taskList = taskList;
       this.todoClickListener = onTodoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // here we define new viewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      // here we bind data and views together
        Task task= taskList.get(position);//  we get  the task object
        // we have to convert back the date
        String formatted= Utils.formatDate(task.getDueDate());
        // adding colors to row acc. to priority
        ColorStateList colorStateList= new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        },new int[]{
                Color.LTGRAY, //disabled
                Utils.priorityColor(task)
        });


        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);

    }

    @Override
    public int getItemCount() {
        // here we count the number of data or what is the size of the list
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // here we access the widgets through viewHolder
        public RadioButton radioButton;
        public TextView task;
        public Chip todayChip;
        OnTodoClickListener onTodoClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.todo_radio_button);
            task=itemView.findViewById(R.id.todo_row_todo);
            todayChip=itemView.findViewById(R.id.todo_row_chip);
            this.onTodoClickListener=todoClickListener;
            itemView.setOnClickListener(this); // means view of this class is clicked
            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id=v.getId();
            Task currTask=taskList.get(getAdapterPosition());// getting curr task from the taskList at adapter position
            if(id == R.id.todo_row_layout)
            {

                onTodoClickListener.onTodoClick(currTask); // calling method
                // now we implement onTodoClickListener interface in mainActivity to acces the method
            }
            if(id== R.id.todo_radio_button)
            {
                onTodoClickListener.onRadioButtonClick(currTask);
            }

        }
    }
}
