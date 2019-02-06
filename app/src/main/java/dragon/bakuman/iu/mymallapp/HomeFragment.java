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

    private RecyclerView homePageRecyclerView;

    private FirebaseFirestore firebaseFirestore;

    private HomePageAdapter adapter;

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

        /*List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
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
*/

        ///////// Horizontal Product Layout


        /////////////////////////////

        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);
        final List<HomePageModel> homePageModelList = new ArrayList<>();
        adapter = new HomePageAdapter(homePageModelList);
        homePageRecyclerView.setAdapter(adapter);

        firebaseFirestore.collection("CATEGORIES").document("HOME").collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");

                                    for (long x = 1; x < no_of_banners + 1; x++) {

                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + x).toString(), documentSnapshot.get("banner_" + x + "_background").toString()));
                                    }

                                    homePageModelList.add(new HomePageModel(0, sliderModelList));

                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    homePageModelList.add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString(), documentSnapshot.get("background").toString()));


                                } else if ((long) documentSnapshot.get("view_type") == 2) {

                                } else if ((long) documentSnapshot.get("view_type") == 3) {

                                }

                            }

                            adapter.notifyDataSetChanged();

                        } else {

                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        /////////////////////////////

        return view;
    }

}











