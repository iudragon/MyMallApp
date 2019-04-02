package dragon.bakuman.iu.mymallapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static dragon.bakuman.iu.mymallapp.DBqueries.categoryModelList;
import static dragon.bakuman.iu.mymallapp.DBqueries.lists;
import static dragon.bakuman.iu.mymallapp.DBqueries.loadCategories;
import static dragon.bakuman.iu.mymallapp.DBqueries.loadFragmentData;
import static dragon.bakuman.iu.mymallapp.DBqueries.loadedCategoriesNames;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private ConnectivityManager connectivityManager;

    private NetworkInfo networkInfo;

    public static SwipeRefreshLayout swipeRefreshLayout;

    private List<CategoryModel> categoryModelFakeList = new ArrayList<>();

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;

    private RecyclerView homePageRecyclerView;

    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;

    private HomePageAdapter adapter;

    private ImageView noInternetConnection;

    public Button retryButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));

        noInternetConnection = view.findViewById(R.id.no_internet_connection);


        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);

        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);

        retryButton = view.findViewById(R.id.retry_button);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);


        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);


        ///// categories fake list


        categoryModelFakeList.add(new CategoryModel("null", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));

        ///// categories fake list


        ///// home page fake list

        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null", "#defeed"));
        sliderModelFakeList.add(new SliderModel("null", "#defeed"));
        sliderModelFakeList.add(new SliderModel("null", "#defeed"));
        sliderModelFakeList.add(new SliderModel("null", "#defeed"));
        sliderModelFakeList.add(new SliderModel("null", "#defeed"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();

        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));

        homePageModelFakeList.add(new HomePageModel(0, sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1, "", "#defeed"));
        homePageModelFakeList.add(new HomePageModel(2, "", "#defeed", horizontalProductScrollModelFakeList, new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3, "", "#defeed", horizontalProductScrollModelFakeList));

        ///// home page fake list

        categoryAdapter = new CategoryAdapter(categoryModelFakeList);

        adapter = new HomePageAdapter(homePageModelFakeList);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {

            MainActivity.drawer.setDrawerLockMode(0);

            noInternetConnection.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            if (categoryModelList.size() == 0) {

                loadCategories(categoryRecyclerView, getContext());
            } else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }

            categoryRecyclerView.setAdapter(categoryAdapter);

            if (lists.size() == 0) {
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());

                loadFragmentData(homePageRecyclerView, getContext(), 0, "Home");
            } else {
                adapter = new HomePageAdapter(lists.get(0));

                adapter.notifyDataSetChanged();

            }
            homePageRecyclerView.setAdapter(adapter);
        } else {

            MainActivity.drawer.setDrawerLockMode(1);

            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);

            Glide.with(this).load(R.drawable.ic_favorite).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);

            retryButton.setVisibility(View.VISIBLE);
        }

        ///// refresh layout

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                reloadPage();


            }
        });

        ///// refresh layout

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });

        return view;
    }

    private void reloadPage() {


        networkInfo = connectivityManager.getActiveNetworkInfo();
//
//        categoryModelList.clear();
//        lists.clear();
//        loadedCategoriesNames.clear();

        DBqueries.clearData();

        if (networkInfo != null && networkInfo.isConnected() == true) {

            MainActivity.drawer.setDrawerLockMode(0);

            noInternetConnection.setVisibility(View.GONE);

            retryButton.setVisibility(View.GONE);

            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            categoryAdapter = new CategoryAdapter(categoryModelFakeList);

            adapter = new HomePageAdapter(homePageModelFakeList);

            categoryRecyclerView.setAdapter(categoryAdapter);

            homePageRecyclerView.setAdapter(adapter);

            loadCategories(categoryRecyclerView, getContext());

            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerView, getContext(), 0, "Home");

        } else {

            MainActivity.drawer.setDrawerLockMode(1);

            Toast.makeText(getContext(), "No Internet NP Connection", Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.ic_favorite).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);

            swipeRefreshLayout.setRefreshing(false);
        }

    }

}











