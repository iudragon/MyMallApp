package dragon.bakuman.iu.mymallapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;

    public static List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        gridView = findViewById(R.id.grid_view);

        int layout_code = getIntent().getIntExtra("layout_code", -1);

        if (layout_code == 0) {

            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            List<WishlistModel> wishlistModelList = new ArrayList<>();
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 1, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 0, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 3, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 4, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 3, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 1, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 0, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 3, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 4, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
            wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 3, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));


            WishlistAdapter adapter = new WishlistAdapter(wishlistModelList, false);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else if (layout_code == 1) {

            gridView.setVisibility(View.VISIBLE);

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
            gridView.setAdapter(gridProductLayoutAdapter);

        }

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
