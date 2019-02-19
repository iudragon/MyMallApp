package dragon.bakuman.iu.mymallapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPverificationActivity extends AppCompatActivity {

    private TextView phoneNo;
    private EditText otp;
    private Button verifyBtn;
    private String userNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        phoneNo = findViewById(R.id.phone_no);
        otp = findViewById(R.id.otp);
        verifyBtn = findViewById(R.id.verify_btn);
        userNo = getIntent().getStringExtra("mobileNo");

        phoneNo.setText("Check your mobile +91 " + userNo + " for veriffcation code");

        Random random = new Random();
        final int OTP_number = random.nextInt(999999 - 111111) + 111111;

        String SMS_API = "https://www.fast2sms.com/dev/bulk";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (otp.getText().toString().equals(String.valueOf(OTP_number))) {

                            DeliveryActivity.codOrderConfirmed = true;
                            finish();

                        } else {

                            Toast.makeText(OTPverificationActivity.this, "OTP is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                finish();
                Toast.makeText(OTPverificationActivity.this, "you failed in OTP.lol", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "tK0cvlNZGxW2LOQ76gjaUiTuBYCMR1EyJIhSXHofb5ks8de3FmNuA8C9Qtgf5wEFsJdaGHmp4RKveZxY");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "FSTSMS");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", userNo);
                body.put("message", "6516");
                body.put("variables", "{#BB#}");
                body.put("variables_values", String.valueOf(OTP_number));
                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(OTPverificationActivity.this);
        requestQueue.add(stringRequest);

    }
}
