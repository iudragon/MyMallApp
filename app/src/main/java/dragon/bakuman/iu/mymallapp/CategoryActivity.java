package dragon.bakuman.iu.mymallapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryRecyclerView = findViewById(R.id.category_recyclerview);

        ///////// Banner Slider

        List<SliderModel> sliderModelList = new ArrayList<>();

        sliderModelList.add(new SliderModel(R.drawable.ic_person_outline, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_sign_out, "#ddaaee"));

        sliderModelList.add(new SliderModel(R.drawable.farmer, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_add_circle, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.connect, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_shopping, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_favorite, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_person, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_person_outline, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_sign_out, "#ddaaee"));

        sliderModelList.add(new SliderModel(R.drawable.farmer, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_add_circle, "#ddaaee"));


        ///////// Banner Slider


        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.farmer, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_add_circle, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_card_giftcard, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_favorite, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_red, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_fitness, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_fitness, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_fitness, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_close, "Redmi 6", "Niiiiice", "Rs. 6699"));


        ///////// Horizontal Product Layout


        /////////////////////////////

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(0, sliderModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.farmer, "#000000"));
        homePageModelList.add(new HomePageModel(2, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(3, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.connect, "#0ddd00"));
        homePageModelList.add(new HomePageModel(3, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(2, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(0, sliderModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.ic_favorite, "#fff000"));


        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);


        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_search_icon) {
            return true;
        } else if (id == android.R.id.home) {

            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
