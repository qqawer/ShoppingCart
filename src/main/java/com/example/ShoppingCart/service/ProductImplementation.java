package com.example.ShoppingCart.service;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.pojo.dto.ProductCreateDTO;
import com.example.ShoppingCart.pojo.dto.ProductUpdateDTO;
import com.example.ShoppingCart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@Transactional
public class ProductImplementation implements ProductInterface {
    @Autowired
    private ProductRepository prepo;

    @Override
    @Transactional
    public Page<Product> getAllProducts(Pageable pageable) {

        return prepo.findAll(pageable);
    }

    @Override
    @Transactional
    public Product getProductById(String productId) {
        return prepo.findByProductId(productId);
    }

    @Override
    @Transactional
    public Page<Product> searchProductsByName(String productName,Pageable pageable) {
        return prepo.findByProductNameContaining(productName,pageable);
    }

    @Override
    @Transactional
    public Product createProduct(ProductCreateDTO dto) {
        if (prepo.existsByProductName(dto.getProductName())) {
            throw new BusinessException(ErrorCode.PRODUCT_NAME_DUPLICATE, dto.getProductName());
        }
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setBrand(dto.getBrand());
        product.setMainImage(dto.getMainImage());
        product.setDescription(dto.getDescription());
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : 1); // 默认1

        return prepo.save(product);
    }

    @Override
    public Page<Product> getAllProductsByStatus(Pageable pageable) {
        return prepo.findByStatus(1, pageable);
    }

    @Override
    public Product updateProduct(ProductUpdateDTO dto) {
        String productId=dto.getProductId();
        if (productId==null){
            throw new BusinessException(ErrorCode.PARAM_CANNOT_BE_NULL);
        }
        Product product=prepo.findByProductId(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode. PRODUCT_NOT_EXIST, productId);
        }
        if (dto.getProductName() != null) {
            product.setProductName(dto.getProductName());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getStock() != null) {
            product.setStock(dto.getStock());
        }
        if (dto.getBrand() != null) {
            product.setBrand(dto.getBrand());
        }
        if (dto.getMainImage() != null) {
            product.setMainImage(dto.getMainImage());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            product.setStatus(dto.getStatus());
        }

        return prepo.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(String productId) {
        prepo.deleteProductByProductId(productId);
    }


}
