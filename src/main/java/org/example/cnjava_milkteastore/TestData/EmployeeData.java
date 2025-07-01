package org.example.cnjava_milkteastore.TestData;

import org.example.cnjava_milkteastore.backend.employee.dto.EmployeeDTO;
import org.example.cnjava_milkteastore.backend.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class EmployeeData {

    @Autowired
    private EmployeeService employeeService;

    public void insertMockEmployees() {
        String[] ho = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Phan", "Vũ", "Bùi", "Đặng", "Đỗ"};
        String[] tenDemNam = {"Văn", "Hữu", "Minh", "Xuân", "Quang", "Thế", "Nhật", "Trọng"};
        String[] tenNam = {"Anh", "Bình", "Cường", "Dũng", "Giang", "Hải", "Khoa", "Long", "Nam", "Phong"};

        String[] tenDemNu = {"Thị", "Ngọc", "Thuỳ", "Diệu", "Thanh", "Bảo", "Lan"};
        String[] tenNu = {"Anh", "Bình", "Châu", "Dung", "Hà", "Hương", "Mai", "Nga", "Trang", "Yến"};

        String[] diaChi = {
                "123 Lê Lợi, Q1", "456 Trần Hưng Đạo, Q5", "789 Nguyễn Trãi, Q10",
                "321 Hai Bà Trưng, Q3", "654 Nguyễn Đình Chiểu, Q1", "777 CMT8, Q10",
                "888 Phan Xích Long, Q.Phú Nhuận", "999 Hoàng Văn Thụ, Q.Tân Bình",
                "111 Nguyễn Huệ, Q1", "222 Lý Tự Trọng, Q1"
        };

        List<EmployeeDTO> employees = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            String gender = random.nextBoolean() ? "Nam" : "Nữ";

            String hoTen;
            if (gender.equals("Nam")) {
                hoTen = ho[random.nextInt(ho.length)] + " " +
                        tenDemNam[random.nextInt(tenDemNam.length)] + " " +
                        tenNam[random.nextInt(tenNam.length)];
            } else {
                hoTen = ho[random.nextInt(ho.length)] + " " +
                        tenDemNu[random.nextInt(tenDemNu.length)] + " " +
                        tenNu[random.nextInt(tenNu.length)];
            }

            String address = diaChi[random.nextInt(diaChi.length)];

            LocalDate dob = LocalDate.of(
                    1990 + random.nextInt(12), // năm sinh từ 1990–2001
                    1 + random.nextInt(12),    // tháng
                    1 + random.nextInt(28)     // ngày
            );

            String phone = "09" + (random.nextInt(100000000) + 10000000); // SĐT 10 số

            employees.add(new EmployeeDTO(0, "", hoTen, address, gender, dob, phone));
        }

        for (EmployeeDTO employee : employees) {
            employeeService.createOrUpdate(employee);
            System.out.println(employee.getName());
        }

        System.out.println("✅ Đã thêm 100 nhân viên thường (không bao gồm admin) thành công!");
    }

    public void insertEmployees() {
        List<EmployeeDTO> employees = List.of(
                // Admin
//                new EmployeeDTO(0, "ADM001", "Nguyễn Văn Linh", "1 Quang Trung, Q.Gò Vấp", "Nam", LocalDate.of(1988, 2, 14), "0911234567"),
//                new EmployeeDTO(0, "ADM002", "Trần Thị Ma", "2 Nguyễn Văn Cừ, Q5", "Nữ", LocalDate.of(1991, 10, 9), "0912345678"),
                new EmployeeDTO(0, "ADMIN00", "Le Viet Anh", "3 Dương Bá Trạc, Q8", "Nam", LocalDate.of(1985, 6, 3), "0913456789")
        );

        for (EmployeeDTO employee : employees) {
            employeeService.createOrUpdate(employee);
        }
    }
}
