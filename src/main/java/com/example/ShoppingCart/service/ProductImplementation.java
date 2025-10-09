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

import java.math.BigDecimal;


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

        Integer status = dto.getStatus();
        if (status == null) {
            product.setStatus(0);
        } else {
            //If set to shelf status but inventory is 0, refuse to create
            if (status == 1 && product.getStock() <= 0) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "Inventory is 0, unable to set to shelf status");
            }
            product.setStatus(status);
        }
        return prepo.save(product);
    }

    @Override
    public Page<Product> getAllProductsByStatus(Pageable pageable) {
        return prepo.findAvailableProducts(pageable);
    }

    @Override
    public Product updateProduct(ProductUpdateDTO dto) {
        String productId=dto.getProductId();
        if (productId == null || productId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_CANNOT_BE_NULL);
        }
        Product product=prepo.findByProductId(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode. PRODUCT_NOT_EXIST, productId);
        }

        if (dto.getProductName() != null) {
            String newProductName = dto.getProductName().trim();

            //If the new name is the same as the old name, there is no need to check for duplicates

            if (!newProductName.equals(product.getProductName())) {
                //Check if there are other products using the same name
                if (prepo.existsByProductName(newProductName)) {
                    throw new BusinessException(ErrorCode.PRODUCT_NAME_DUPLICATE, newProductName);
                }
                //Update product name
                product.setProductName(newProductName);
            }
        }

        if (dto.getProductName() != null) {
            product.setProductName(dto.getProductName());
        }
        if (dto.getPrice() != null) {
            if (dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(ErrorCode. PARAM_ERROR, "The price must be greater than 0");
            }
            product.setPrice(dto.getPrice());
        }
        if (dto.getStock() != null) {
            if (dto.getStock() < 0) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "Inventory cannot be negative");
            }

        }
        if (dto.getStatus() != null) {
            //If set to shelf status but inventory is 0, refuse to update
            if (dto.getStatus() == 1 && dto.getStock() <= 0) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "Inventory is 0, unable to set to shelf status");
            }
        }
        if (dto.getStock()==0){
            product.setStatus(0);
            product.setStock(dto.getStock());
        }else{
            product.setStatus(dto.getStatus());
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


        return prepo.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(String productId) {
        prepo.deleteProductByProductId(productId);
    }


}
