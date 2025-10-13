package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.pojo.dto.ProductCreateDTO;
import com.example.ShoppingCart.pojo.dto.ProductUpdateDTO;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.SessionConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // 或你前端 dev-server 的端口
@Controller
public class ProductController {

    @Autowired
    private ProductInterface pservice;

    @Autowired
    private CartInterface cartInterface;

    //Query all products.
    @GetMapping("products/lists")
    public String getAllProducts(@PageableDefault(size = 12, sort = "price") Pageable pageable,
                                 Model model, HttpSession session) {


        Page<Product> products=pservice.getAllProductsByStatus(pageable);
        model.addAttribute("page", products);

        // 获取购物车商品数量
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        int cartCount = 0;
        if (userId != null) {
            cartCount = cartInterface.getCartItemsByUserId(userId).size();
        }
        model.addAttribute("cartCount", cartCount);

        return "product/lists";
    }



    // Query a single product based on the product ID
    @GetMapping("/products/{productId}")
    public String getProductById(@PathVariable String productId,Model model,HttpSession session) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_CANNOT_BE_NULL,"productId can not be null");
        }
        Product product= pservice.getProductById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXIST, productId);
        }
        model.addAttribute("product",product);
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        int cartCount = 0;
        if (userId != null) {
            cartCount = cartInterface.getCartItemsByUserId(userId).size();
        }
        model.addAttribute("cartCount", cartCount);
        return "product/product-detail";
    }

    @GetMapping("/products/search")
    public String searchProductsByName(@RequestParam(required = false) String productName,
                                       @RequestParam(required = false) String sort,
                                       @PageableDefault(size = 12) Pageable pageable,
                                       Model model,
                                       HttpSession session) {
        // 保持购物车数量显示
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        int cartCount = 0;
        if (userId != null) {
            cartCount = cartInterface.getCartItemsByUserId(userId).size();
        }
        model.addAttribute("cartCount", cartCount);

        // 处理排序逻辑
        Pageable sortedPageable = handleSorting(sort, pageable);
        String sortMessage = getSortMessage(sort);

        Page<Product> products;
        if (productName == null || productName.isEmpty()) {
            // 显示所有商品
            products = pservice.getAllProductsByStatus(sortedPageable);

            // 如果有排序操作，显示排序消息
            if (sort != null && !sort.isEmpty()) {
                model.addAttribute("successMessage", "商品已按" + sortMessage + "排列");
            } else {
                model.addAttribute("errorMessage", "请输入商品名称！");
            }
        }
        else {
            productName = productName.strip();
            products = pservice.searchAvailableProductsByName(productName, sortedPageable);

            if (products.isEmpty()) {
                model.addAttribute("errorMessage", "抱歉，没有找到与 '" + productName + "' 相关的商品");
                // 显示所有商品
                products = pservice.getAllProductsByStatus(sortedPageable);
            }
            else {
                String message = "找到 " + products.getTotalElements() + " 个相关商品";
                // 如果有排序，添加排序信息
                if (sort != null && !sort.isEmpty()) {
                    message += "，并按" + sortMessage + "排列";
                }
                model.addAttribute("successMessage", message);
            }
        }

        model.addAttribute("page", products);
        model.addAttribute("searchKeyword", productName);
        model.addAttribute("currentSort", sort); // 用于前端显示当前排序状态

        return "product/lists";
    }

    // 处理排序的辅助方法
    private Pageable handleSorting(String sortParam, Pageable originalPageable) {
        if (sortParam == null || sortParam.isEmpty()) {
            return originalPageable;
        }

        Sort.Direction direction;
        String field;

        switch (sortParam) {
            case "price_asc":
                direction = Sort.Direction.ASC;
                field = "price";
                break;
            case "price_desc":
                direction = Sort.Direction.DESC;
                field = "price";
                break;
            default:
                return originalPageable;
        }

        Sort sort = Sort.by(direction, field);
        return PageRequest.of(originalPageable.getPageNumber(),
                originalPageable.getPageSize(),
                sort);
    }

    // 生成排序消息的辅助方法
    private String getSortMessage(String sort) {
        if (sort == null || sort.isEmpty()) {
            return "默认顺序";
        }

        switch (sort) {
            case "price,asc":
                return "价格升序";
            case "price,desc":
                return "价格降序";
            default:
                return "指定顺序";
        }
    }


    @GetMapping("/api/products/lists")
    @ResponseBody
    public ResponseMessage<Page<Product>> getApiProduct(@PageableDefault(size = 12) Pageable pageable){
        Page<Product> products=pservice.getAllProducts(pageable);
        if (products.isEmpty()) {
            throw new BusinessException(ErrorCode.PRODUCT_LIST_EMPTY);
        }
        return ResponseMessage.success(products);
    }

    @GetMapping("/api/products/{productId}")
    @ResponseBody
    public ResponseMessage<Product> getApiProductById(@PathVariable String productId) {
        Product product= pservice.getProductById(productId);
        if (product==null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXIST);
        }
        return ResponseMessage.success(product);
    }

    @PostMapping("/api/addProduct")
    @ResponseBody
    public ResponseMessage<Product> createProduct( @Valid @RequestBody ProductCreateDTO createDTO) {
        Product createdProduct = pservice.createProduct(createDTO);
        return ResponseMessage.success(createdProduct);
    }
    @PostMapping("/api/updateProduct")
    @ResponseBody
    public ResponseMessage<Product> editProduct( @Valid @RequestBody ProductUpdateDTO updateDTO) {
        Product createdProduct = pservice.updateProduct(updateDTO);
        return ResponseMessage.success(createdProduct);
    }
    @PostMapping("/api/deleteProduct/{productId}")
    @ResponseBody
    public ResponseMessage<String> deleteProduct(@PathVariable String productId) {
        if (productId == null || productId.isBlank()) {
            throw new BusinessException( ErrorCode.PARAM_CANNOT_BE_NULL,"productId can not be null");
        }
        productId=productId.trim();
        pservice.deleteProduct(productId);
        return ResponseMessage.success(productId);
    }

    // 管理员商品管理页面
    @GetMapping("/admin/products")
    public String adminProductsPage(@PageableDefault(size = 10) Pageable pageable, Model model, HttpSession session) {
        // 检查用户是否登录
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }

        // 检查用户是否为管理员
        UserInfoDTO currentUser = (UserInfoDTO) session.getAttribute(SessionConstant.CURRENT_USER);
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            // 不是管理员,拒绝访问,跳转到商品列表页面
            return "redirect:/products/lists";
        }
        // 是管理员,跳转到百度
        return "redirect:http://localhost:5173/products";
    }

}
