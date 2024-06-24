package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.TaskRoomDatabase;

import java.util.List;

public class DoisterRepository {
    public final TaskDao taskDao;
    public final LiveData<List<Task>> allTasks;

    public DoisterRepository(Application application) {
        // getting instance of room database
        TaskRoomDatabase database= TaskRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getTasks();
    }
    // creating methods which can be used anywhere we want
    // getting all tasks
    public LiveData<List<Task>> getAllTasks()
    {
        return allTasks;
    }
    // insert
    public void insert(Task task)
    {
        // we use executor service or database writer executor so that all write operations are
        // are done in the background thread
        TaskRoomDatabase.databaseWriterExecutor.execute(()->taskDao.insertTask(task));
    }
    public LiveData<Task> get(long id)
    {
        return taskDao.get(id);
    }
    public void delete(Task task)
    {
        TaskRoomDatabase.databaseWriterExecutor.execute(()->taskDao.delete(task));
    }
    public void update(Task task)
    {
        TaskRoomDatabase.databaseWriterExecutor.execute(()->taskDao.update(task));
    }

}
