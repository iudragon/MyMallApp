package dragon.bakuman.iu.mymallapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import static dragon.bakuman.iu.mymallapp.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {


    private RecyclerView myAddressesRecyclerView;
    private Button deliverHereButton;

    private static AddressesAdapter addressesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAddressesRecyclerView = findViewById(R.id.addresses_recycler_view);

        deliverHereButton = findViewById(R.id.deliver_here_button);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(linearLayoutManager);

        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("G_DRAGON", "S.KOREA", "000000", true));
        addressesModelList.add(new AddressesModel("iKON", "S.KOREA", "000000", false));
        addressesModelList.add(new AddressesModel("SEUNGRI", "S.KOREA", "000000", false));
        addressesModelList.add(new AddressesModel("TAEYANG", "S.KOREA", "000000", false));
        addressesModelList.add(new AddressesModel("G_DRAGON", "S.KOREA", "000000", false));
        addressesModelList.add(new AddressesModel("DAESUNG", "S.KOREA", "000000", false));
        addressesModelList.add(new AddressesModel("TWICE", "S.KOREA", "000000", false));
        addressesModelList.add(new AddressesModel("BLACKPINK", "S.KOREA", "000000", false));
        addressesModelList.add(new AddressesModel("TOP", "S.KOREA", "000000", false));

        int mode = getIntent().getIntExtra("MODE", -1);

        if (mode == SELECT_ADDRESS) {

            deliverHereButton.setVisibility(View.VISIBLE);
        } else {

            deliverHereButton.setVisibility(View.GONE);
        }

        addressesAdapter = new AddressesAdapter(addressesModelList, mode);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator) myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();

    }

    public static void refreshItem(int deselect, int select) {
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
