package dragon.bakuman.iu.mymallapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();

    public static List<String> loadedCategoriesNames = new ArrayList<>();

    public static List<String> wishlist = new ArrayList<>();

    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();

    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();

    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static int selectedAddress = -1;

    public static List<AddressesModel> addressesModelList = new ArrayList<>();

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {

        categoryModelList.clear();

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));

                    }

                    CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);

                    categoryRecyclerView.setAdapter(categoryAdapter);

                    categoryAdapter.notifyDataSetChanged();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static void loadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName) {

        firebaseFirestore.collection("CATEGORIES").document(categoryName.toUpperCase()).collection("TOP_DEALS").orderBy("index").get()
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

                                    lists.get(index).add(new HomePageModel(0, sliderModelList));

                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString(), documentSnapshot.get("background").toString()));


                                } else if ((long) documentSnapshot.get("view_type") == 2) {

                                    List<WishlistModel> viewAllProductlist = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();


                                    long no_of_products = (long) documentSnapshot.get("no_of_products");

                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString(), documentSnapshot.get("product_image_" + x).toString(), documentSnapshot.get("product_title_" + x).toString(), documentSnapshot.get("product_subtitle_" + x).toString(), documentSnapshot.get("product_price_" + x).toString()));


                                        viewAllProductlist.add(new WishlistModel(documentSnapshot.get("product_ID_" + x).toString(), documentSnapshot.get("product_image_" + x).toString(), documentSnapshot.get("product_full_title_" + x).toString(), (long) documentSnapshot.get("free_coupons_" + x), documentSnapshot.get("average_rating_" + x).toString(), (long) documentSnapshot.get("total_ratings_" + x), documentSnapshot.get("product_price_" + x).toString(), documentSnapshot.get("cutted_price_" + x).toString(), (boolean) documentSnapshot.get("COD_" + x), (boolean) documentSnapshot.get("in_stock_" + x)));

                                    }

                                    lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, viewAllProductlist));


                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> gridLayoutModelList = new ArrayList<>();


                                    long no_of_products = (long) documentSnapshot.get("no_of_products");

                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        gridLayoutModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString(), documentSnapshot.get("product_image_" + x).toString(), documentSnapshot.get("product_title_" + x).toString(), documentSnapshot.get("product_subtitle_" + x).toString(), documentSnapshot.get("product_price_" + x).toString()));

                                    }

                                    lists.get(index).add(new HomePageModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), gridLayoutModelList));


                                }

                            }

                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));

                            homePageRecyclerView.setAdapter(homePageAdapter);

                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);

                        } else {

                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadWishlist(final Context context, final Dialog dialog, final boolean loadProductData) {

        wishlist.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                        wishlist.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBqueries.wishlist.contains(ProductDetailsActivity.productID)) {

                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;

                            if (ProductDetailsActivity.addToWishlistBtn != null) {

                                ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent));

                            }


                        } else {

                            if (ProductDetailsActivity.addToWishlistBtn != null) {


                                ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));

                            }

                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }

                        if (loadProductData) {

                            wishlistModelList.clear();

                            final String productId = task.getResult().get("product_ID_" + x).toString();

                            firebaseFirestore.collection("PRODUCTS").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        wishlistModelList.add(new WishlistModel(productId, task.getResult().get("product_image_1").toString(), task.getResult().get("product_title").toString(), (long) task.getResult().get("free_coupons"), task.getResult().get("average_rating").toString(), (long) task.getResult().get("total_ratings"), task.getResult().get("product_price").toString(), task.getResult().get("cutted_price").toString(), (boolean) task.getResult().get("COD"), (boolean) task.getResult().get("in_stock")));

                                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();

                                    } else {

                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }

                dialog.dismiss();
            }
        });
    }

    public static void removeFromWishlist(final int index, final Context context) {

        final String removeProductId = wishlist.get(index);

        wishlist.remove(index);
        Map<String, Object> updateWishlist = new HashMap<>();

        for (int x = 0; x < wishlist.size(); x++) {

            updateWishlist.put("product_ID_" + x, wishlist.get(x));
        }

        updateWishlist.put("list_size", (long) wishlist.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST").set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    if (wishlistModelList.size() != 0) {

                        wishlistModelList.remove(index);

                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }

                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;

                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();

                } else {

                    if (ProductDetailsActivity.addToWishlistBtn != null) {

                        ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.btnRed)));
                    }

                    wishlist.add(index, removeProductId);

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }


                ProductDetailsActivity.running_wishlist_query = false;

            }
        });
    }

    public static void loadRatingList(final Context context) {

        if (!ProductDetailsActivity.running_rating_query) {
            ProductDetailsActivity.running_rating_query = true;

            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                            myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailsActivity.productID)) {

                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;

                                if (ProductDetailsActivity.rateNowContainer != null) {

                                    ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                }
                            }
                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }

                    ProductDetailsActivity.running_rating_query = false;
                }
            });
        }
    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount, final TextView cartTotalAmount) {
        cartList.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                        cartList.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBqueries.cartList.contains(ProductDetailsActivity.productID)) {

                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;

                        } else {

                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                        }

                        if (loadProductData) {

                            cartItemModelList.clear();

                            final String productId = task.getResult().get("product_ID_" + x).toString();

                            firebaseFirestore.collection("PRODUCTS").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        int index = 0;
                                        if (cartList.size() >= 2) {
                                            index = cartList.size() - 2;
                                        }

                                        cartItemModelList.add(index, new CartItemModel(CartItemModel.CART_ITEM, productId, task.getResult().get("product_image_1").toString(), task.getResult().get("product_title").toString(), (long) task.getResult().get("free_coupons"), task.getResult().get("product_price").toString(), task.getResult().get("cutted_price").toString(), (long) 1, (long) 0, (long) 0, (boolean) task.getResult().get("in_stock")));

                                        if (cartList.size() == 1) {
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                            parent.setVisibility(View.VISIBLE);
                                        }
                                        if (cartList.size() == 0) {
                                            cartItemModelList.clear();
                                        }

                                        MyCartFragment.cartAdapter.notifyDataSetChanged();

                                    } else {

                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    if (cartList.size() != 0) {
                        badgeCount.setVisibility(View.VISIBLE);
                    } else badgeCount.setVisibility(View.INVISIBLE);

                    if (DBqueries.cartList.size() < 99) {

                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    } else {
                        badgeCount.setText("99");

                    }


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }

                dialog.dismiss();
            }
        });

    }

    public static void removeFromCart(final int index, final Context context, final TextView cartTotalAmount) {

        final String removeProductId = cartList.get(index);

        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();

        for (int x = 0; x < cartList.size(); x++) {

            updateCartList.put("product_ID_" + x, cartList.get(x));
        }

        updateCartList.put("list_size", (long) cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART").set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    if (cartItemModelList.size() != 0) {

                        cartItemModelList.remove(index);

                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }

                    if (cartList.size() == 0) {

                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);

                        cartItemModelList.clear();
                    }

                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();

                } else {

                    cartList.add(index, removeProductId);

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }


                ProductDetailsActivity.running_cart_query = false;

            }
        });
    }

    public static void loadAddresses(final Context context, final Dialog loadingDialog) {

        addressesModelList.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Intent deliveryIntent;
                    if ((long) task.getResult().get("list_size") == 0) {
                        deliveryIntent = new Intent(context, AddAddressActivity.class);

                        deliveryIntent.putExtra("INTENT", "deliveryIntent");

                    } else {

                        for (long x = 1; x < (long) task.getResult().get("list_size") + 1; x++) {
                            addressesModelList.add(new AddressesModel(task.getResult().get("fullname_" + x).toString(),
                                    task.getResult().get("address_" + x).toString(),
                                    task.getResult().get("pincode_" + x).toString(),
                                    (boolean) task.getResult().get("selected_" + x),
                                    task.getResult().get("mobile_no_" + x).toString()));

                            if ((boolean) task.getResult().get("selected_" + x)) {

                                selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        deliveryIntent = new Intent(context, DeliveryActivity.class);

                    }
                    context.startActivity(deliveryIntent);
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }

                loadingDialog.dismiss();
            }
        });
    }

    public static void clearData() {

        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishlist.clear();
        wishlistModelList.clear();
        cartList.clear();
        cartItemModelList.clear();

        myRatedIds.clear();
        myRating.clear();
        addressesModelList.clear();
    }

}