package com.ssm.mall.dao.vo;

import java.math.BigDecimal;

public class CartItemVo {//结合了产品和购物车的选项
    private  Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;//该商品的购买数量
    private Integer checked;//该选项是否被选中
    private String limitQuantity;//限制购买数量
    //商品的信息
    private String proName;
    private String proSubtitle;
    private String proMainImage;
    private Integer proStatus;
    private Integer proStock;
    private BigDecimal proPrice;
    //统计数据(该商品项的总计价格)
    private BigDecimal cartItemTotalPrice;

    public CartItemVo() {
    }

    public CartItemVo(Integer userId, Integer productId, Integer quantity, Integer checked, String limitQuantity, String proName, String proSubtitle, String proMainImage, Integer proStatus, Integer proStock, BigDecimal proPrice, BigDecimal cartItemTotalPrice) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.checked = checked;
        this.limitQuantity = limitQuantity;
        this.proName = proName;
        this.proSubtitle = proSubtitle;
        this.proMainImage = proMainImage;
        this.proStatus = proStatus;
        this.proStock = proStock;
        this.proPrice = proPrice;
        this.cartItemTotalPrice = cartItemTotalPrice;
    }

    public CartItemVo(Integer id, Integer userId, Integer productId, Integer quantity, Integer checked, String limitQuantity, String proName, String proSubtitle, String proMainImage, Integer proStatus, Integer proStock, BigDecimal proPrice, BigDecimal cartItemTotalPrice) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.checked = checked;
        this.limitQuantity = limitQuantity;
        this.proName = proName;
        this.proSubtitle = proSubtitle;
        this.proMainImage = proMainImage;
        this.proStatus = proStatus;
        this.proStock = proStock;
        this.proPrice = proPrice;
        this.cartItemTotalPrice = cartItemTotalPrice;
    }

    @Override
    public String toString() {
        return "CartItemVo{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", checked=" + checked +
                ", limitQuantity='" + limitQuantity + '\'' +
                ", proName='" + proName + '\'' +
                ", proSubtitle='" + proSubtitle + '\'' +
                ", proMainImage='" + proMainImage + '\'' +
                ", proStatus=" + proStatus +
                ", proStock=" + proStock +
                ", proPrice=" + proPrice +
                ", cartItemTotalPrice=" + cartItemTotalPrice +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public String getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(String limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProSubtitle() {
        return proSubtitle;
    }

    public void setProSubtitle(String proSubtitle) {
        this.proSubtitle = proSubtitle;
    }

    public String getProMainImage() {
        return proMainImage;
    }

    public void setProMainImage(String proMainImage) {
        this.proMainImage = proMainImage;
    }

    public Integer getProStatus() {
        return proStatus;
    }

    public void setProStatus(Integer proStatus) {
        this.proStatus = proStatus;
    }

    public Integer getProStock() {
        return proStock;
    }

    public void setProStock(Integer proStock) {
        this.proStock = proStock;
    }

    public BigDecimal getProPrice() {
        return proPrice;
    }

    public void setProPrice(BigDecimal proPrice) {
        this.proPrice = proPrice;
    }

    public BigDecimal getCartItemTotalPrice() {
        return cartItemTotalPrice;
    }

    public void setCartItemTotalPrice(BigDecimal cartItemTotalPrice) {
        this.cartItemTotalPrice = cartItemTotalPrice;
    }
}
