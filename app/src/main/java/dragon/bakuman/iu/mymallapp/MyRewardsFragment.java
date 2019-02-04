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
public class MyRewardsFragment extends Fragment {


    public MyRewardsFragment() {
        // Required empty public constructor
    }

    private RecyclerView rewardsRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);

        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardsRecyclerView.setLayoutManager(linearLayoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Discount", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Buy 1 get 3 free", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        return view;
    }

}
