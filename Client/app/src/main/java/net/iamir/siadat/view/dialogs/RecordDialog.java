package net.iamir.siadat.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.iamir.siadat.R;
import net.iamir.siadat.handel.libs.sqlite.DatabaseHandler;
import net.iamir.siadat.handel.models.Record;
import net.iamir.siadat.handel.services.api.ApiService;
import net.iamir.siadat.view.MainActivity;

public class RecordDialog extends AppCompatDialogFragment {
    private Record record;
    private EditText editTextName;
    private EditText editTextFamily;
    private EditText editTextLevel;
    private resetRecords resetRecords;

    private Context context;

    private ApiService apiService;

    private DatabaseHandler db;

    public RecordDialog(Context context, Record record, resetRecords resetRecords) {
        this.context = context;
        this.record = record;
        apiService = new ApiService(context);
        db = new DatabaseHandler(context);
        this.resetRecords = resetRecords;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.record_dialog, null);
        String title = record.getRecordId() != null ? "Edit " + record.getRecordId() : "Create";
        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        record.setRecordName(editTextName.getText().toString());
                        record.setRecordFamily(editTextFamily.getText().toString());
                        record.setRecordLevel(editTextLevel.getText().toString());
                        apiService.saveRecord(record, new Response.Listener<Record>() {
                            @Override
                            public void onResponse(Record response) {
                                Toast.makeText(context, "Saved in school.raya.pw", Toast.LENGTH_SHORT).show();
                                resetRecords.OnReset(record);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (MainActivity.hasInternetConnection(context)) {
                                    Toast.makeText(context, "Can't Save, Please Check Network...", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Saved in Local", Toast.LENGTH_SHORT).show();
                                    db.updateRecord(record, null);
                                    resetRecords.OnReset(record);
                                }
                            }
                        });
                    }
                });
        editTextName = view.findViewById(R.id.edit_name);
        editTextFamily = view.findViewById(R.id.edit_family);
        editTextLevel = view.findViewById(R.id.edit_level);
        if (record.getRecordName() != null) editTextName.setText(record.getRecordName());
        if (record.getRecordFamily() != null) editTextFamily.setText(record.getRecordFamily());
        if (record.getRecordLevel() != null) editTextLevel.setText(record.getRecordLevel());
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface resetRecords {
        void OnReset(Record record);
    }
}