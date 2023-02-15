package edu.northeastern.team1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DisplayShowInformation extends AppCompatDialogFragment {
    private TextView title;
    private TextView description;
    private TextView year;
    private TextView rating;
    private ImageView img;

    private DgListener dglistener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder inputBox = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.display_show_info, null);
        inputBox.setView(view);
        inputBox.setTitle("Show Information");
        inputBox.setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dg, int which) {
                // do nothing
            }
        });

       /* title = view.findViewById(R.id.);
        description = view.findViewById(R.id.);
        rating = view.findViewById(R.id.);
        year = view.findViewById(R.id.);
        img = view.findViewById(R.id.);


*/
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

    public interface DgListener {
            void applyTexts(String urlName, String url);
        }

}