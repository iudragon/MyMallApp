package dragon.bakuman.iu.mymallapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemModelList;
    public static CartAdapter cartAdapter;
    private RecyclerView deliveryRecyclerView;
    private Button changeOrAddNewAddressButton;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullname;
    private String name, mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    private Button continueBtn;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageButton paytm, cod;
    private ConstraintLayout orderConfirmationLayout;
    private TextView orderId;
    private ImageButton continueShoppingBtn;
    private boolean successResponse = false;
    public static boolean fromCart;
    private String order_id;
    public static boolean codOrderConfirmed = false;
    private FirebaseFirestore firebaseFirestore;
    public static boolean allProductsAvailable;
    public static boolean getQtyIDs = true;


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
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_id);
        ///// loading dialog

        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);

        loadingDialog.setCancelable(false);

        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ///// loading dialog


        ///// Payment dialog

        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);

        paymentMethodDialog.setCancelable(true);

        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));

        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paytm = paymentMethodDialog.findViewById(R.id.paytm);
        cod = paymentMethodDialog.findViewById(R.id.cod_btn);

        ///// Payment dialog

        firebaseFirestore = FirebaseFirestore.getInstance();

        getQtyIDs = true;

        allProductsAvailable = true;

        order_id = UUID.randomUUID().toString().substring(0, 28);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);


        cartAdapter.notifyDataSetChanged();


        changeOrAddNewAddressButton.setVisibility(View.VISIBLE);
        changeOrAddNewAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getQtyIDs = false;

                Intent myAddressIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressIntent);
                ////// DOUBT deleted EXTRA startActivity
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allProductsAvailable) {
                    paymentMethodDialog.show();
                } else {


                }

            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getQtyIDs = false;

                paymentMethodDialog.dismiss();
                Intent otpIntent = new Intent(DeliveryActivity.this, OTPverificationActivity.class);

                otpIntent.putExtra("mobileNo", mobileNo.substring(0, 10));

                startActivity(otpIntent);
            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getQtyIDs = false;

                paymentMethodDialog.dismiss();
                loadingDialog.show();

                if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }

                final String M_id = "lwXdCh06196706964567";
                final String customer_id = FirebaseAuth.getInstance().getUid();
                String url = "http://npkimaki.website/paytm/generateChecksum.php";
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
//                                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                                        if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {
                                            showConfirmationLayout();
                                        }
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

        /////accessing quantity

        if (getQtyIDs) {

            for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                for (int y = 0; y < cartItemModelList.get(x).getProductQuantity(); y++) {

                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);

                    Map<String, Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());

                    final int finalX = x;
                    final int finalY = y;

                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName).set(timeStamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            cartItemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);
                            if (finalY + 1 == cartItemModelList.get(finalX).getProductQuantity()) {

                                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQuantity()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {


                                            List<String> serverQuantity = new ArrayList<>();

                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {


                                                serverQuantity.add(queryDocumentSnapshot.getId());
                                            }

                                            long availableQty = 0;

                                            boolean noLongerAvailable = true;

                                            for (String qtyId : cartItemModelList.get(finalX).getQtyIDs()) {

                                                if (!serverQuantity.contains(qtyId)) {


                                                    if (noLongerAvailable) {

                                                        cartItemModelList.get(finalX).setInStock(false);
                                                    } else {
                                                        cartItemModelList.get(finalX).setQtyError(true);
                                                        cartItemModelList.get(finalX).setMaxQuantity(availableQty);

                                                        Toast.makeText(DeliveryActivity.this, "HAHA, Products nahi honge kuch jitne chahiye utne", Toast.LENGTH_SHORT).show();

                                                    }

                                                    allProductsAvailable = false;

                                                } else {
                                                    availableQty++;
                                                    noLongerAvailable = false;
                                                }

                                            }

                                            cartAdapter.notifyDataSetChanged();

                                        } else {

                                            String error = task.getException().getMessage();
                                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        }
                    });

                }
            }
        } else {
            getQtyIDs = true;
        }
        /////accessing quantity

        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();

        fullname.setText(name + " - " + mobileNo);
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

        if (codOrderConfirmed) {

            showConfirmationLayout();
        }
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

        if (getQtyIDs) {

            for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                if (!successResponse) {

                    for (final String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                        final int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size() - 1))) {
                                    cartItemModelList.get(finalX).getQtyIDs().clear();


                                }
                            }
                        });


                    }
                } else {
                    cartItemModelList.get(x).getQtyIDs().clear();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {

        if (successResponse) {

            finish();
            return;
        }

        super.onBackPressed();
    }

    private void showConfirmationLayout() {


        successResponse = true;
        codOrderConfirmed = false;

        getQtyIDs = false;

        for (int x = 0; x < cartItemModelList.size() - 1; x++) {

            for (String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID", FirebaseAuth.getInstance().getUid());

            }
        }

        if (MainActivity.mainActivity != null) {

            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            MainActivity.showCart = false;
        } else {

            MainActivity.resetMainActivity = true;
        }

        if (ProductDetailsActivity.productDetailsActivity != null) {
            ProductDetailsActivity.productDetailsActivity.finish();
            ProductDetailsActivity.productDetailsActivity = null;
        }

        ///// sent confirmation SMS
        String SMS_API = "https://www.fast2sms.com/dev/bulk";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "tK0cvlNZGxW2LOQ76gjaUiTuBYCMR1EyJIhSXHofb5ks8de3FmNuA8C9Qtgf5wEFsJdaGHmp4RKveZxY");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "FSTSMS");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", mobileNo);
                body.put("message", "6714");
                body.put("variables", "{#FF#}"); // NEED #FF# HERE  DOUBT
                body.put("variables_values", order_id);
                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue.add(stringRequest);

        ///// sent confirmation SMS

        if (fromCart) {

            Map<String, Object> updateCartList = new HashMap<>();

            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();

            for (int x = 0; x < DBqueries.cartList.size(); x++) {

                if (!cartItemModelList.get(x).isInStock()) {

                    updateCartList.put("product_ID_" + cartListSize, cartItemModelList.get(x).getProductID());
                    cartListSize++;
                } else {
                    indexList.add(x);
                }

            }

            updateCartList.put("list_size", cartListSize);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART").set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        for (int x = 0; x < indexList.size(); x++) {

                            DBqueries.cartList.remove(indexList.get(x).intValue());
                            DBqueries.cartItemModelList.remove(indexList.get(x).intValue());
                            DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                        }
                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    loadingDialog.dismiss();
                }
            });
        }

        continueBtn.setEnabled(false);
        changeOrAddNewAddressButton.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderId.setText("Order ID " + order_id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
