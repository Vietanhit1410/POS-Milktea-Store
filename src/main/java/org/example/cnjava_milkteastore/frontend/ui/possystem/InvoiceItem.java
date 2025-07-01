package org.example.cnjava_milkteastore.frontend.ui.possystem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.cnjava_milkteastore.backend.product.dto.ProductDTO;

@Getter
@Setter

@NoArgsConstructor
class InvoiceItem {
    ProductDTO product;
    int quantity;

    public InvoiceItem(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getName() { return product.getProductName(); }
    public float getUnitPrice() { return product.getPrice(); } // Giá đơn vị
    public int getQuantity() { return quantity; }
    public ProductDTO getProduct() { return product; }
    public int getProductID() { return product.getID(); }
    public String getProductType() { return product.getProductType(); }
    public float getSubtotal() { return product.getPrice() * quantity; }
}
