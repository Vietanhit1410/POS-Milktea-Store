package org.example.cnjava_milkteastore.TestData;


import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.frontend.swingworker.ApiWorker;
import org.example.cnjava_milkteastore.backend.product.dto.ProductDTO;
import org.example.cnjava_milkteastore.backend.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class ProductData {
    @Autowired
    ProductService productService;




    public void insertMock100Products() {
        List<ProductDTO> products = new ArrayList<>();

        String[] categories = {
                "Trà Sữa","Đồ ngọt","Recommend","Best Seller","Trà Trái Cây", "Nước Ép", "Topping", "Bánh Ngọt", "Combo", "Cà Phê", "Sinh Tố", "Sữa Chua", "Soda"
        };

        String[] adjectives = {
                "Truyền Thống", "Đặc Biệt", "Đường Đen", "Matcha", "Xoài", "Dâu", "Chanh Leo", "Cà Phê", "Socola", "Bạc Hà",
                "Nhiệt Đới", "Thảo Mộc", "Hạnh Nhân", "Kem Cheese", "Trứng Muối"
        };

        String[] baseNames = {
                "Trà Sữa", "Trà Trái Cây", "Nước Ép", "Pudding", "Thạch", "Trân Châu", "Sữa Chua", "Sinh Tố", "Cà Phê", "Soda"
        };

        String[] imageFilenames = {
                "trasua_truyenthong.jpg", "trasua_duongden.jpg", "trasua_matcha.jpg",
                "tradao_camsa.jpg", "travail_hatchia.jpg", "traxoai_nhietdoi.jpg",
                "tranchau_den.jpg", "thach_dua.jpg", "pudding_trung.jpg",
                "nuocep_cam.jpg", "nuocep_duahau.jpg",
                "banh_flan.jpg", "banhmi_bosua.jpg",
                "combo_ts_flan.jpg", "combo_tradao_tranchau.jpg"
        };

        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            String category = categories[random.nextInt(categories.length)];
            String base = baseNames[random.nextInt(baseNames.length)];
            String adj = adjectives[random.nextInt(adjectives.length)];

            String productName = base + " " + adj ;
            String image = imageFilenames[i % imageFilenames.length];

            int quantity = 20 + random.nextInt(200); // 20 -> 219
            float price = 15000f + random.nextInt(50) * 500; // 15000 -> 40000
            int point = 1 + random.nextInt(5); // 1 -> 5

            products.add(new ProductDTO(0, "", quantity, category, price, point, productName, image));
        }

        for (ProductDTO product : products) {
            productService.createOrUpdate(product);
            System.out.println("✔️ " + product.getProductName());
        }

        System.out.println("✅ Đã thêm 100 sản phẩm mẫu với tên và loại đa dạng!");
    }



    public void insertProduct() {

        List<ProductDTO> products = new ArrayList<>();

        products.add(new ProductDTO(0, "", 100, "Trà Sữa", 32000f, 3, "Trà Sữa Truyền Thống", "trasua_truyenthong.jpg"));
        products.add(new ProductDTO(0, "", 80, "Trà Sữa", 35000f, 4, "Trà Sữa Đường Đen", "trasua_duongden.jpg"));
        products.add(new ProductDTO(0, "", 70, "Trà Sữa", 37000f, 4, "Trà Sữa Matcha", "trasua_matcha.jpg"));
        products.add(new ProductDTO(0, "", 60, "Trà Trái Cây", 36000f, 4, "Trà Đào Cam Sả", "tradao_camsa.jpg"));
        products.add(new ProductDTO(0, "", 50, "Trà Trái Cây", 34000f, 3, "Trà Vải Hạt Chia", "travail_hatchia.jpg"));
        products.add(new ProductDTO(0, "", 55, "Trà Trái Cây", 38000f, 4, "Trà Xoài Nhiệt Đới", "traxoai_nhietdoi.jpg"));
        products.add(new ProductDTO(0, "", 200, "Topping", 5000f, 1, "Trân Châu Đen", "tranchau_den.jpg"));
        products.add(new ProductDTO(0, "", 150, "Topping", 6000f, 1, "Thạch Dừa", "thach_dua.jpg"));
        products.add(new ProductDTO(0, "", 120, "Topping", 7000f, 1, "Pudding Trứng", "pudding_trung.jpg"));
        products.add(new ProductDTO(0, "", 40, "Nước Ép", 30000f, 2, "Nước Ép Cam", "nuocep_cam.jpg"));
        products.add(new ProductDTO(0, "", 50, "Nước Ép", 28000f, 2, "Nước Ép Dưa Hấu", "nuocep_duahau.jpg"));
        products.add(new ProductDTO(0, "", 30, "Bánh Ngọt", 15000f, 1, "Bánh Flan", "banh_flan.jpg"));
        products.add(new ProductDTO(0, "", 25, "Bánh Ngọt", 20000f, 1, "Bánh Mì Bơ Sữa", "banhmi_bosua.jpg"));
        products.add(new ProductDTO(0, "", 20, "Combo", 45000f, 5, "Combo Trà Sữa + Flan", "combo_ts_flan.jpg"));
        products.add(new ProductDTO(0, "", 15, "Combo", 40000f, 5, "Combo Trà Đào + Trân Châu", "combo_tradao_tranchau.jpg"));

        for (ProductDTO product : products) {
            productService.createOrUpdate(product);
        }

    }
    public void fetchProductsFromApi() {
        String apiUrl = "http://localhost:8080/api/products";

        ApiWorker<List<ProductDTO>> worker = new ApiWorker<>(
                apiUrl,
                "GET",
                null,
                new TypeReference<>() {
                },
                products -> {
                    // onSuccess: xử lý danh sách sản phẩm
                    for (ProductDTO product : products) {
                        System.out.println("✔️ " + product.getProductName());
                        // Bệ Hạ có thể thêm vào bảng, danh sách, UI tùy ý
                    }
                },
                () -> {
                    // onStart: có thể show loading
                    System.out.println("🔄 Đang tải danh sách sản phẩm...");
                },
                () -> {
                    // onFinish: tắt loading
                    System.out.println("✅ Tải xong sản phẩm!");
                },
                e -> {
                    System.err.println("❌ Lỗi khi gọi API: " + e.getMessage());
                }
        );

        worker.execute(); // Bắt đầu chạy SwingWorker
    }

}


