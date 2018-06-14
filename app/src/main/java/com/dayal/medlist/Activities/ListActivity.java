package com.dayal.medlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dayal.medlist.Data.DatabaseHandler;
import com.dayal.medlist.Model.Medicine;
import com.dayal.medlist.R;
import com.dayal.medlist.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Medicine>  medicineList;
    private List<Medicine> listIems;
    private DatabaseHandler db;
    private EditText medName, medQty;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getSupportActionBar().setTitle("Medicine List");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        db=new DatabaseHandler(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        db=new DatabaseHandler(this);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineList=new ArrayList<>();
        listIems=new ArrayList<>();
        //get from DB
        medicineList=db.getAllMeds();
        for(Medicine m:medicineList){
            Medicine medicine=new Medicine();
            medicine.setName(m.getName());
//            Log.w("list","id " + medicine.getId());
//            Log.w("list","Name " + medicine.getName());
            medicine.setQuantity("Qty: " + m.getQuantity());
            medicine.setId(m.getId());
            medicine.setDateAdded("Added on: " + m.getDateAdded());

            listIems.add(medicine);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(this,listIems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void createPopupDialog() {
        dialogBuilder=new AlertDialog.Builder(this);
        Button saveBtn;
        View view=getLayoutInflater().inflate(R.layout.popup,null);    // inflate popup dialog
        medName=(EditText)view.findViewById(R.id.edit_name);
        medQty=(EditText)view.findViewById(R.id.edit_qty);

        saveBtn=(Button)view.findViewById(R.id.save_btn);
        dialogBuilder.setView(view);
        dialog=dialogBuilder.create();
        dialog.show();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(!medName.getText().toString().isEmpty()&& !medQty.getText().toString().isEmpty())
                saveMedToDB(v);
            }
        });

    }

    private void saveMedToDB(View v) {
        Medicine newMed=new Medicine();
        String medicineName= medName.getText().toString();
        String medicineQty=medQty.getText().toString();
        newMed.setName(medicineName);
        newMed.setQuantity(medicineQty);

        db.addMed(newMed);
       // Log.i("item added",String.valueOf(db.getMedCount()));
        Snackbar.make(v,"Saved",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
//                finish();
//               startActivity(getIntent());                     // can be used to refresh activity
                setUpRecyclerView();
                dialog.dismiss();
            }
        },800);

    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();                 // to quit the app
    }
}
