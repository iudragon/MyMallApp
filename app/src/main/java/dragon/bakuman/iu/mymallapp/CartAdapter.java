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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                boolean qtyError = cartItemModelList.get(position).isQtyError();

                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();
                long stockQty = cartItemModelList.get(position).getStockQuantity();

                ((CartItemViewHolder) viewHolder).setItemDetails(productID, resource, title, freeCoupons, productPrice, cuttedPrice, offersApplied, position, inStock, String.valueOf(productQuantity), maxQuantity, qtyError, qtyIds, stockQty);

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


        private void setItemDetails(final String productID, String resource, String title, Long freeCouponsNo, String productPriceText, String cuttedPriceText, Long offersAppliedNo, final int position, boolean inStock, final String quantity, final Long maxQuantity, boolean qtyError, final List<String> qtyIds, final long stockQty) {

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

                if (!showDeleteBtn) {

                    if (qtyError) {

                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorAccent)));

                    } else {


                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.colorBlack));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorBlack)));

                    }
                }

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
                                        if (!showDeleteBtn) {
                                            DeliveryActivity.cartItemModelList.get(position).setQtyError(false);

                                            final int initialQty = Integer.parseInt(quantity);

                                            final int finalQty = Integer.parseInt(quantityNo.getText().toString());
                                            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                            if (finalQty > initialQty) {

                                                for (int y = 0; y < finalQty - initialQty; y++) {

                                                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);

                                                    Map<String, Object> timeStamp = new HashMap<>();
                                                    timeStamp.put("time", FieldValue.serverTimestamp());

                                                    final int finalY = y;

                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName).set(timeStamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            qtyIds.add(quantityDocumentName);


                                                            if (finalY + 1 == finalQty - initialQty) {

                                                                firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {


                                                                            List<String> serverQuantity = new ArrayList<>();

                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {


                                                                                serverQuantity.add(queryDocumentSnapshot.getId());
                                                                            }

                                                                            long availableQty = 0;


                                                                            for (String qtyId : qtyIds) {

                                                                                if (!serverQuantity.contains(qtyId)) {


                                                                                    DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                    DeliveryActivity.cartItemModelList.get(position).setMaxQuantity(availableQty);

                                                                                    Toast.makeText(itemView.getContext(), "HAHA, Products nahi honge kuch jitne chahiye utne", Toast.LENGTH_SHORT).show();


                                                                                    DeliveryActivity.allProductsAvailable = false;


                                                                                } else {

                                                                                    availableQty++;
                                                                                }

                                                                            }

                                                                            DeliveryActivity.cartAdapter.notifyDataSetChanged();

                                                                        } else {

                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                            }

                                                        }
                                                    });

                                                }
                                            } else if (initialQty > finalQty) {


                                                for (int x = 0; x < initialQty - finalQty; x++) {

                                                    final String qtyId = qtyIds.get(qtyIds.size() - 1 - x);

                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            qtyIds.remove(qtyId);
                                                            DeliveryActivity.cartAdapter.notifyDataSetChanged();

                                                        }
                                                    });


                                                }


                                            }

                                        }

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

//            deleteBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!ProductDetailsActivity.running_cart_query) {
//                        ProductDetailsActivity.running_cart_query = true;
////                        DBqueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
//                    }
//                }
//            });
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

                if (DeliveryActivity.fromCart) {

                    DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                    DeliveryActivity.cartItemModelList.remove(DeliveryActivity.cartItemModelList.size() - 1);
                }

                if (showDeleteBtn){
                    DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);


                }
                parent.setVisibility(View.GONE);

            } else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

}






