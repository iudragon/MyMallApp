package dragon.bakuman.iu.mymallapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dragon.bakuman.iu.mymallapp.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private int previousAddress;
    private LinearLayout addNewAddressBtn;
    private TextView addressesSaved;
    private RecyclerView myAddressesRecyclerView;
    private Button deliverHereButton;

    private static AddressesAdapter addressesAdapter;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///// loading dialog

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);

        loadingDialog.setCancelable(false);

        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        ///// loading dialog

        previousAddress = DBqueries.selectedAddress;

        myAddressesRecyclerView = findViewById(R.id.addresses_recycler_view);

        deliverHereButton = findViewById(R.id.deliver_here_button);

        addNewAddressBtn = findViewById(R.id.add_new_address_btn);

        addressesSaved = findViewById(R.id.address_saved);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(linearLayoutManager);


        int mode = getIntent().getIntExtra("MODE", -1);

        if (mode == SELECT_ADDRESS) {

            deliverHereButton.setVisibility(View.VISIBLE);
        } else {

            deliverHereButton.setVisibility(View.GONE);
        }

        deliverHereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DBqueries.selectedAddress != previousAddress) {
                    final int previousAddressIndex = previousAddress;
                    loadingDialog.show();
                    Map<String, Object> updateSelection = new HashMap<>();
                    updateSelection.put("selected_" + String.valueOf(previousAddress + 1), false);
                    updateSelection.put("selected_" + String.valueOf(DBqueries.selectedAddress + 1), true);

                    previousAddress = DBqueries.selectedAddress;

                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES").update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                finish();
                            } else {
                                previousAddress = previousAddressIndex;
                                String error = task.getException().getMessage();
                                Toast.makeText(MyAddressesActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                            loadingDialog.dismiss();
                        }
                    });
                } else {
                    finish();
                }
            }
        });

        addressesAdapter = new AddressesAdapter(DBqueries.addressesModelList, mode);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator) myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();


        addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(MyAddressesActivity.this, AddAddressActivity.class);

                addAddressIntent.putExtra("INTENT", "null");

                startActivity(addAddressIntent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        addressesSaved.setText(String.valueOf(DBqueries.addressesModelList.size()) + " saved addresses");
    }

    public static void refreshItem(int deselect, int select) {
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (DBqueries.selectedAddress != previousAddress) {
                DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                DBqueries.addressesModelList.get(previousAddress).setSelected(true);
                DBqueries.selectedAddress = previousAddress;
            }

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (DBqueries.selectedAddress != previousAddress) {
            DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
            DBqueries.addressesModelList.get(previousAddress).setSelected(true);
            DBqueries.selectedAddress = previousAddress;
        }
        super.onBackPressed();
    }
}
