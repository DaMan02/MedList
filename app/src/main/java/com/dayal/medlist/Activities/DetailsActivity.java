package com.dayal.medlist.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dayal.medlist.Data.DatabaseHandler;
import com.dayal.medlist.R;
import com.dayal.medlist.UI.RecyclerViewAdapter;

public class DetailsActivity extends AppCompatActivity {
    private TextView nameTxt;
    private TextView qtyTxt;
    private TextView datetxt;
    private Button editBtn;
    private Button deleteBtn;
    public int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        nameTxt=(TextView)findViewById(R.id.detail_name);
        qtyTxt=(TextView)findViewById(R.id.detail_qty);
        datetxt=(TextView)findViewById(R.id.detail_date);
        editBtn = (Button)findViewById(R.id.edit_btn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");

        editBtn.setVisibility(View.INVISIBLE);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            nameTxt.setText(bundle.getString("name"));
            qtyTxt.setText(bundle.getString("qty"));
            datetxt.setText(bundle.getString("date"));
            id=bundle.getInt("id");

            deleteBtn=(Button)findViewById(R.id.del_btn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(id);
                }
            });

        }


    }

    private void deleteItem(final int id) {
        AlertDialog.Builder alert=new AlertDialog.Builder(DetailsActivity.this);
        alert.setMessage("Delete this item ?");
        alert.setNegativeButton("Oops, no",null);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHandler db=new DatabaseHandler(getApplicationContext());
                db.deleteMed(id);
                //TODO:  start recyclerViewAdapter activity for result to notifydatasetchanged

                Toast.makeText(getApplicationContext(),"Item deleted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailsActivity.this,ListActivity.class));

            }
        });
        alert.show();
    }
}
