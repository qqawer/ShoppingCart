package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.pojo.dto.ProductCreateDTO;
import com.example.ShoppingCart.pojo.dto.ProductUpdateDTO;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // 或你前端 dev-server 的端口
@Controller
public class ProductController {

    @Autowired
    private ProductInterface pservice;

    //Query all products.
    @GetMapping("products/lists")
    public String getAllProducts(@PageableDefault(size = 12) Pageable pageable, Model model) {
        Page<Product> products=pservice.getAllProductsByStatus(pageable);
        model.addAttribute("page", products);
        return "product/lists";
    }

    // Query a single product based on the product ID
    @GetMapping("/products/{productId}")
    public String getProductById(@PathVariable String productId,Model model) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_CANNOT_BE_NULL,"productId can not be null");
        }
        Product product= pservice.getProductById(productId);
        model.addAttribute("product",product);
        return "product/detail";
    }

    // Perform a fuzzy search based on the product name
    @GetMapping("/products/search")
    public ResponseMessage<Page<Product>> searchProductsByName(@RequestParam(required = false) String productName,Pageable pageable) {
        if (productName == null || productName.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_CANNOT_BE_NULL);
        }
        productName = productName.strip();
        Page<Product> products=pservice.searchProductsByName(productName,pageable);

        return ResponseMessage.success(products);
    }
    @GetMapping("/api/products/lists")
    @ResponseBody
    public ResponseMessage<Page<Product>> getApiProduct(@PageableDefault(size = 5) Pageable pageable){
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


}
