package org.example.cnjava_milkteastore.TestData;

import org.example.cnjava_milkteastore.backend.invoice.dto.InvoiceDTO;
import org.example.cnjava_milkteastore.backend.invoice.service.InvoiceService;
import org.example.cnjava_milkteastore.backend.invoicedetail.dto.InvoiceDetailDTO;
import org.example.cnjava_milkteastore.backend.invoicedetail.service.InvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class InvoiceData {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    public void insertInvoicesWithDetails() {
        Random random = new Random();
        int invoiceId = 0;
        int invoiceCount = 0;

        for (int year = 2024; year <= 2025; year++) {
            int startMonth = (year == 2024) ? 1 : 1;
            int endMonth = (year == 2024) ? 12 : 5;

            for (int month = startMonth; month <= endMonth; month++) {
                int numInvoices = (year == 2024) ? 10 : 6;
                for (int i = 1; i <= numInvoices; i++) {
                    invoiceCount++;
                    int day = Math.min(i, LocalDate.of(year, month, 1).lengthOfMonth());
                    LocalDateTime date = LocalDate.of(year, month, day).atTime(random.nextInt(10) + 8, 0);

                    int customerID = random.nextInt(100) + 1;
                    int employeeID = random.nextInt(100) + 1;
                    InvoiceDTO invoice = getInvoiceDTO(customerID, employeeID, date);

                    // Lưu invoice
                    InvoiceDTO savedInvoice = invoiceService.createOrUpdate(invoice);
                    invoiceId = savedInvoice.getID();

                    // Tạo các chi tiết hóa đơn
                    int numDetails = random.nextInt(5) + 1;
                    float total = 0;
                    int totalDrinkQty = 0;
                    int totalFoodQty = 0;
                    float totalDrinkPrice = 0;
                    float totalFoodPrice = 0;

                    for (int d = 0; d < numDetails; d++) {
                        int productId = random.nextInt(15) + 1; // giả định có 15 sản phẩm đầu
                        int quantity = random.nextInt(3) + 1;

                        String[] names = {
                                "Trà Sữa Truyền Thống", "Trà Sữa Đường Đen", "Trà Sữa Matcha",
                                "Trà Đào Cam Sả", "Trà Vải Hạt Chia", "Trà Xoài Nhiệt Đới",
                                "Trân Châu Đen", "Thạch Dừa", "Pudding Trứng",
                                "Nước Ép Cam", "Nước Ép Dưa Hấu", "Bánh Flan",
                                "Bánh Mì Bơ Sữa", "Combo Trà Sữa + Flan", "Combo Trà Đào + Trân Châu"
                        };
                        String[] types = {
                                "Trà Sữa", "Trà Sữa", "Trà Sữa",
                                "Trà Trái Cây", "Trà Trái Cây", "Trà Trái Cây",
                                "Topping", "Topping", "Topping",
                                "Nước Ép", "Nước Ép", "Bánh Ngọt",
                                "Bánh Ngọt", "Combo", "Combo"
                        };
                        float[] prices = {
                                32000, 35000, 37000, 36000, 34000, 38000,
                                5000, 6000, 7000, 30000, 28000,
                                15000, 20000, 45000, 40000
                        };

                        float price = prices[productId - 1];

                        InvoiceDetailDTO detail = new InvoiceDetailDTO(
                                0, invoiceId, productId, quantity, price,
                                names[productId - 1], types[productId - 1]
                        );

                        invoiceDetailService.createOrUpdate(detail);

                        total += price * quantity;
                        if (types[productId - 1].equals("Trà Sữa") || types[productId - 1].equals("Trà Trái Cây") || types[productId - 1].equals("Nước Ép")) {
                            totalDrinkQty += quantity;
                            totalDrinkPrice += price * quantity;
                        } else {
                            totalFoodQty += quantity;
                            totalFoodPrice += price * quantity;
                        }
                    }

                    // Cập nhật lại invoice với tổng
                    savedInvoice.setTotalPrice(total);
                    savedInvoice.setTotalQuantityDrink(totalDrinkQty);
                    savedInvoice.setTotalQuantityFood(totalFoodQty);
                    savedInvoice.setTotalPriceDrink(totalDrinkPrice);
                    savedInvoice.setTotalPriceFood(totalFoodPrice);
                    invoiceService.createOrUpdate(savedInvoice);
                }
            }
        }

        System.out.println("✅ Đã tạo " + invoiceCount + " hóa đơn kèm chi tiết.");
    }

    private static InvoiceDTO getInvoiceDTO(int customerID, int employeeID, LocalDateTime date) {
        String invoiceCode = "";

        InvoiceDTO invoice = new InvoiceDTO();
        invoice.setID(0);
        invoice.setInvoiceCode(invoiceCode);
        invoice.setCustomerID(customerID);
        invoice.setEmployeeID(employeeID);
        invoice.setDateCreated(date);

        // Tạm để tổng giá và số lượng là 0 — sẽ cập nhật sau
        invoice.setTotalPrice(0);
        invoice.setTotalQuantityDrink(0);
        invoice.setTotalQuantityFood(0);
        invoice.setTotalPriceDrink(0);
        invoice.setTotalPriceFood(0);
        return invoice;
    }
}
