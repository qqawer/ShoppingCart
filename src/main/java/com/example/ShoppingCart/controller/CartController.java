package com.example.ShoppingCart.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.SessionConstant;

import jakarta.servlet.http.HttpSession;

/**
 * Cart Controller - Handles shopping cart operations
 * Main responsibilities: Display cart, add/remove items, update quantities
 */
@Controller
public class CartController {
    @Autowired
    private CartInterface cartInterface;

    /**
     * Display the cart page with all items
     * Logic: Retrieve user's cart items, calculate total price, check stock availability
     */
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        // Step 1: Get user ID from session, use test user if not logged in
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            // if not login, use the test userID
            userId = "testUser123";
        }

        // Step 2: Retrieve all cart items for this user
        List<CartRecord> cartItems = cartInterface.getCartItemsByUserId(userId);

        // Step 3: Calculate total price and check stock for each item
        BigDecimal totalPrice = BigDecimal.ZERO;
        boolean hasInsufficientStock = false;
        for (CartRecord item : cartItems) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(new BigDecimal(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
            int availableStock = cartInterface.getProductInventory(item.getProduct().getProductId());
            boolean insufficientStock = item.getQuantity() > availableStock;
            item.setInsufficientStock(insufficientStock);
            if (insufficientStock) {
                item.setAvailableStock(availableStock);
                hasInsufficientStock = true;
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("userId", userId);
        model.addAttribute("hasInsufficientStock", hasInsufficientStock);

        return "product/cart";
    }

    /**
     * Update product quantity via input box
     * Logic: Validate new quantity, check stock availability, update cart record
     */
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam String productId,
                                 @RequestParam Integer quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            return "redirect:/login";
        }

        try {
            CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
            if (existingCartRecord != null) {
                // verify the quantity of input
                if (quantity <= 0) {
                    redirectAttributes.addFlashAttribute("errorMessage", "the quantity of product must be greater than 0");
                    return "redirect:/cart";
                }

                // check the stock
                if (!cartInterface.checkInventory(productId, quantity)) {
                    int availableStock = cartInterface.getProductInventory(productId);
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "stock is not enough! the remaining stock is " + availableStock + " pieces");
                    return "redirect:/cart";
                }

                // calculate the quantity to add or subtract
                int quantityDiff = quantity - existingCartRecord.getQuantity();
                cartInterface.updateQuantity(existingCartRecord, quantityDiff);
                redirectAttributes.addFlashAttribute("successMessage", "the quantity of product has been updated");
            }
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cart";
    }

    /**
     * Increase product quantity by 1
     * Logic: Check if stock allows increment, update quantity if valid
     */
    @GetMapping("/cart/increase")
    public String increaseQuantity(@RequestParam String productId, HttpSession session, RedirectAttributes redirectAttributes) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            return "redirect:/login";
        }

        try {
            CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
            if (existingCartRecord != null) {
                //test whether can add the stock,
                int newQuantity = existingCartRecord.getQuantity() + 1;
                //add the error message
                if (!cartInterface.checkInventory(productId, newQuantity)) {
                    int availableStock = cartInterface.getProductInventory(productId);
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "stock is not enough! the remaining stock is " + availableStock + " pieces, please reduce the quantity");
                    return "redirect:/cart";
                }
                cartInterface.updateQuantity(existingCartRecord, 1);

            }
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * Decrease product quantity by 1
     * Logic: Prevent quantity from going below 1, no stock check needed for decrease
     */
    @GetMapping("/cart/decrease")
    public String decreaseQuantity(@RequestParam String productId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            return "redirect:/login";
        }

        try {
            CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
            if (existingCartRecord != null) {
                int currentQuantity = existingCartRecord.getQuantity();

                // if the current quantity is already 1, it cannot be reduced anymore
                if (currentQuantity <= 1) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "the quantity of product has reached the minimum value, it cannot be reduced anymore");
                    return "redirect:/cart";
                }

                // normal decrease 1, no check the stock (decrease operation does not need to check the stock)
                cartInterface.updateQuantityWithoutStockCheck(existingCartRecord, -1);
                redirectAttributes.addFlashAttribute("successMessage", "the quantity of product has been reduced");
            }
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cart";
    }

    /**
     * Remove product from cart
     * Logic: Set quantity to negative to trigger deletion in service layer
     */
    @GetMapping("/cart/remove")
    public String removeItem(@RequestParam String productId, HttpSession session) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            return "redirect:/login";
        }

        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
        if (existingCartRecord != null) {
            // pass a negative number to make the quantity 0 or less, trigger the delete logic
            cartInterface.updateQuantity(existingCartRecord, -existingCartRecord.getQuantity());
        }
        return "redirect:/cart";
    }

    /**
     * Add product to cart
     * Logic: Check if item exists in cart, verify total quantity against stock,
     * either update existing item or create new cart record
     */
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam String productId,
            @RequestParam(required = false, defaultValue = "1") Integer quantity,
            @RequestParam(required = false, defaultValue = "0") Integer currentPage,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            // if not login, redirect to the login page
            return "redirect:/login";
        }

        try {
            CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
            int totalQuantity = quantity;

            if (existingCartRecord != null) {
                totalQuantity += existingCartRecord.getQuantity();
            }

            // check the stock whether it is enough
            if (!cartInterface.checkInventory(productId, totalQuantity)) {
                int availableStock = cartInterface.getProductInventory(productId);
                redirectAttributes.addFlashAttribute("errorMessage",
                        "stock is not enough! the remaining stock is " + availableStock + " pieces, cannot add to the cart");
                return "redirect:/products/lists?page=" + currentPage;
            }

            if (existingCartRecord != null) {
                cartInterface.updateQuantity(existingCartRecord, quantity);
            } else {
                cartInterface.createCartItem(userId, productId, quantity);
            }
            redirectAttributes.addFlashAttribute("successMessage", "the product has been successfully added to the cart");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/products/lists?page=" + currentPage;
    }
}