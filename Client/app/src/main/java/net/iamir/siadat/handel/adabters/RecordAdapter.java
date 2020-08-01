package net.iamir.siadat.handel.adabters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.iamir.siadat.R;
import net.iamir.siadat.handel.models.Record;
import net.iamir.siadat.view.MainActivity;
import net.iamir.siadat.view.dialogs.RecordDialog;

import java.util.ArrayList;
import java.util.List;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private List<Record> records = new ArrayList<>();

    private RecordViewHolder.OnRecordItemClick onRecordItemClick;
    private RecordViewHolder.OnRecordItemClickDelete onRecordItemClickDelete;

    public RecordAdapter(RecordViewHolder.OnRecordItemClick onRecordItemClick, RecordViewHolder.OnRecordItemClickDelete onRecordItemClickDelete) {
        this.onRecordItemClick = onRecordItemClick;
        this.onRecordItemClickDelete = onRecordItemClickDelete;
    }

    public void addRecords(List<Record> Records) {
        this.records.addAll(Records);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.record, parent, false);
        RecordViewHolder vHolder = new RecordViewHolder(view, onRecordItemClick, onRecordItemClickDelete);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.bindRecord(records.get(position));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private TextView RecordId;
        private TextView RecordName;
        private TextView RecordFamily;
        private TextView RecordLevel;
        private Button btnEdit;
        private Button btnDelete;

        private OnRecordItemClick onRecordItemClick;
        private OnRecordItemClickDelete onRecordItemClickDelete;

        public RecordViewHolder(View itemView, OnRecordItemClick onRecordItemClick, OnRecordItemClickDelete onRecordItemClickDelete) {
            super(itemView);
            this.onRecordItemClick = onRecordItemClick;
            this.onRecordItemClickDelete = onRecordItemClickDelete;
            RecordId = itemView.findViewById(R.id.record_id);
            RecordName = itemView.findViewById(R.id.record_name);
            RecordFamily = itemView.findViewById(R.id.record_family);
            RecordLevel = itemView.findViewById(R.id.record_level);
            btnEdit = itemView.findViewById(R.id.record_edit);
            btnDelete = itemView.findViewById(R.id.record_delete);
        }

        @SuppressLint("NewApi")
        public void bindRecord(final Record record) {
            RecordId.setText(record.getRecordId());
            RecordName.setText(record.getRecordName());
            RecordFamily.setText(record.getRecordFamily());
            RecordLevel.setText(record.getRecordLevel());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecordItemClick.OnRecordClick(record);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecordItemClickDelete.OnRecordClick(record);
                }
            });
        }

        public interface OnRecordItemClick {
            void OnRecordClick(Record record);
        }

        public interface OnRecordItemClickDelete {
            void OnRecordClick(Record record);
        }
    }

    public void clear() {
        int size = records.size();
        records.clear();
        notifyItemRangeRemoved(0, size);
    }
}
