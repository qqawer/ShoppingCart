package com.example.ShoppingCart;

import com.example.ShoppingCart.model.*;
import com.example.ShoppingCart.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;




@SpringBootTest
public class ShoppingCartApplicationTests  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRecordRepository;

    @Autowired
    private OrderRepository shopOrderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Test
    void injectAllData() {
        // USER
        User user1 =buildUser("avatar1.jpg", "2024-01-15 10:00:00", "hashed_password_1", "123-456-7890", "John Doe");
        userRepository.save(user1);
        User user2 =buildUser("avatar2.png", "2024-01-15 10:30:00", "hashed_password_2", "987-654-3210", "Jane Smith");
        userRepository.save(user2);
        User user3 =buildUser("avatar3.gif", "2024-01-15 11:00:00", "hashed_password_3", "555-123-4567", "Peter Jones");
        userRepository.save(user3);
        User user4 =buildUser("avatar4.jpeg", "2024-01-15 11:30:00", "hashed_password_4", "111-222-3333", "Alice Brown");
        userRepository.save(user4);
        User user5 =buildUser("avatar5.svg", "2024-01-15 12:00:00", "hashed_password_5", "444-555-6666", "Bob Wilson");
        userRepository.save(user5);
        //USER ADDRESS
        UserAddress userAddress1 = buildUserAddress("123 Main St, Apt 4B", true, "123-456-7890", "John Doe", "California", user1);
        userAddressRepository.save(userAddress1);
        UserAddress userAddress2 = buildUserAddress("456 Oak Ave", false, "987-654-3210", "Jane Smith", "New York", user2);
        userAddressRepository.save(userAddress2);
        UserAddress userAddress3 = buildUserAddress("789 Pine Ln", false, "555-123-4567", "Peter Jones", "Texas", user3);
        userAddressRepository.save(userAddress3);
        UserAddress userAddress4 = buildUserAddress("101 Elm Rd", false, "111-222-3333", "Alice Brown", "Florida", user4);
        userAddressRepository.save(userAddress4);
        UserAddress userAddress5 = buildUserAddress("222 Maple Dr", true, "444-555-6666", "Bob Wilson", "Illinois", user5);
        userAddressRepository.save(userAddress5);
        //Product
        Product product1 =buildProduct("Apple", "The latest iPhone", "iphone15_main.jpg", "999.99", "iPhone 15 Pro",200);
        productRepository.save(product1);
        Product product2 =buildProduct("Samsung", "The newest Galaxy phone", "samsung_galaxy_s23.jpg", "899.99", "Samsung Galaxy S23",300);
        productRepository.save(product2);
        Product product3 =buildProduct("Apple", "16-inch MacBook Pro", "macbook_pro_16.jpg", "2499.99", "MacBook Pro 16",400);
        productRepository.save(product3);
        Product product4 =buildProduct("Nike", "Classic Air Max sneakers", "nike_air_max.jpg", "129.99", "Nike Air Max",500);
        productRepository.save(product4);
        Product product5 =buildProduct("Mr. Coffee", "Programmable coffee maker", "coffee_maker.jpg", "79.99", "Coffee Maker",600);
        productRepository.save(product5);
        Product product6 =buildProduct("Sony", "Noise-cancelling wireless headphones", "wireless_headphones.jpg", "199.99", "Wireless Headphones",700);
        productRepository.save(product6);
        Product product7 =buildProduct("Gaiam", "Non-slip yoga mat", "yoga_mat.jpg", "39.99", "Yoga Mat",800);
        productRepository.save(product7);
        Product product8 =buildProduct("TaoTronics", "LED desk lamp with USB charging", "desk_lamp.jpg", "49.99", "Desk Lamp",900);
        productRepository.save(product8);
        //Order
        Order order1 = buildShopOrder(0, "2025-10-05 10:00:00", "1259.97", userAddress1, user1);
        shopOrderRepository.save(order1);
        Order order2 = buildShopOrder(1, "2025-10-05 11:00:00", "1099.98", userAddress2, user2);
        shopOrderRepository.save(order2);
        Order order3 = buildShopOrder(1, "2025-10-05 12:00:00", "2659.97", userAddress3, user3);
        shopOrderRepository.save(order3);
        //OrderItem
        orderItemRepository.save(buildOrderItem( 1, product1.getProductName(), product1.getPrice(), order1, product1));
        orderItemRepository.save(buildOrderItem(2, product4.getProductName(), product4.getPrice(), order1, product4));
        orderItemRepository.save(buildOrderItem( 1, product2.getProductName(), product2.getPrice(), order2, product2));
        orderItemRepository.save(buildOrderItem( 1, product6.getProductName(), product6.getPrice(), order2, product6));
        orderItemRepository.save(buildOrderItem(1, product3.getProductName(), product3.getPrice(), order3, product3));
        orderItemRepository.save(buildOrderItem(2, product5.getProductName(), product5.getPrice(), order3, product5));
        //CartRecord
        cartRecordRepository.save(buildCartRecord( "2025-10-05 09:30:00", true, 1, product1, user1));
        cartRecordRepository.save(buildCartRecord("2025-10-05 09:35:00", true, 2, product4, user1));
        cartRecordRepository.save(buildCartRecord("2025-10-05 09:40:00", false, 1, product6, user1));
        cartRecordRepository.save(buildCartRecord( "2025-10-05 10:15:00", true, 1, product2, user2));
        cartRecordRepository.save(buildCartRecord("2025-10-05 10:20:00", false, 1, product7, user2));
        cartRecordRepository.save(buildCartRecord("2025-10-05 10:25:00", false, 3, product5, user2));
        cartRecordRepository.save(buildCartRecord( "2025-10-05 11:10:00", true, 1, product3, user3));
        cartRecordRepository.save(buildCartRecord("2025-10-05 11:15:00", true, 2, product5, user3));
        cartRecordRepository.save(buildCartRecord("2025-10-05 11:20:00", true, 1, product8, user3));
        cartRecordRepository.save(buildCartRecord("2025-10-05 12:05:00", false, 1, product1, user4));
        cartRecordRepository.save(buildCartRecord( "2025-10-05 12:10:00", false, 1, product3, user4));
        cartRecordRepository.save(buildCartRecord( "2025-10-05 13:30:00", true, 1, product6, user5));
        cartRecordRepository.save(buildCartRecord( "2025-10-05 13:35:00", false, 2, product4, user5));
        //Payment
        paymentRecordRepository.save(buildPaymentRecord( 0, order1.getTotalAmount(), "Credit Card", "2025-10-05 10:10:00", "trade123", order1));
        paymentRecordRepository.save(buildPaymentRecord( 1, order2.getTotalAmount(), "PayPal", "2025-10-05 11:15:00", "trade456", order2));
        paymentRecordRepository.save(buildPaymentRecord(1, order3.getTotalAmount(), "Debit Card", "2025-10-05 12:20:00", "trade789", order3));

    }


    // 构建方法
    private User buildUser( String avatar, String createTime, String password, String phoneNumber, String userName) {
        User user = new User();
        user.setAvatar(avatar);
        user.setCreateTime(LocalDateTime.parse(createTime.replace(" ", "T")));
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setUserName(userName);
        return user;
    }

    private UserAddress buildUserAddress( String detailAddress, boolean isDefault,
                                          String phone, String receiverName, String region, User user) {
        UserAddress address = new UserAddress();
        address.setDetailAddress(detailAddress);
        address.setDefault(isDefault);
        address.setPhone(phone);
        address.setReceiverName(receiverName);
        address.setRegion(region);
        address.setUser(user);
        return address;
    }

    private Product buildProduct(String brand, String description,
                                 String mainImage, String price, String productName, int stock) {
        Product product = new Product();
        product.setBrand(brand);
        product.setDescription(description);
        product.setMainImage(mainImage);
        product.setPrice(new BigDecimal(price));
        product.setProductName(productName);
        product.setStock(stock);
        return product;
    }

    private Order buildShopOrder(int orderStatus, String orderTime,
                                 String totalAmount, UserAddress address, User user) {
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        order.setOrderTime(LocalDateTime.parse(orderTime.replace(" ", "T")));
        order.setTotalAmount(new BigDecimal(totalAmount));
        order.setAddress(address);
        order.setUser(user);
        return order;
    }

    private OrderItem buildOrderItem(int buyQuantity, String productName,
                                     BigDecimal productPrice, Order order, Product product) {
        OrderItem item = new OrderItem();
        item.setBuyQuantity(buyQuantity);
        item.setProductName(productName);
        item.setProductPrice(productPrice);
        item.setOrder(order);
        item.setProduct(product);
        return item;
    }

    private CartRecord buildCartRecord(String addTime, boolean isSelected,
                                       int quantity, Product product, User user) {
        CartRecord cart = new CartRecord();
        cart.setAddTime(LocalDateTime.parse(addTime.replace(" ", "T")));
        cart.setSelected(isSelected);
        cart.setQuantity(quantity);
        cart.setProduct(product);
        cart.setUser(user);
        return cart;
    }

    private PaymentRecord buildPaymentRecord(int payStatus, BigDecimal paymentAmount,
                                             String paymentMethod, String paymentTime, String tradeNo, Order order) {
        PaymentRecord payment = new PaymentRecord();
        payment.setPayStatus(payStatus);
        payment.setPaymentAmount(paymentAmount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentTime(LocalDateTime.parse(paymentTime.replace(" ", "T")));
        payment.setTradeNo(tradeNo);
        payment.setOrder(order);
        return payment;
    }

}
