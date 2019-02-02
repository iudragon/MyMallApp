package dragon.bakuman.iu.mymallapp;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;

    ///////// Banner Slider

    private ViewPager bannerSliderViewPager;
    private List<SliderModel> sliderModelList;
    private int currentPage = 2;

    private Timer timer;

    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;
    ///////// Banner Slider


    ///////// Strip Ad

    private ImageView stripAdImage;
    private ConstraintLayout stripAdContainer;

    ///////// Strip Ad


    ///////// Horizontal Product Layout

    private Button horizontalLayoutViewAllBtn;
    private TextView horizontalLayoutTitle;
    private RecyclerView horizontalRecyclerView;

    ///////// Horizontal Product Layout


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<CategoryModel> categoryModelList = new ArrayList<>();
        categoryModelList.add(new CategoryModel("link", "Home"));
        categoryModelList.add(new CategoryModel("link", "Furniture"));
        categoryModelList.add(new CategoryModel("link", "Toys"));
        categoryModelList.add(new CategoryModel("link", "Shoes"));
        categoryModelList.add(new CategoryModel("link", "Books"));
        categoryModelList.add(new CategoryModel("link", "Kitchen"));
        categoryModelList.add(new CategoryModel("link", "Sports"));
        categoryModelList.add(new CategoryModel("link", "Electronics"));

        categoryAdapter = new CategoryAdapter(categoryModelList);

        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        ///////// Banner Slider

        bannerSliderViewPager = view.findViewById(R.id.banner_slider_view_pager);
        sliderModelList = new ArrayList<>();

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

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);

        bannerSliderViewPager.setAdapter(sliderAdapter);

        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);

        bannerSliderViewPager.setCurrentItem(currentPage);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                if (i == ViewPager.SCROLL_STATE_IDLE) {

                    pageLooper();
                }

            }
        };


        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

        startBannerSlideShow();

        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                pageLooper();
                stopBannerSlideShow();

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    startBannerSlideShow();

                }

                return false;
            }
        });

        ///////// Banner Slider


        ///////// Strip Ad

        stripAdImage = view.findViewById(R.id.strip_ad_image);
        stripAdContainer = view.findViewById(R.id.strip_ad_container);

        stripAdImage.setImageResource(R.drawable.farmer);
        stripAdContainer.setBackgroundColor(Color.parseColor("#000000"));

        ///////// Strip Ad


        ///////// Horizontal Product Layout

        horizontalLayoutTitle = view.findViewById(R.id.horizontal_scroll_layout_title);
        horizontalLayoutViewAllBtn = view.findViewById(R.id.horizontal_scroll_view_all_button);
        horizontalRecyclerView = view.findViewById(R.id.horizontal_product_scroll_layout_recyclerview);

        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.farmer, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_add_circle, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_card_giftcard, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_favorite, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_red, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_fitness, "Redmi 6", "Niiiiice", "Rs. 6699"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_close, "Redmi 6", "Niiiiice", "Rs. 6699"));

        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);

        horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
        horizontalProductScrollAdapter.notifyDataSetChanged();



        ///////// Horizontal Product Layout

        return view;
    }
    ///////// Banner Slider

    private void pageLooper() {

        if (currentPage == sliderModelList.size() - 2) {

            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage, false);
        }

        if (currentPage == 1) {

            currentPage = sliderModelList.size() - 3;
            bannerSliderViewPager.setCurrentItem(currentPage, false);
        }

    }

    private void startBannerSlideShow() {

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {

                if (currentPage >= sliderModelList.size()) {

                    currentPage = 1;

                }

                bannerSliderViewPager.setCurrentItem(currentPage++, true);

            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);


    }

    private void stopBannerSlideShow() {

        timer.cancel();

    }

    ///////// Banner Slider
}











