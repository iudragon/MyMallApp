package dragon.bakuman.iu.mymallapp;

public class MyOrderItemModel {


    private int productImage;
    private String productTitle;
    private String deliveryStatus;

    private int rating;


    public MyOrderItemModel(int productImage, String productTitle, String deliveryStatus, int rating) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.deliveryStatus = deliveryStatus;
        this.rating = rating;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
