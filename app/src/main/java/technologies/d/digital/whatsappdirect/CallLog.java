package technologies.d.digital.whatsappdirect;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wickerlabs.logmanager.LogObject;
import com.wickerlabs.logmanager.LogsManager;

import java.util.Collections;
import java.util.List;

public class CallLog extends AppCompatActivity {


    private static final int READ_LOGS = 725;
    private ListView logList;
    private Runnable logsRunnable;
    private String[] requiredPermissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);

        logList = (ListView) findViewById(R.id.LogsList);

        logsRunnable = new Runnable() {
            @Override
            public void run() {
                loadLogs();
            }
        };




        // Checking for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionToExecute(requiredPermissions, READ_LOGS, logsRunnable);
        } else {
            logsRunnable.run();
        }

    }




    // This is to be run only when READ_CONTACTS and READ_CALL_LOG permission are granted
    private void loadLogs() {
        LogsManager logsManager = new LogsManager(this);
        final List<LogObject> callLogs = logsManager.getLogs(LogsManager.ALL_CALLS);
        Collections.reverse(callLogs);

        final LogsAdapter logsAdapter = new LogsAdapter(this, R.layout.log_layout, callLogs);
        logList.setAdapter(logsAdapter);
        logList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             String mynum13 = callLogs.get(i).getNumber();
                mynum13.replace("-","");
                String mynum10 = mynum13.substring(3);

                Intent intent2 = new Intent(CallLog.this , MainActivity.class);
                intent2.putExtra("mynum10",mynum10);
                startActivity(intent2);





            }});
    }

    // A method to check if a permission is granted then execute tasks depending on that particular permission
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionToExecute(String permissions[], int requestCode, Runnable runnable) {

        boolean logs = ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED;
        boolean contacts = ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED;

        if (logs || contacts) {
            requestPermissions(permissions, requestCode);
        } else {
            runnable.run();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_LOGS && permissions[0].equals(Manifest.permission.READ_CALL_LOG) && permissions[1].equals(Manifest.permission.READ_CONTACTS)) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED && grantResults[1] == PermissionChecker.PERMISSION_GRANTED) {
                logsRunnable.run();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("The app needs these permissions to work, Exit?")
                        .setTitle("Permission Denied")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkPermissionToExecute(requiredPermissions, READ_LOGS, logsRunnable);
                            }
                        })
                        .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        }
    }






    }

