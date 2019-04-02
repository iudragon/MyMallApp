package dragon.bakuman.iu.mymallapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SpeciallistAdapter extends RecyclerView.Adapter<SpeciallistAdapter.ViewHolder> {

    private List<SpeciallistModel> speciallistModelList;

    private Boolean speciallist;

    private int lastPosition = -1;

    private FirebaseUser currentUser;


    public SpeciallistAdapter(List<SpeciallistModel> speciallistModelList, Boolean speciallist) {
        this.speciallistModelList = speciallistModelList;
        this.speciallist = speciallist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.speciallist_item_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String productId = speciallistModelList.get(position).getProductId();
        String resource = speciallistModelList.get(position).getProductImage();
        String title = speciallistModelList.get(position).getProductTitle();
        long freeCoupons = speciallistModelList.get(position).getFreeCoupons();
        String rating = speciallistModelList.get(position).getRating();
        long totalRatings = speciallistModelList.get(position).getTotalRatings();
        String productPrice = speciallistModelList.get(position).getProductPrice();
        String cuttedPrice = speciallistModelList.get(position).getCuttedPrice();
        boolean paymentMethod = speciallistModelList.get(position).isCOD();
        boolean inStock = speciallistModelList.get(position).isInStock();

        viewHolder.setData(productId, resource, title, freeCoupons, rating, totalRatings, productPrice, cuttedPrice, paymentMethod, position, inStock);

        if (lastPosition < position) {

            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return speciallistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupons;
        private ImageView couponIcon;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView paymentMethod;
        private TextView rating;
        private TextView totalRatings;
        private View priceCut;
        private ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupons = itemView.findViewById(R.id.free_coupon);
            couponIcon = itemView.findViewById(R.id.coupon_icon);
            rating = itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings = itemView.findViewById(R.id.total_ratings);
            priceCut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            paymentMethod = itemView.findViewById(R.id.payment_method);
            deleteBtn = itemView.findViewById(R.id.delete_button);

        }

        private void setData(final String productId, String resource, String title, long freeCouponsNo, String averageRate, long totalRatingsNo, String price, String cuttedPricevValue, boolean COD, final int index, boolean inStock) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholdericonmini)).into(productImage);

            productTitle.setText(title);
            if (freeCouponsNo != 0 && inStock) {
                couponIcon.setVisibility(View.VISIBLE);
                if (freeCouponsNo == 1) {
                    freeCoupons.setText("Free " + freeCouponsNo + " Coupon");
                } else {
                    freeCoupons.setText("Free " + freeCouponsNo + " Coupons");
                }
            } else {
                couponIcon.setVisibility(View.INVISIBLE);
                freeCoupons.setVisibility(View.INVISIBLE);
            }

            LinearLayout linearLayout = (LinearLayout) rating.getParent();
            if (inStock) {


                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorBlack));
                cuttedPrice.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

                rating.setText(averageRate);
                totalRatings.setText("(" + totalRatingsNo + ") ratings");
                productPrice.setText("Rs. " + price + "/-");
                cuttedPrice.setText("Rs. " + cuttedPricevValue + "/-");

                if (COD) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {

                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            } else {

                linearLayout.setVisibility(View.INVISIBLE);

                rating.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                cuttedPrice.setVisibility(View.INVISIBLE);

                paymentMethod.setVisibility(View.INVISIBLE);

            }


            if (speciallist) {
                if (currentUser == null){
                    deleteBtn.setVisibility(View.GONE);
                } else if (currentUser != null){
                    deleteBtn.setVisibility(View.VISIBLE);

                }



            } else {

                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!ProductDetailsActivity.running_speciallist_query) {

                        ProductDetailsActivity.running_speciallist_query = true;
                        DBqueries.removeFromSpeciallist(index, itemView.getContext());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);

                    productDetailsIntent.putExtra("PRODUCT_ID", productId);

                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }
    }


}







