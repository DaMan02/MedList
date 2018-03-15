package com.dayal.medlist.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dayal.medlist.Activities.DetailsActivity;
import com.dayal.medlist.Data.DatabaseHandler;
import com.dayal.medlist.Model.Medicine;
import com.dayal.medlist.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<Medicine> medicineItems;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;                // inflater for confirmation dialog
    private AlertDialog dialog;

    public RecyclerViewAdapter(Context context, List<Medicine> medicineItems) {
        this.context = context;
        this.medicineItems = medicineItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
             Medicine medicine=medicineItems.get(position);
              holder.medName.setText(medicine.getName());
              holder.qty.setText(medicine.getQuantity());
              holder.dateAdded.setText(medicine.getDateAdded());
    }

    @Override
    public int getItemCount() {
          return medicineItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView medName;
        public TextView qty;
        public TextView dateAdded;
        public Button editBtn;
        public Button deleteBtn;
        public int id;
        public ViewHolder(View view,Context cntxt) {
            super(view);
            context =cntxt;

            medName=(TextView)view.findViewById(R.id.med_name);
            qty=(TextView)view.findViewById(R.id.med_qty);
            dateAdded=(TextView)view.findViewById(R.id.date_added);
            editBtn=(Button)view.findViewById(R.id.edit_btn);
            deleteBtn=(Button)view.findViewById(R.id.delete_btn);
            editBtn.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // go to details activity
                    int position=getAdapterPosition();
                    Medicine medicine=medicineItems.get(position);  //get med from list of meds at pos <>
                    Intent intent=new Intent(context, DetailsActivity.class);
                    intent.putExtra("name",medicine.getName());
                    intent.putExtra("qty",medicine.getQuantity());
                    intent.putExtra("date",medicine.getDateAdded());
                    intent.putExtra("id",medicine.getId());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {
           switch(v.getId()){
               case R.id.edit_btn:
                   Medicine medicine=medicineItems.get(getAdapterPosition());
                   editItem(medicine);
                   Log.w("log","to edit " + String.valueOf(medicine.getId()));
                   break;
               case R.id.delete_btn:
                     Medicine med=medicineItems.get(getAdapterPosition());
                      deleteItem(med.getId());
                  // Log.w("log","to delete " + String.valueOf(med.getId()));
                   break;
           }
        }

        public void deleteItem(final int id) {

            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.conformation_dialog,null);
            Button yesBtn=(Button)view.findViewById(R.id.yes_btn);
            Button noBtn=(Button)view.findViewById(R.id.no_btn);
            builder.setView(view);
            dialog=builder.create();
            dialog.show();
            Log.w("log","delete() id= " + String.valueOf(id));
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db=new DatabaseHandler(context);
                    db.deleteMed(id);
                    medicineItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });
        }

        public void editItem(final Medicine medicine){
            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.popup,null);
            TextView txt=(TextView)view.findViewById(R.id.enter_med_name);
            txt.setText("Edit");
            final EditText editText1=(EditText)view.findViewById(R.id.edit_name);
            final EditText editText2=(EditText)view.findViewById(R.id.edit_qty);
            Button saveBtn=(Button)view.findViewById(R.id.save_btn);

            builder.setView(view);
            dialog=builder.create();
            dialog.show();
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                         DatabaseHandler db =new DatabaseHandler(context);
                          medicine.setName(editText1.getText().toString());
                          medicine.setQuantity(editText2.getText().toString());
                     if(!editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()){
                         db.updateMed(medicine);
                         notifyItemChanged(getAdapterPosition(),medicine);
                         dialog.dismiss();
                     }
                     else {
                         // TODO
                     }
                }
            });

        }

        
    }

}
