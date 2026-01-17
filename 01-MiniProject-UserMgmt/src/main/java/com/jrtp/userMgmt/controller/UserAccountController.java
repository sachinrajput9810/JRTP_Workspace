package com.jrtp.userMgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jrtp.userMgmt.entities.UserAccount;
import com.jrtp.userMgmt.service.UserAccountService;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/view-users")
    public String getAllUserAccounts(Model model) {
        List<UserAccount> userAccounts = userAccountService.getAllUserAccounts();
        model.addAttribute("users", userAccounts);
        return "view-users";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new UserAccount());
        return "index";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new UserAccount());
        return "index";
    }

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute("user") UserAccount userAccount, Model model) {
        String msg = userAccountService.saveOrUpdateUserAccount(userAccount);
        model.addAttribute("msg", msg);
        model.addAttribute("user", new UserAccount());
        return "index";
    }

    @GetMapping("/edit-user")
    public String showEditUserForm(@RequestParam("id") Integer id, Model model) {
        UserAccount userAccount = userAccountService.getUserAccountById(id);
        model.addAttribute("user", userAccount);
        return "index";
    }

    @GetMapping("/delete-user")
    public String deleteUser(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        UserAccount user = userAccountService.getUserAccountById(id);
        String name = user != null ? user.getFullName() : "Unknown";

        userAccountService.deleteUserAccountById(id);
        redirectAttributes.addFlashAttribute("msg", "User " + name + " (ID: " + id + ") is deleted successfully.");
        return "redirect:/view-users";
    }

    @GetMapping("/update-status")
    public String updateUserStatus(@RequestParam("id") Integer id, @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {
        UserAccount user = userAccountService.getUserAccountById(id);
        String name = user != null ? user.getFullName() : "Unknown";
        String action = "Y".equals(status) ? "activated" : "deactivated";

        userAccountService.updateUserAccountStatus(id, status);
        redirectAttributes.addFlashAttribute("msg",
                "User " + name + " (ID: " + id + ") is " + action + " successfully.");
        return "redirect:/view-users";
    }
}
