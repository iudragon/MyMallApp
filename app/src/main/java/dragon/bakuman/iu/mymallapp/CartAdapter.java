package dragon.bakuman.iu.mymallapp;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {


    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {

            case 0:
                return CartItemModel.CART_ITEM;

            case 1:
                return CartItemModel.TOTAL_AMOUNT;

            default:
                return -1;


        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {

            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new CartItemViewHolder(cartItemView);


            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new CartTotalAmountViewHolder(cartTotalView);
            default:
                return null;

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (cartItemModelList.get(position).getType()) {

            case CartItemModel.CART_ITEM:

                String productID = cartItemModelList.get(position).getProductID();

                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupons = cartItemModelList.get(position).getFreeCoupons();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();

                boolean inStock = cartItemModelList.get(position).isInStock();
                Long productQuantity = cartItemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartItemModelList.get(position).getMaxQuantity();

                ((CartItemViewHolder) viewHolder).setItemDetails(productID, resource, title, freeCoupons, productPrice, cuttedPrice, offersApplied, position, inStock, String.valueOf(productQuantity), maxQuantity);

                break;

            case CartItemModel.TOTAL_AMOUNT:

                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;
                for (int x = 0; x < cartItemModelList.size(); x++) {

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {

                        totalItems++;

                        totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice());

                    }
                }

                if (totalItemPrice > 500) {

                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                } else {
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice + 60;
                }


                ((CartTotalAmountViewHolder) viewHolder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount);

                break;

            default:
                return;
        }

        if (lastPosition < position) {

            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView freeCouponIcon;
        private TextView productTitle;
        private TextView freeCoupons;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView couponsApplied;
        private TextView offersApplied;
        private TextView productQuantity;
        private LinearLayout couponRedemptionLayout;

        private LinearLayout deleteBtn;


        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupons = itemView.findViewById(R.id.tv_free_icon);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupons_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            freeCouponIcon = itemView.findViewById(R.id.free_icon_coupon);
            couponRedemptionLayout = itemView.findViewById(R.id.coupn_redemption_layout);

            deleteBtn = itemView.findViewById(R.id.remove_item_btn);


        }


        private void setItemDetails(String productID, String resource, String title, Long freeCouponsNo, String productPriceText, String cuttedPriceText, Long offersAppliedNo, final int position, boolean inStock, final String quantity, final Long maxQuantity) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholdericonmini)).into(productImage);

            productTitle.setText(title);


            if (inStock) {

                if (freeCouponsNo > 0) {

                    freeCouponIcon.setVisibility(View.VISIBLE);
                    freeCoupons.setVisibility(View.VISIBLE);
                    if (freeCouponsNo == 1) {

                        freeCoupons.setText("free " + freeCouponsNo + " coupon");
                    } else {

                        freeCoupons.setText("free " + freeCouponsNo + " coupon");
                    }

                } else {

                    freeCouponIcon.setVisibility(View.INVISIBLE);
                    freeCoupons.setVisibility(View.INVISIBLE);
                }

                productPrice.setText("Rs. " + productPriceText + "/-");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorBlack));
                cuttedPrice.setText("Rs. " + cuttedPriceText + "/-");
                couponRedemptionLayout.setVisibility(View.VISIBLE);


                productQuantity.setText("Qty: " + quantity);


                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);

                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        quantityDialog.setCancelable(false);

                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);

                        quantityNo.setHint("Max " + String.valueOf(maxQuantity));

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!TextUtils.isEmpty(quantityNo.getText())) {

                                    if (Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0) {

                                        if (itemView.getContext() instanceof MainActivity) {
                                            DBqueries.cartItemModelList.get(position).setProductQuantity(Long.parseLong(quantityNo.getText().toString()));


                                        } else {

                                            if (DeliveryActivity.fromCart) {


                                                DBqueries.cartItemModelList.get(position).setProductQuantity(Long.parseLong(quantityNo.getText().toString()));
                                            } else {

                                                DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.parseLong(quantityNo.getText().toString()));
                                            }
                                        }


                                        productQuantity.setText("Qty: " + quantityNo.getText());
                                    } else {

                                        Toast.makeText(itemView.getContext(), "Max quantity: " + maxQuantity.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                }


                                quantityDialog.dismiss();

                            }
                        });

                        quantityDialog.show();
                    }
                });

                if (offersAppliedNo > 0) {

                    offersApplied.setVisibility(View.VISIBLE);
                    offersApplied.setText(offersAppliedNo + " offers applied");

                } else {

                    offersApplied.setVisibility(View.INVISIBLE);
                }

            } else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                cuttedPrice.setText("");
                couponRedemptionLayout.setVisibility(View.GONE);
                freeCoupons.setVisibility(View.INVISIBLE);
                productQuantity.setVisibility(View.INVISIBLE);
                couponsApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
                freeCouponIcon.setVisibility(View.INVISIBLE);


            }

            if (showDeleteBtn) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailsActivity.running_cart_query) {
                        ProductDetailsActivity.running_cart_query = true;
                        DBqueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                    }
                }
            });
        }
    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView savedAmount;
        private TextView totalAmount;


        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);

        }

        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText) {

            totalItems.setText("Price(" + totalItemText + " items)");
            totalItemPrice.setText("Rs. " + totalItemPriceText + "/-");

            if (deliveryPriceText.equals("FREE")) {

                deliveryPrice.setText(deliveryPriceText);

            } else {
                deliveryPrice.setText("Rs. " + deliveryPriceText + "/-");
            }
            totalAmount.setText("Rs. " + totalAmountText + "/-");
            cartTotalAmount.setText("Rs. " + totalAmountText + "/-");
            savedAmount.setText("You saved Rs. " + savedAmountText + "/- on this order. ");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0) {
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                parent.setVisibility(View.GONE);

            } else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

}






