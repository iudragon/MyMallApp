package dragon.bakuman.iu.mymallapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryRecyclerView;
    private Button changeOrAddNewAddressButton;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullname;
    private TextView fullAddress;
    private TextView pincode;
    private Button continueBtn;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageButton paytm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecyclerView = findViewById(R.id.delivery_recycler_view);

        changeOrAddNewAddressButton = findViewById(R.id.change_or_address_btn);

        totalAmount = findViewById(R.id.total_cart_amount);

        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        continueBtn = findViewById(R.id.cart_continue_btn);

        ///// loading dialog

        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);

        loadingDialog.setCancelable(false);

        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ///// loading dialog


        ///// loading dialog

        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);

        paymentMethodDialog.setCancelable(true);

        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));

        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paytm = paymentMethodDialog.findViewById(R.id.paytm);
        ///// loading dialog


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        changeOrAddNewAddressButton.setVisibility(View.VISIBLE);
        changeOrAddNewAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressIntent);
                ////// DOUBT deleted EXTRA startActivity
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.show();


            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
                loadingDialog.show();

                if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }

                final String M_id = "lwXdCh06196706964567";
                final String customer_id = FirebaseAuth.getInstance().getUid();
                final String order_id = UUID.randomUUID().toString().substring(0, 28);
                String url = "https://mallpaytmapp.000webhostapp.com/paytm/generateChecksum.php";
                final String callBackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
                RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("CHECKSUMHASH")) {

                                String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                                PaytmPGService paytmPGService = PaytmPGService.getStagingService();

                                HashMap<String, String> paramMap = new HashMap<String, String>();
                                paramMap.put("MID", M_id);
                                paramMap.put("ORDER_ID", order_id);
                                paramMap.put("CUST_ID", customer_id);
                                paramMap.put("CHANNEL_ID", "WAP");
                                paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(4, totalAmount.getText().length() - 2));
                                paramMap.put("WEBSITE", "WEBSTAGING");
                                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                                paramMap.put("CALLBACK_URL", callBackUrl);
                                paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                                PaytmOrder order = new PaytmOrder(paramMap);

                                paytmPGService.initialize(order, null);
                                paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void onTransactionResponse(Bundle inResponse) {
                                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode, String
                                            inErrorMessage, String inFailingUrl) {
                                        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle
                                            inResponse) {
                                        Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismiss();
                        Toast.makeText(DeliveryActivity.this, "Something is fkn wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", M_id);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(4, totalAmount.getText().length() - 2));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callBackUrl);

                        return paramMap;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fullname.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname());
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
    }
}
