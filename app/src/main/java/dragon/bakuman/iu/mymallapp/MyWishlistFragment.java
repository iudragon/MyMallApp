package dragon.bakuman.iu.mymallapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class MyWishlistFragment extends Fragment {


    public MyWishlistFragment() {
        // Required empty public constructor
    }

    private RecyclerView wishlistRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

        List<WishlistModel> wishlistModelList = new ArrayList<>();
        wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 1, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 0, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 3, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 4, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.farmer, "Pixel 222", 3, "3", 133, "Rs. 4999/-", "Rs. 5999/-", "Cash on Delivery"));

        WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList, true);
        wishlistRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();


        return view;
    }

}
