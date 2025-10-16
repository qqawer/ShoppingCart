package com.example.ShoppingCart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.model.UserAddress;
import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.RegisterRequest;
import com.example.ShoppingCart.pojo.dto.UpdateUserRequest;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import com.example.ShoppingCart.repository.UserAddressRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * User Controller - Handles user authentication, profile management, and address operations
 * Main responsibilities: Login/logout, registration, profile viewing/editing, address management
 */
@Controller
public class UserController {
    @Autowired
    private UserInterface userService;

    @Autowired
    private UserAddressRepository userAddressRepository;

    /**
     * Delete user address
     * Logic: Verify user is logged in, check address ownership, delete from database
     */
    @PostMapping("/user/address/delete/{addressId}")
    public String deleteAddress(@PathVariable String addressId, HttpSession session, Model model,
                                RedirectAttributes redirectAttributes) {
        // check whether the user is logged in
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // get the address to be deleted
            UserAddress address = userAddressRepository.findById(addressId).orElse(null);

            // check whether the address exists and belongs to the current user
            if (address != null && address.getUser().getUserId().equals(userId)) {
                userAddressRepository.delete(address);
                redirectAttributes.addFlashAttribute("successMessage", "address deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "address does not exist or does not belong to the current user");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "failed to delete address: " + e.getMessage());
        }

        return "redirect:/user/addresses";
    }

    /**
     * Display login page
     * Logic: Create empty LoginRequest object for form binding
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "user/login";
    }

    /**
     * Display user's address list page
     * Logic: Check login status, retrieve all addresses for current user
     */
    @GetMapping("/user/addresses")
    public String addressesPage(HttpSession session, Model model) {
        // check whether the user is logged in
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }

        // get all the addresses of the user
        List<UserAddress> addresses = userAddressRepository.findByUser_UserId(userId);
        model.addAttribute("addresses", addresses);

        return "user/addresses";
    }

    /**
     * Process user login
     * Logic: Validate input, authenticate user, save to session, redirect based on role (admin/customer)
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest request,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "the input information is incorrect, please check");
            return "user/login";
        }

        try {
            UserInfoDTO userInfo = userService.login(request, session, bindingResult);
            // redirect to different pages based on the user role
            if ("ADMIN".equals(userInfo.getRole())) {
                // admin redirect to the products page
                return "redirect:/#/products";
            } else {
                // customer redirect to the product list page
                return "redirect:/products/lists";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "user/login";
        }
    }



    /**
     * User logout
     * Logic: Invalidate session, redirect to login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/login";
    }

    /**
     * Display user profile page
     * Logic: Retrieve current user from session, show profile information
     */
    @GetMapping("/user/profile")
    public String getUserProfile(HttpSession session, Model model) {
        try {
            UserInfoDTO userInfo = userService.getCurrentUser(session);
            model.addAttribute("userInfo", userInfo);
            return "user/profile";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    /**
     * Display registration page
     * Logic: Create empty RegisterRequest object for form binding
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "user/register";
    }

    /**
     * Process user registration
     * Logic: Validate input, check duplicates, create new user account
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "the input information is incorrect, please check");
            return "user/register";
        }

        try {
            userService.register(request);
            model.addAttribute("success", "registration successful, please login");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "user/register";
        }
    }

    /**
     * Display edit profile page
     * Logic: Retrieve current user info, prepare form for editing
     */
    @GetMapping("/user/edit")
    public String editProfilePage(HttpSession session, Model model) {
        try {
            UserInfoDTO userInfo = userService.getCurrentUser(session);
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("updateRequest", new UpdateUserRequest());
            return "user/edit-profile";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    /**
     * Process user profile update
     * Logic: Validate input, update user info (username/avatar/password/address), save to session
     */
    @PostMapping("/user/update")
    public String updateProfile(@Valid @ModelAttribute("updateRequest") UpdateUserRequest request,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model) {
        try {
            UserInfoDTO currentUser = userService.getCurrentUser(session);
            model.addAttribute("userInfo", currentUser);  // key: put back

            if (bindingResult.hasErrors()) {
                model.addAttribute("error", "the input information is incorrect, please check");
                model.addAttribute("updateRequest", request);
                return "user/edit-profile";
            }

            UserInfoDTO updatedUser = userService.updateUserInfo(currentUser.getUserId(), request);
            session.setAttribute(SessionConstant.CURRENT_USER, updatedUser);
            model.addAttribute("success", "information updated successfully");
            return "redirect:/user/profile";
        } catch (Exception e) {
            try {
                model.addAttribute("userInfo", userService.getCurrentUser(session));
            } catch (Exception ignored) {}
            model.addAttribute("updateRequest", request);
            model.addAttribute("error", e.getMessage());
            return "user/edit-profile";
        }
    }
    /**
     * Set default shipping address
     * Logic: Unset all default flags, set selected address as default
     */
    @PostMapping("/user/address/default/{addressId}")
    public String setDefaultAddress(@PathVariable String addressId, HttpSession session) {
        try {
            UserInfoDTO userInfo = userService.getCurrentUser(session);
            // first set all addresses to non-default
            List<UserAddress> addresses = userAddressRepository.findByUser_UserId(userInfo.getUserId());
            for (UserAddress address : addresses) {
                address.setDefault(false);
                userAddressRepository.save(address);
            }
            // set the selected address to default
            UserAddress selectedAddress = userAddressRepository.findById(addressId).orElse(null);
            if (selectedAddress != null) {
                selectedAddress.setDefault(true);
                userAddressRepository.save(selectedAddress);
            }

            return "redirect:/user/addresses";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }
}
