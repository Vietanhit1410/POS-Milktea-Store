package org.example.cnjava_milkteastore.TestData;

import org.example.cnjava_milkteastore.backend.account.dto.AccountDTO;
import org.example.cnjava_milkteastore.backend.account.service.AccountService;
import org.example.cnjava_milkteastore.backend.employee.dto.EmployeeDTO;
import org.example.cnjava_milkteastore.backend.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

@Component
public class AccountData {

    @Autowired
    private AccountService accountService;
    @Autowired
    private EmployeeService employeeService;

    public void insertAccounts() {
        List<AccountDTO> accounts = new ArrayList<>();
        List<EmployeeDTO> employees = employeeService.getAll();
        for (EmployeeDTO em : employees) {
            String username = String.format("nv%03d",em.getID()); // nv001 → nv100
            String password = "123456";
            String role = "EMPLOYEE";
            accounts.add(new AccountDTO(0, username, password, role, em.getID()));
        }

        for (AccountDTO account : accounts) {
            accountService.createOrUpdate(account);
        }
    }

    public void insertAccounts1() {
            accountService.createOrUpdate(new AccountDTO(0, "admin", "123456", "ADMIN", 552));;

    }
}
