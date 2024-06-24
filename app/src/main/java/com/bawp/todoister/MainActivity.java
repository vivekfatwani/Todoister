package com.bawp.todoister;

import android.content.Intent;
import android.os.Bundle;

import com.bawp.todoister.adapter.OnTodoClickListener;
import com.bawp.todoister.adapter.RecyclerViewAdapter;
import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private TaskViewModel taskViewModel;
    private static final String TAG="ITEM";
    private int counter;
    BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        counter=0;
        bottomSheetFragment=new BottomSheetFragment();
        ConstraintLayout constraintLayout=findViewById(R.id.bottomSheet);
        // now we adding bottom sheet behaviour
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior=BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);
        // initializing recyclerView
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));// layout manager is responsible of laying everything out
        // now we set up our recycler view adapter inside getAllTasks method

        taskViewModel=new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication())
                .create(TaskViewModel.class);

        //shared view model
        sharedViewModel=new ViewModelProvider(this).get(SharedViewModel.class);
        // retrieving data from the table we use taskViewModel and since it is live data we use observe
        taskViewModel.getAllTasks().observe(this, tasks -> {
           // setting up our recycler view adapter
            recyclerViewAdapter=new RecyclerViewAdapter(tasks,this); // this meaning is entire class implements this interface
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
           // Task task= new Task("Task"+counter++, Priority.HIGH, Calendar.getInstance().getTime(),
             //       Calendar.getInstance().getTime(),false);
            // TaskViewModel.insert(task);
            ShowBottomSheetDialog();
        });
    }

    private void ShowBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(),bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AboutActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(Task task) {
        //Log.d("Click1", "onTodoClick: " + adapterPosition);
        sharedViewModel.selectedItem(task);
        sharedViewModel.setIsEdit(true);
        ShowBottomSheetDialog();
    }

    @Override
    public void onRadioButtonClick(Task task) {

        TaskViewModel.delete(task);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}