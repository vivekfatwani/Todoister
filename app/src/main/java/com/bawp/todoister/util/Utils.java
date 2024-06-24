package com.bawp.todoister.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDate(Date date){
        // we use simple date format to format the date
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        // now we apply the date pattern that we want
        simpleDateFormat.applyPattern("EEE,MMM d");
        return simpleDateFormat.format(date);

    }
    public static void hideSoftKeyboard(View view) // code for hiding keyboard
    {
        InputMethodManager imm=(InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0); // called at bottom sheet fragment inside calender group
    }

// color coding for the row
    public static int priorityColor(Task task) {
        int color;
        if(task.getPriority()== Priority.HIGH)
        {
            color= Color.argb(200,201,21,23);
        }
        else if (task.getPriority()==Priority.MEDIUM)
        {
            color=Color.argb(200,155,179,0);
        }
        else
        {
            color=Color.argb(200,51,181,129);
        }
        return color;
    }
}
