package org.example.cnjava_milkteastore.frontend.session;

import javax.swing.*;
import java.io.*;

public class Session {

    public void deleteSessionFile() {
        File sessionFile = new File("D:\\CNJAVA_MilkTeaStore\\src\\main\\java\\org\\example\\cnjava_milkteastore\\frontend\\session\\session.txt");

        if (sessionFile.exists()) {
            boolean deleted = sessionFile.delete();
            if (deleted) {
                System.out.println("✅ Đã xóa file session.txt");
            } else {
                System.err.println("❌ Không thể xóa file session.txt");
            }
        } else {
            System.out.println("⚠️ File session.txt không tồn tại");
        }
    }


    // Hàm kiểm tra role từ file account_info.txt (có thể gọi từ file khác)
    public boolean isAdminFromAccountInfo() {
        File accountInfoFile = new File("D:\\CNJAVA_MilkTeaStore\\src\\main\\java\\org\\example\\cnjava_milkteastore\\frontend\\session\\session.txt");
        if (!accountInfoFile.exists()) {
            return false; // File không tồn tại, coi như không phải admin
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(accountInfoFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Vai trò: ")) {
                    String role = line.substring("Vai trò: ".length()).trim();
                    return "ADMIN".equalsIgnoreCase(role); // Trả về true nếu là ADMIN
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void clearAccountInfoFile() {
        File accountInfoFile = new File("D:\\CNJAVA_MilkTeaStore\\src\\main\\java\\org\\example\\cnjava_milkteastore\\frontend\\session\\session.txt");
        if (accountInfoFile.exists()) {
            try (FileWriter writer = new FileWriter(accountInfoFile, false)) {
                writer.write(""); // Ghi đè file với nội dung rỗng
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Hàm kiểm tra xem file account_info.txt có nội dung hay không
    public boolean isAccountInfoFileEmpty() {
        File accountInfoFile = new File("D:\\CNJAVA_MilkTeaStore\\src\\main\\java\\org\\example\\cnjava_milkteastore\\frontend\\session\\session.txt");
        if (!accountInfoFile.exists()) {
            return true; // File không tồn tại, coi như rỗng
        }
        return accountInfoFile.length() == 0; // Kiểm tra kích thước file
    }

}
