package dragon.bakuman.iu.mymallapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static dragon.bakuman.iu.mymallapp.ProductDetailsActivity.productDescription;
import static dragon.bakuman.iu.mymallapp.ProductDetailsActivity.productOtherDetails;
import static dragon.bakuman.iu.mymallapp.ProductDetailsActivity.tabPosition;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDescriptionFragment extends Fragment {


    public ProductDescriptionFragment() {
        // Required empty public constructor
    }

    private TextView descriptionBody;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_description, container, false);

        descriptionBody = view.findViewById(R.id.tv_product_description);

        if (tabPosition == 0) {

            descriptionBody.setText(productDescription);

        } else {

            descriptionBody.setText(productOtherDetails);
        }



        return view;
    }

}
