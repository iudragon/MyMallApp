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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();

    public static List<String> loadedCategoriesNames = new ArrayList<>();

    public static List<String> wishlist = new ArrayList<>();
    public static List<String> speciallist = new ArrayList<>();

    public static List<WishlistModel> wishlistModelList = new ArrayList<>();
    public static List<SpeciallistModel> speciallistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();

    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();



    public static int selectedAddress = -1;



    public static List<RewardModel> rewardModelList = new ArrayList<>();

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

                                }



                                else if ((long) documentSnapshot.get("view_type") == 2) {

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

        firebaseFirestore.collection("USERS").document("pyFEsRmGjJVWejrOFagS0yEzJ1x1").collection("USER_DATA").document("MY_WISHLIST").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                                        final DocumentSnapshot documentSnapshot = task.getResult();

                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {

                                                        wishlistModelList.add(new WishlistModel(productId, documentSnapshot.get("product_image_1").toString(), documentSnapshot.get("product_title").toString(), (long) documentSnapshot.get("free_coupons"), documentSnapshot.get("average_rating").toString(), (long) documentSnapshot.get("total_ratings"), documentSnapshot.get("product_price").toString(), documentSnapshot.get("cutted_price").toString(), (boolean) documentSnapshot.get("COD"), true));


                                                    } else {

                                                        wishlistModelList.add(new WishlistModel(productId, documentSnapshot.get("product_image_1").toString(), documentSnapshot.get("product_title").toString(), (long) documentSnapshot.get("free_coupons"), documentSnapshot.get("average_rating").toString(), (long) documentSnapshot.get("total_ratings"), documentSnapshot.get("product_price").toString(), documentSnapshot.get("cutted_price").toString(), (boolean) documentSnapshot.get("COD"), false));


                                                    }

                                                    MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();


                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


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

    //// SPECIAL


    public static void loadSpeciallist(final Context context, final Dialog dialog, final boolean loadProductData) {

        speciallist.clear();

        firebaseFirestore.collection("USERS").document("pyFEsRmGjJVWejrOFagS0yEzJ1x1").collection("USER_DATA").document("MY_SPECIALLIST").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                        speciallist.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBqueries.speciallist.contains(ProductDetailsActivity.productID)) {



                            ProductDetailsActivity.ALREADY_ADDED_TO_SPECIALLIST = true;

                            if (ProductDetailsActivity.addToSpeciallistBtn != null) {

                                ProductDetailsActivity.addToSpeciallistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent));

                            }


                        } else {

                            if (ProductDetailsActivity.addToSpeciallistBtn != null) {


                                ProductDetailsActivity.addToSpeciallistBtn.setSupportImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));

                            }

                            ProductDetailsActivity.ALREADY_ADDED_TO_SPECIALLIST = false;
                        }

                        if (loadProductData) {

                            speciallistModelList.clear();

                            final String productId = task.getResult().get("product_ID_" + x).toString();

                            firebaseFirestore.collection("PRODUCTS").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        final DocumentSnapshot documentSnapshot = task.getResult();

                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {

                                                        speciallistModelList.add(new SpeciallistModel(productId, documentSnapshot.get("product_image_1").toString(), documentSnapshot.get("product_title").toString(), (long) documentSnapshot.get("free_coupons"), documentSnapshot.get("average_rating").toString(), (long) documentSnapshot.get("total_ratings"), documentSnapshot.get("product_price").toString(), documentSnapshot.get("cutted_price").toString(), (boolean) documentSnapshot.get("COD"), true));


                                                    } else {

                                                        speciallistModelList.add(new SpeciallistModel(productId, documentSnapshot.get("product_image_1").toString(), documentSnapshot.get("product_title").toString(), (long) documentSnapshot.get("free_coupons"), documentSnapshot.get("average_rating").toString(), (long) documentSnapshot.get("total_ratings"), documentSnapshot.get("product_price").toString(), documentSnapshot.get("cutted_price").toString(), (boolean) documentSnapshot.get("COD"), false));


                                                    }

                                                    MySpeciallistFragment.speciallistAdapter.notifyDataSetChanged();


                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


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

    //// SPECIAL

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

    //// SPECIAL

    public static void removeFromSpeciallist(final int index, final Context context) {

        final String removeProductId = speciallist.get(index);

        speciallist.remove(index);
        Map<String, Object> updateSpeciallist = new HashMap<>();

        for (int x = 0; x < speciallist.size(); x++) {

            updateSpeciallist.put("product_ID_" + x, speciallist.get(x));
        }

        updateSpeciallist.put("list_size", (long) speciallist.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_SPECIALLIST").set(updateSpeciallist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    if (speciallistModelList.size() != 0) {

                        speciallistModelList.remove(index);

                        MySpeciallistFragment.speciallistAdapter.notifyDataSetChanged();
                    }

                    ProductDetailsActivity.ALREADY_ADDED_TO_SPECIALLIST = false;

                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();

                } else {

                    if (ProductDetailsActivity.addToSpeciallistBtn != null) {

                        ProductDetailsActivity.addToSpeciallistBtn.setSupportImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.btnRed)));
                    }

                    speciallist.add(index, removeProductId);

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }


                ProductDetailsActivity.running_speciallist_query = false;

            }
        });
    }

    //// SPECIAL




    public static void loadRewards(final Context context, final Dialog loadingDialog) {

        rewardModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        if (documentSnapshot.get("type").toString().equals("Discount")) {
                            rewardModelList.add(new RewardModel(documentSnapshot.get("type").toString(),
                                    documentSnapshot.get("lower_limit").toString(),
                                    documentSnapshot.get("upper_limit").toString(),
                                    documentSnapshot.get("percentage").toString(),
                                    documentSnapshot.get("body").toString(),
                                    (Date) documentSnapshot.getTimestamp("validity").toDate()));
                        } else {
                            rewardModelList.add(new RewardModel(documentSnapshot.get("type").toString(),
                                    documentSnapshot.get("lower_limit").toString(),
                                    documentSnapshot.get("upper_limit").toString(),
                                    documentSnapshot.get("amount").toString(),
                                    documentSnapshot.get("body").toString(),
                                    (Date) documentSnapshot.getTimestamp("validity").toDate()));
                        }
                    }

                    MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();

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
        speciallist.clear();
        wishlistModelList.clear();
        speciallistModelList.clear();
        cartList.clear();

        myRatedIds.clear();
        myRating.clear();
        rewardModelList.clear();
    }

}