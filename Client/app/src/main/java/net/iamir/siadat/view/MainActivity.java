package net.iamir.siadat.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.iamir.siadat.R;
import net.iamir.siadat.handel.adabters.RecordAdapter;
import net.iamir.siadat.handel.libs.Ping;
import net.iamir.siadat.handel.libs.loadmore.InfiniteScrollProvider;
import net.iamir.siadat.handel.libs.loadmore.OnLoadMoreListener;
import net.iamir.siadat.handel.libs.sqlite.DatabaseHandler;
import net.iamir.siadat.handel.models.Record;
import net.iamir.siadat.handel.services.api.ApiService;
import net.iamir.siadat.view.dialogs.RecordDialog;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        Response.Listener<List<Record>>,
        Response.ErrorListener {

    //private List<Record> records = new ArrayList<>();

    private RecordAdapter recordAdapter;

    private ApiService apiService;

    private DatabaseHandler db;

    private RecyclerView recyclerView;

    // private BubbleSeekBar spaceProgress;
    private ProgressBar primaryProgressBar;
    private ProgressBar footerProgressBar;

    private Button btnCreate;
    private Button btnReload;
    private Button btnSearch;

    private EditText editTextSearch;

    private int page = 1;

    private RecordAdapter.RecordViewHolder.OnRecordItemClick onRecordItemClick = new RecordAdapter.RecordViewHolder.OnRecordItemClick() {
        @Override
        public void OnRecordClick(Record record) {
            openDialog(record);
        }
    };

    private RecordAdapter.RecordViewHolder.OnRecordItemClickDelete onRecordItemClickDelete = new RecordAdapter.RecordViewHolder.OnRecordItemClickDelete() {
        @Override
        public void OnRecordClick(final Record record) {
            if (record.getRecordId() == null) {
                db.deleteRecord(record);
                reset();
            } else
                apiService.delRecord(record, new Response.Listener<Record>() {
                    @Override
                    public void onResponse(Record response) {
                        Toast.makeText(MainActivity.this, "Deleted " + response.getRecordId() + " in school.raya.pw", Toast.LENGTH_SHORT).show();
                        db.deleteRecord(record);
                        reset();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (Ping.check())
                            Toast.makeText(MainActivity.this, "Can't Delete, Please Check Network...", Toast.LENGTH_SHORT).show();
                        else{
                            db.updateRecord(record, "delete");
                            reset();
                        }
                    }
                });
        }
    };

    private RecordDialog.resetRecords resetRecords = new RecordDialog.resetRecords() {
        @Override
        public void OnReset(Record record) {
            reset();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setupView();
        if (Ping.check()) {
            this.apiService();
            this.check();
            Toast.makeText(getApplicationContext(), "Have Connection !", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_LONG).show();
            recordAdapter.addRecords(db.getAllRecords(true));
        }
    }

    @SuppressLint({"WrongConstant", "WrongViewCast"})
    private void setupView() {
        // Set RecyclreViews
        recyclerView = (RecyclerView) findViewById(R.id.rv_records);
        // Set ProgressBars
        primaryProgressBar = (ProgressBar) findViewById(R.id.progressBar_mainactivity_main);
        footerProgressBar = (ProgressBar) findViewById(R.id.progressBar_mainactivity_footer);
        // Config RecyclreView for Contents
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        // Set Contents Adapter
        recordAdapter = new RecordAdapter(onRecordItemClick, onRecordItemClickDelete);
        // Set Content Adapter in Contents RecyclreView
        recyclerView.setAdapter(recordAdapter);

        btnCreate = (Button) findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record $record = new Record();
                openDialog($record);
            }
        });
        btnReload = (Button) findViewById(R.id.btn_reload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                reset();
            }
        });

        btnSearch = (Button) findViewById(R.id.btn_search);
        editTextSearch = (EditText) findViewById(R.id.edit_text_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAdapter.clear();
                page = 1;
                if (Ping.check()){
                    apiService();
                }else {
                    recordAdapter.addRecords(db.getAllRecordsQuery(editTextSearch.getText().toString()));
                }
            }
        });

        InfiniteScrollProvider infiniteScrollProvider = new InfiniteScrollProvider();
        infiniteScrollProvider.attach(recyclerView, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                footerProgressBar.setVisibility(View.VISIBLE);
                apiService();
            }
        });
        apiService = new ApiService(this);
        db = new DatabaseHandler(getApplicationContext());
    }

    private void apiService() {
        apiService.getRecords(page, editTextSearch.getText().toString(), MainActivity.this, MainActivity.this);
    }

    private Record $last;

    private void check() {
        final Integer count = db.getAllRecords(false).size();
        $last = count > 0? db.getAllRecords(false).get(count - 1): new Record();
        if (Ping.check()) {
            for (final Record record : db.getAllRecords(false)) {
                if (record.getRecordStatus().equals("delete")) {
                    apiService.delRecord(record, new Response.Listener<Record>() {
                        @Override
                        public void onResponse(Record response) {
                            db.deleteRecord(record);
                            if ($last.getRecordId().equals(record.getRecordId())) apiService();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            db.updateRecord(record, "saved");
                            if ($last.getRecordId().equals(record.getRecordId())) apiService();
                        }
                    });
                } else
                    apiService.saveRecord(record, new Response.Listener<Record>() {
                        @Override
                        public void onResponse(Record response) {
                            record.setRecordId(response.getRecordId());
                            db.updateRecord(record, "saved");
                            if ($last.getRecordId().equals(record.getRecordId())) apiService();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            db.deleteRecord(record);
                            if ($last.getRecordId().equals(record.getRecordId())) apiService();
                        }
                    });
            }
        }
    }

    private void reset() {
        recordAdapter.clear();
        page = 1;
        if (Ping.check()) this.apiService();
        else recordAdapter.addRecords(db.getAllRecords(true));
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        primaryProgressBar.setVisibility(View.GONE);
        footerProgressBar.setVisibility(View.GONE);
        Toast.makeText(MainActivity.this, "Error in receiving information", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(List<Record> response) {
        primaryProgressBar.setVisibility(View.GONE);
        footerProgressBar.setVisibility(View.GONE);
        recordAdapter.addRecords(response);
        for (final Record record : response) {
            db.updateRecord(record, "saved");
        }
        page += 1;

    }

    public void openDialog(Record record) {
        RecordDialog recordDialog = new RecordDialog(MainActivity.this, record, resetRecords);
        recordDialog.show(getSupportFragmentManager(), "record dialog");
    }
}