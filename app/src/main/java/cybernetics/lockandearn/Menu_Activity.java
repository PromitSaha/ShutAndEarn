package cybernetics.lockandearn;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static cybernetics.lockandearn.Login_Activity.EMAIL_SHARED_PREF;
import static cybernetics.lockandearn.Login_Activity.LOGGEDIN_SHARED_PREF;

/**
 * Created by Promit on 1/27/2018.
 */

public class Menu_Activity extends AppCompatActivity {

    Button bLogout;
    long TotalPoints = 0;
    long updatepoints=70;
    long l = 0;
    TextView tvTotalPoint;
    CardView cvLockYourPhone, cvRedeemPoints, cvAccountSettings;


    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;
    String email;
    public static final String url="http://murssaleentravels.com/locknearn/points.php";
    public static final String urll="http://murssaleentravels.com/locknearn/updatepoints.php";
    public static final String SHARED_PREF_NAME="tech";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_);

        deviceManger = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        bLogout = (Button) findViewById(R.id.bLogout);
        cvLockYourPhone = (CardView) findViewById(R.id.cvLockYourPhone);
        cvRedeemPoints = (CardView) findViewById(R.id.cvRedeemPoints);
        cvAccountSettings = (CardView) findViewById(R.id.cvAccountSettings);
        tvTotalPoint = (TextView) findViewById(R.id.tvTotalPoint);

        SharedPreferences set = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        email=set.getString(EMAIL_SHARED_PREF,"wht");

        cvLockYourPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(deviceManger != null &&deviceManger.isAdminActive(compName)) {
                    Intent intent2 = new Intent(getApplicationContext(), LockPhone_Activity.class);
                    startActivity(intent2);
                }else{
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why this needs to be added.");
                    startActivityForResult(intent, RESULT_ENABLE);
                }

                //Code for Disabling device administrator
                //deviceManger.removeActiveAdmin(compName);
                //updateButtonStates();


            }
        });

        cvRedeemPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("totalPoints", TotalPoints);
                i.putExtras(bundle);
                startActivity(i);
                //startActivity(new Intent(getApplicationContext(),ReadActivity.class).putExtra("totalPoint",TotalPoints));
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences settings = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove(LOGGEDIN_SHARED_PREF);
                editor.remove(EMAIL_SHARED_PREF);

                editor.commit();
                finish();*/

                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //showpoints();
        TotalPoints = 40;
        tvTotalPoint.setText(Long.toString(TotalPoints));

        Intent intent = getIntent();
        if(intent.hasExtra("current_value")){
            Bundle bundle = getIntent().getExtras();
            l = bundle.getLong("current_value");

            tvTotalPoint.setText(Long.toString(TotalPoints+l));
        }
        TotalPoints+=l;


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void ab(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonarray = jsonobject.getJSONArray("person");
                    JSONObject data = jsonarray.getJSONObject(0);

                    TotalPoints = data.getInt("totalpoints");
                    // String  TotalPoint = data.getString("totalpoints");

                    Toast.makeText(Menu_Activity.this, "value is "+TotalPoints, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    //Toast.makeText(Menu_Activity.this, email,Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Menu_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();
                parameters.put("email",email);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void setUpdatepoints(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")) {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Menu_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();
                parameters.put("updatepoints", String.valueOf(updatepoints));
                parameters.put("email",email);

                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);





    }
    private void showpoints(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonarray = jsonobject.getJSONArray("person");
                    JSONObject data = jsonarray.getJSONObject(0);

                    TotalPoints = data.getInt("totalpoints");
                    // String  TotalPoint = data.getString("totalpoints");

                    Toast.makeText(Menu_Activity.this, "value is "+TotalPoints, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    //Toast.makeText(Menu_Activity.this, email,Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Menu_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();
                parameters.put("email",email);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
