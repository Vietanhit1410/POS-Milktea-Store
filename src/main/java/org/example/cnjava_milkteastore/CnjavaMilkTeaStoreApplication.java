package org.example.cnjava_milkteastore;

import org.example.cnjava_milkteastore.TestData.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CnjavaMilkTeaStoreApplication {

    public static void main(String[] args) {
        ApplicationContext context =
                SpringApplication.run(CnjavaMilkTeaStoreApplication.class, args);
//        ProductData productData = (ProductData) context.getBean("productData");
//        productData.insertMock100Products();

//        EmployeeData employeeData = (EmployeeData) context.getBean("employeeData");
//        employeeData.insertEmployees();

//        AccountData accountData = (AccountData) context.getBean("accountData");
//        accountData.insertAccounts1();
//
//        InvoiceData invoiceData = (InvoiceData) context.getBean("invoiceData");
//        invoiceData.insertInvoicesWithDetails();

//        CustomerData customerData = (CustomerData) context.getBean("customerData");
//        customerData.insertCustomers();

    }

}
