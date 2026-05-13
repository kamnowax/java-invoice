package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {

    private static int nextNumber = 1;

    private final int number;

    private final Map<Product, Integer> products;

    public Invoice() {
        number = nextNumber++;
        products = new HashMap<>();
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        products.merge(product, quantity, Integer::sum);
    }

    public BigDecimal getSubtotal() {
        BigDecimal sum = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            sum = sum.add(
                    entry.getKey()
                            .getPrice()
                            .multiply(BigDecimal.valueOf(entry.getValue()))
            );
        }

        return sum;
    }

    public BigDecimal getTax() {
        BigDecimal tax = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            BigDecimal productTax = product.getPrice()
                    .multiply(product.getTaxPercent())
                    .multiply(BigDecimal.valueOf(quantity));

            tax = tax.add(productTax);
        }

        return tax;
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getTax());
    }

    public int getNumber() {
        return number;
    }
}
