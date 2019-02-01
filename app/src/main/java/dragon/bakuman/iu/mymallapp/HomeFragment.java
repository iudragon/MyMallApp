package dragon.bakuman.iu.mymallapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


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
    ///////// Banner Slider


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

        sliderModelList.add(new SliderModel(R.drawable.ic_person_outline));
        sliderModelList.add(new SliderModel(R.drawable.ic_sign_out));

        sliderModelList.add(new SliderModel(R.drawable.farmer));
        sliderModelList.add(new SliderModel(R.drawable.ic_add_circle));
        sliderModelList.add(new SliderModel(R.drawable.connect));
        sliderModelList.add(new SliderModel(R.drawable.ic_shopping));
        sliderModelList.add(new SliderModel(R.drawable.ic_favorite));
        sliderModelList.add(new SliderModel(R.drawable.ic_person));
        sliderModelList.add(new SliderModel(R.drawable.ic_person_outline));
        sliderModelList.add(new SliderModel(R.drawable.ic_sign_out));

        sliderModelList.add(new SliderModel(R.drawable.farmer));
        sliderModelList.add(new SliderModel(R.drawable.ic_add_circle));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);

        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);


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

        ///////// Banner Slider

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

    ///////// Banner Slider
}











