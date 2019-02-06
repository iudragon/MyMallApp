package dragon.bakuman.iu.mymallapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private List<CategoryModel> categoryModelList;

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;

    private RecyclerView testing;

    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));

                    }

                    categoryAdapter.notifyDataSetChanged();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });


        ///////// Banner Slider

        List<SliderModel> sliderModelList = new ArrayList<>();

        sliderModelList.add(new SliderModel(R.drawable.farmer, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_add_circle, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.connect, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_shopping, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_favorite, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_person, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_person_outline, "#ddaaee"));
        sliderModelList.add(new SliderModel(R.drawable.ic_sign_out, "#ddaaee"));

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

        testing = view.findViewById(R.id.home_page_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(0, sliderModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.farmer, "#000000"));
        homePageModelList.add(new HomePageModel(2, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(3, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.connect, "#0ddd00"));
        homePageModelList.add(new HomePageModel(3, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(2, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.ic_favorite, "#fff000"));
        homePageModelList.add(new HomePageModel(0, sliderModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.farmer, "#000000"));
        homePageModelList.add(new HomePageModel(2, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(3, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.connect, "#0ddd00"));
        homePageModelList.add(new HomePageModel(3, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(2, "Deals of the day!!!", horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(1, R.drawable.ic_favorite, "#fff000"));


        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);

        testing.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        /////////////////////////////

        return view;
    }

}











