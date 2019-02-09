package dragon.bakuman.iu.mymallapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dragon.bakuman.iu.mymallapp.MainActivity.showCart;
import static dragon.bakuman.iu.mymallapp.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;


    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;

    private ImageView codIndicator;
    private TextView tvCodIndicator;


    private ViewPager productImagesViewPager;
    private TabLayout viewPagerIndicator;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static FloatingActionButton addToWishlistBtn;

    ///// product description

    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;

    private TextView productOnlyDescriptionBody;

    private String productDescription;
    private String productOtherDetails;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();


    ///// product description

    private LinearLayout couponRedemptionLayout;

    private Button couponRedeemBtn;

    private TextView rewardTitle;
    private TextView rewardBody;


    ///// ratings layout

    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;

    private TextView averageRating;

    ///// ratings layout

    private Button buyNowBtn;

    private LinearLayout addToCartBtn;

    private FirebaseFirestore firebaseFirestore;

    ///// coupon dialog

    public static TextView couponTitle;
    public static TextView couponExpiryDate;
    public static TextView couponBody;
    private static RecyclerView couponsRecyclerView;
    private static LinearLayout selectedCoupon;

    ///// coupon dialog

    private Dialog loadingDialog;

    private Dialog signInDialog;

    private FirebaseUser currentUser;

    public static String productID;

    private DocumentSnapshot documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewPagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);

        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout = findViewById(R.id.product_details_tablayout);

        buyNowBtn = findViewById(R.id.buy_now_btn);

        couponRedeemBtn = findViewById(R.id.coupon_redemption_btn);

        productTitle = findViewById(R.id.product_title);

        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);

        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);

        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);

        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indicator_imageview);

        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);


        productDetailsTabsContainer = findViewById(R.id.product_details_tab_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);

        productOnlyDescriptionBody = findViewById(R.id.product_details_body);

        totalRatings = findViewById(R.id.total_ratings);

        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);

        totalRatingsFigure = findViewById(R.id.total_ratings_figure);

        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);


        averageRating = findViewById(R.id.average_rating);

        addToCartBtn = findViewById(R.id.add_to_cart_btn);

        couponRedemptionLayout = findViewById(R.id.coupn_redemption_layout);

        initialRating = -1;

        ///// loading dialog

        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);

        loadingDialog.setCancelable(false);

        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));

        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        loadingDialog.show();

        ///// loading dialog


        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("PRODUCTS").document(productID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    documentSnapshot = task.getResult();

                    for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                        productImages.add(documentSnapshot.get("product_image_" + x).toString());

                    }

                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product_title").toString());
                    averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());

                    totalRatingMiniView.setText("(" + documentSnapshot.get("total_ratings") + ")ratings");

                    productPrice.setText("Rs. " + documentSnapshot.get("product_price").toString() + "/-");

                    cuttedPrice.setText("Rs. " + documentSnapshot.get("cutted_price").toString() + "/-");


                    if ((boolean) documentSnapshot.get("COD")) {


                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);
                    } else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }

                    rewardTitle.setText((long) documentSnapshot.get("free_coupons") + documentSnapshot.get("free_coupon_title").toString());


                    rewardBody.setText(documentSnapshot.get("free_coupon_body").toString());

                    if ((boolean) documentSnapshot.get("use_tab_layout")) {
                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        productDetailsOnlyContainer.setVisibility(View.GONE);

                        productDescription = documentSnapshot.get("product_description").toString();

                        productOtherDetails = documentSnapshot.get("product_other_details").toString();

                        for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {

                            productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));

                            for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(),
                                        documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));

                            }
                        }

                    } else {
                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                    }


                    totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                    for (int x = 0; x < 5; x++) {

                        TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                        rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);

                        int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));

                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));

                    }

                    totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));

                    averageRating.setText(documentSnapshot.get("average_rating").toString());

                    productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));


                    if (currentUser != null) {

                        if (DBqueries.myRating.size() == 0) {

                            DBqueries.loadRatingList(ProductDetailsActivity.this);
                        }

                        if (DBqueries.wishlist.size() == 0) {

                            DBqueries.loadWishlist(ProductDetailsActivity.this, loadingDialog, false);

                        } else {
                            loadingDialog.dismiss();
                        }


                    } else {
                        loadingDialog.dismiss();
                    }

                    if (DBqueries.myRatedIds.contains(productID)) {

                        int index = DBqueries.myRatedIds.indexOf(productID);

                        initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;

                        setRating(initialRating);
                    }


                    if (DBqueries.wishlist.contains(productID)) {

                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent));
                    } else {
                        addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        ALREADY_ADDED_TO_WISHLIST = false;
                    }


                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                }
            }
        });

        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);
        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser == null) {

                    signInDialog.show();
                } else {

                    if (!running_wishlist_query) {

                        running_wishlist_query = true;

                        if (ALREADY_ADDED_TO_WISHLIST) {

                            int index = DBqueries.wishlist.indexOf(productID);

                            DBqueries.removeFromWishlist(index, ProductDetailsActivity.this);

                            addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                        } else {
                            addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnRed)));

                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishlist.size()), productID);

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")

                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Map<String, Object> updateListSize = new HashMap<>();
                                        updateListSize.put("list_size", (long) (DBqueries.wishlist.size() + 1));

                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")

                                                .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    if (DBqueries.wishlistModelList.size() != 0) {

                                                        DBqueries.wishlistModelList.add(new WishlistModel(productID, documentSnapshot.get("product_image_1").toString(), documentSnapshot.get("product_title").toString(), (long) documentSnapshot.get("free_coupons"), documentSnapshot.get("average_rating").toString(), (long) documentSnapshot.get("total_ratings"), documentSnapshot.get("product_price").toString(), documentSnapshot.get("cutted_price").toString(), (boolean) documentSnapshot.get("COD")));

                                                    }

                                                    ALREADY_ADDED_TO_WISHLIST = true;

                                                    addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                                    DBqueries.wishlist.add(productID);
                                                    Toast.makeText(ProductDetailsActivity.this, "Added to wishlist success", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                                                }

                                                running_wishlist_query = false;

                                            }
                                        });
                                    } else {
                                        running_wishlist_query = false;

                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    }

                }

            }
        });


        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                productDetailsViewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ///// ratings layout

        rateNowContainer = findViewById(R.id.rate_now_container);

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {

            final int startPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (currentUser == null) {
                        signInDialog.show();
                    } else {

                        if (startPosition != initialRating) {

                            if (!running_rating_query) {

                                running_rating_query = true;

                                setRating(startPosition);

                                Map<String, Object> updateRating = new HashMap<>();

                                if (DBqueries.myRatedIds.contains(productID)) {

                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - startPosition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);

                                    updateRating.put(startPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);

                                    updateRating.put("average_rating", calculateAverageRating((long) startPosition - initialRating, true));


                                } else {


                                    updateRating.put(startPosition + 1 + "_star", (long) documentSnapshot.get(startPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) startPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                }

                                firebaseFirestore.collection("PRODUCTS").document(productID).update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Map<String, Object> myRating = new HashMap<>();

                                            if (DBqueries.myRatedIds.contains(productID)) {

                                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) startPosition + 1);
                                            } else {
                                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) startPosition + 1);
                                            }
                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS").update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (DBqueries.myRatedIds.contains(productID)) {

                                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) startPosition + 1);

                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - startPosition - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                        } else {

                                                            DBqueries.myRatedIds.add(productID);
                                                            DBqueries.myRating.add((long) startPosition + 1);

                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - startPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");

                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                            Toast.makeText(ProductDetailsActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
                                                        }


                                                        for (int x = 0; x < 5; x++) {

                                                            TextView ratingFigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);


                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());

                                                            progressBar.setMax(maxProgress);

                                                            progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                                                        }

                                                        initialRating = startPosition;

                                                        averageRating.setText(calculateAverageRating(0, true));
                                                        averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                        if (DBqueries.wishlist.contains(productID) && DBqueries.wishlistModelList.size() != 0) {
                                                            int index = DBqueries.wishlist.indexOf(productID);
                                                            DBqueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                            DBqueries.wishlistModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));

                                                        }

                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }

                                                    running_rating_query = false;
                                                }
                                            });

                                        } else {

                                            running_rating_query = false;

                                            setRating(initialRating);

                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }


                    }
                }
            });
        }

        ///// ratings layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentUser == null) {
                    signInDialog.show();
                } else {

                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);

                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {

                    ///// TODO: add to cart

                }
            }
        });

        ///// coupon dialog

        final Dialog checkCouponPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCouponPriceDialog.setContentView(R.layout.coupon_redeem_dialog);
        checkCouponPriceDialog.setCancelable(true);
        checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCouponPriceDialog.findViewById(R.id.toggle_recyclerview);
        couponsRecyclerView = checkCouponPriceDialog.findViewById(R.id.coupons_recyclerview);
        selectedCoupon = checkCouponPriceDialog.findViewById(R.id.selected_coupon);

        couponTitle = checkCouponPriceDialog.findViewById(R.id.coupon_title);
        couponExpiryDate = checkCouponPriceDialog.findViewById(R.id.coupon_validity);
        couponBody = checkCouponPriceDialog.findViewById(R.id.coupon_body);


        TextView originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
        TextView discountPrice = checkCouponPriceDialog.findViewById(R.id.discount_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponsRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Discount", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Buy 1 get 3 free", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd Jan 2020", "Haaha lol. You won. Haaahah. You won really. hahahahaa 20 % off"));


        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList, true);
        couponsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });

        ///// coupon dialog

        couponRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCouponPriceDialog.show();
            }
        });


        ///// sign in dialog


        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);

        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        final Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;

                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        ///// sign in dialog


    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {

            couponRedemptionLayout.setVisibility(View.GONE);

        } else {
            couponRedemptionLayout.setVisibility(View.VISIBLE);

        }

        if (currentUser != null) {

            if (DBqueries.myRating.size() == 0) {

                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }

            if (DBqueries.wishlist.size() == 0) {

                DBqueries.loadWishlist(ProductDetailsActivity.this, loadingDialog, false);


            } else {
                loadingDialog.dismiss();
            }

        } else {
            loadingDialog.dismiss();
        }

        if (DBqueries.myRatedIds.contains(productID)) {

            int index = DBqueries.myRatedIds.indexOf(productID);

            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;

            setRating(initialRating);
        }


        if (DBqueries.wishlist.contains(productID)) {

            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent));
        } else {

            addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

            ALREADY_ADDED_TO_WISHLIST = false;
        }

    }

    public static void showDialogRecyclerView() {

        if (couponsRecyclerView.getVisibility() == View.GONE) {

            couponsRecyclerView.setVisibility(View.VISIBLE);

            selectedCoupon.setVisibility(View.GONE);
        } else {
            couponsRecyclerView.setVisibility(View.GONE);
            selectedCoupon.setVisibility(View.VISIBLE);
        }
    }

    public static void setRating(int startPosition) {


        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starButton = (ImageView) rateNowContainer.getChildAt(x);
            starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#8F8989")));
            if (x <= startPosition) {

                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#D6D60B")));

            }
        }
    }

    private String calculateAverageRating(long currentUserRating, boolean update) {

        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {

            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);


            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        }

        totalStars = totalStars + currentUserRating;

        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {

            if (currentUser == null) {

                signInDialog.show();
            } else {

                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
