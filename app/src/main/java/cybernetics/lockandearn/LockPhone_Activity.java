package cybernetics.lockandearn;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;


//implementation 'com.github.jakob-grabner:Circle-Progress-View:v1.2.9' we need it to implement circleprogressview


public class LockPhone_Activity extends AppCompatActivity {

    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;
    int resume_flag = 0;

    Button btnLock, btnGoToMenu;
    Chronometer chronometer;
    TextView tvPoints;
    long CurrentPoints = 0;
    long time = 0;
    int ButtonStatus = 0;

    @Override
    public void onResume() {
        super.onResume();

        if(resume_flag==1 && ButtonStatus==1){
            System.out.println("inside onresume");
            time = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
            ButtonStatus = 0;
            long temp = time * -1;
            temp = temp / 60000;
            CurrentPoints += temp;
            tvPoints.setText(Long.toString(temp));

        }

        //Toast toast = Toast.makeText(getApplicationContext(), "Nothing", Toast.LENGTH_SHORT);
        //toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockphone);

        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        tvPoints = (TextView) findViewById(R.id.tvPoints);
        btnLock = (Button) findViewById(R.id.btnLock);

        btnGoToMenu = (Button) findViewById(R.id.btnGoToMenu);
        chronometer = (Chronometer) findViewById(R.id.chronometer);



        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ButtonStatus == 0) {
                    chronometer.setBase(SystemClock.elapsedRealtime() + time);
                    chronometer.start();
                    ButtonStatus = 1;
                    resume_flag=1;

                    boolean active = deviceManger.isAdminActive(compName);
                    if (active) {
                        deviceManger.lockNow();
                    }
                }
            }
        });



        btnGoToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Menu_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("current_value", CurrentPoints);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

    }



}

