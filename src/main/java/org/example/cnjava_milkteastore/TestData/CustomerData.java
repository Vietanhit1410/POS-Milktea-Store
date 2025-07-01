package org.example.cnjava_milkteastore.TestData;

import org.example.cnjava_milkteastore.backend.customer.dto.CustomerDTO;
import org.example.cnjava_milkteastore.backend.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CustomerData {

    @Autowired
    private CustomerService customerService;

    public void insertCustomers() {
        List<CustomerDTO> customers = new ArrayList<>();
        Random random = new Random();

        String[] ho = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Vũ", "Đặng", "Bùi", "Đỗ", "Hồ"};
        String[] tenLot = {"Văn", "Thị", "Hữu", "Minh", "Thanh", "Ngọc", "Xuân", "Đức", "Gia", "Quốc"};
        String[] ten = {"An", "Bình", "Cường", "Dũng", "Hà", "Hương", "Khoa", "Linh", "Mai", "Nam", "Oanh", "Phúc", "Quang", "Trang", "Vy"};

        for (int i = 1; i <= 100; i++) {
            String customerName = ho[random.nextInt(ho.length)] + " " +
                    tenLot[random.nextInt(tenLot.length)] + " " +
                    ten[random.nextInt(ten.length)];
            String customerPhone = "09" + String.format("%08d", 10000000 + random.nextInt(89999999));
            int points = random.nextInt(6); // 0 -> 100

            customers.add(new CustomerDTO(0,"", customerName, customerPhone, points));
        }

        for (CustomerDTO customer : customers) {
            customerService.createOrUpdate(customer);
            System.out.println(customer.getCustomerName());
        }

        System.out.println("✅ Đã thêm 100 khách hàng thành công!");
    }
}
