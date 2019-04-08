package dragon.bakuman.iu.mymallapp;

public class WishlistModel {

    private String productId;
    private String productImage;
    private String productTitle;
    private String productPrice;
    private String cuttedPrice;

    public WishlistModel(String productId, String productImage, String productTitle, String productPrice, String cuttedPrice) {
        this.productId = productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;

    }


    public String getProductImage() {
        return productImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

}
