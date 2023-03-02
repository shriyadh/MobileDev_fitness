package edu.northeastern.team1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewChat  extends AppCompatDialogFragment {

    private EditText userName;
    private DgListener dglistener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder inputBox = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_recycler, null);
        inputBox.setView(view);
        inputBox.setTitle("New Chat");

        inputBox.setPositiveButton("Start Chat", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dg, int which) {
                if(userName == null ||
                        userName.getText().toString().equals("")                         ){

                    Toast.makeText(getContext(), "Fields cannot be empty. Please add name of user!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    String name = userName.getText().toString();

                    dglistener.applyTexts(name);

                }


            }
        });

        inputBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dg, int which) {
                // do nothing
            }
        });

        userName = view.findViewById(R.id.editTextTextPersonName);

        return inputBox.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dglistener = (DgListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement dgListener");
        }
    }

    public interface DgListener{
        void applyTexts(String user);
    }
}
