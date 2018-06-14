package com.dayal.medlist.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dayal.medlist.Data.DatabaseHandler;
import com.dayal.medlist.Model.Medicine;
import com.dayal.medlist.R;

public class MainActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private EditText medName, medQty;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab;
        db=new DatabaseHandler(this);
        byPassActivity();                                    // skip adding item every time
        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
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
                if(!medName.getText().toString().isEmpty()&& !medQty.getText().toString().isEmpty())
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
      //  Log.i("item added",String.valueOf(db.getMedCount()));
        Snackbar.make(v,"Saved",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
              startActivity(new Intent(MainActivity.this,ListActivity.class));
                dialog.dismiss();
            }
        },800);

    }

    public void byPassActivity(){
        if(db.getMedCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();

       }
    }
}
