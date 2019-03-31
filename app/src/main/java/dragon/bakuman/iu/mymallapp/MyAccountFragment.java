package dragon.bakuman.iu.mymallapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static dragon.bakuman.iu.mymallapp.DeliveryActivity.SELECT_ADDRESS;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {


    public MyAccountFragment() {
        // Required empty public constructor
    }

    private Button viewAllAddressButton;

    public static final int MANAGE_ADDRESS = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_account, container, false);

//        viewAllAddressButton = view.findViewById(R.id.view_all_addresses_btn);
//        viewAllAddressButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myAddressIntent = new Intent(getContext(), MyAddressesActivity.class);
//                myAddressIntent.putExtra("MODE", MANAGE_ADDRESS);
//                startActivity(myAddressIntent);
//            }
//        });

        return view;
    }

}
