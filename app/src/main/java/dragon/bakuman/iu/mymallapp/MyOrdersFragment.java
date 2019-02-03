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
public class MyOrdersFragment extends Fragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrdersRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.connect, "Pixel 222 Black", "Delivered on Monday, 19th Jan 2019", 2));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.farmer, "Pixel 222 Black", "Delivered on Monday, 19th Jan 2019", 5));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.ic_favorite, "Pixel 222 Black", "Cancelled", 0));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.ic_mall, "Pixel 222 Black", "Delivered on Monday, 19th Jan 2019", 1));


        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return view;

    }

}
