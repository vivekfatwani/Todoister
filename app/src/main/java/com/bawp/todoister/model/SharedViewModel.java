package com.bawp.todoister.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
public boolean isEdit;
    public MutableLiveData<Task> selectedItem = new MutableLiveData<>();
     // setting up getters and setters
    public void selectedItem(Task task)
    {
        selectedItem.setValue(task);
    }
    public LiveData<Task> getSelectedItem()
    {
        return selectedItem;
    }

    public void setIsEdit(boolean isEdit)
    {
        this.isEdit=isEdit;
    }
    public boolean getIsEdit()
    {
        return isEdit;
    }
}
