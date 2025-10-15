package com.example.ShoppingCart.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.OrderInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.UserAddress;
import com.example.ShoppingCart.repository.CartRepository;
import com.example.ShoppingCart.repository.OrderItemRepository;
import com.example.ShoppingCart.repository.OrderRepository;
import com.example.ShoppingCart.repository.PaymentRecordRepository;
import com.example.ShoppingCart.repository.UserAddressRepository;
import com.example.ShoppingCart.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderImplementation implements OrderInterface {
    @Autowired
    private OrderRepository orderrepo;
    @Autowired
    private PaymentRecordRepository  paymentrepo;
    @Autowired
    private CartRepository cartrepo;
    @Autowired
    private UserRepository userrepo;
    @Autowired
    private OrderItemRepository orderitemrepo;
    @Autowired
    private UserAddressRepository useraddressrepo;
    @Autowired
    private ProductImplementation productImplementation;

    @Override
    public Order createOrder(String userId) {
        //first check if the order exists, and the cart cannot be empty, if the order exists delete the old order, create a new order
        Optional<Order> pendingorder = orderrepo.findByUser_UserIdAndOrderStatus(userId,0);
        if(pendingorder.isPresent()){
            // delete the old Pending order, create a new order with the current cart content
            orderrepo.delete(pendingorder.get());
        }
        List<CartRecord> cartRecords = cartrepo.findByUser_UserId(userId);
        //verify if the cart is empty
        if(cartRecords.isEmpty()){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Cart is empty");
        }
        
        // verify if the stock of all products is enough
        for(CartRecord record : cartRecords) {
            Product product = record.getProduct();
            if(product.getStock() < record.getQuantity()) {
                throw new BusinessException(ErrorCode.PRODUCT_STOCK_NOT_ENOUGH, 
                    "Product " + product.getProductName() + " stock is not enough! Current stock: " + product.getStock() + " pieces, cart quantity: " + record.getQuantity() + " pieces");
            }
        }
        
        //start creating order after verification

        Order order = new Order();
        order.setUser(userrepo.findById(userId).orElseThrow());
        // allow creating orders without addresses, addresses can be added on the confirmation page
        UserAddress addr = useraddressrepo.findByUser_UserId(userId).stream().findFirst().orElse(null);
        if (addr != null) {
            order.setAddressdetail(addr.getDetailAddress());   //simple solution to create address and phone
            order.setPhone(addr.getPhone());
            order.setAddress(addr);                            // can be null
        }
        order.setTotalAmount(calculateTotalPrice(cartRecords));
        order.setOrderStatus(0);                                //pending in default
        order.setOrderTime(LocalDateTime.now());
        order.setPaymentRecord(null);

        List<OrderItem> orderItems = new ArrayList<>();

        order=orderrepo.save(order);        //save order firstly

        for(CartRecord record:cartRecords){
            OrderItem orderItem = new OrderItem();

            orderItem.setBuyQuantity(record.getQuantity());
            orderItem.setProduct(record.getProduct());
            orderItem.setProductPrice(record.getProduct().getPrice());
            orderItem.setProductName(record.getProduct().getProductName());

            orderItem.setOrder(order);
            orderItems.add(orderItem);

        }
        order.setOrderItems(orderItems);
        //save orderItems
        orderitemrepo.saveAll(orderItems);
        // note: do not clear the cart here, but after payment
        // so users can click Back on the confirmation page, and the cart content is still retained

        return order;
    }

    @Override
    public BigDecimal calculateTotalPrice(List<CartRecord> cartRecord) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartRecord record :cartRecord) {
            BigDecimal itemTotal = record.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(record.getQuantity()));
            total = total.add(itemTotal);
        }

        return total;
    }

    @Override
    public void updateOrderStatus(Order order,int newOrderStatus) {

        order.setOrderStatus(newOrderStatus);
        orderrepo.save(order);
    }



    @Override
    public PaymentRecord createPaymentRecord(String paymentMethod,Order order) {
        // validate payment method and create record
        if (paymentMethod == null || paymentMethod.isEmpty()){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "paymentMethod");
        }
        // whitelist supported payment methods
        String pm = paymentMethod.toLowerCase();
        if (!(pm.equals("alipay") || pm.equals("paynow") || pm.equals("visa"))){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "paymentMethod");
        }

        PaymentRecord newRecord = new PaymentRecord();
        newRecord.setPaymentMethod(pm);
        newRecord.setPaymentAmount(order.getTotalAmount());
        newRecord.setPaymentTime(LocalDateTime.now());
        newRecord.setTradeNo(UUID.randomUUID().toString());             //not should be generated by frontend
        newRecord.setPayStatus(1);                                     //paymentrecord has status?
        newRecord.setOrder(order);
        order.setOrderStatus(1);                                        //"paid" status
        order.setPaymentRecord(newRecord);
        newRecord=paymentrepo.save(newRecord);
        orderrepo.save(order);
        productImplementation.updateStockAfterPayment(order);

        // after payment, clear the cart
        String userId = order.getUser().getUserId();
        List<CartRecord> cartRecords = cartrepo.findByUser_UserId(userId);
        if (!cartRecords.isEmpty()) {
            cartrepo.deleteAll(cartRecords);
        }

        return  newRecord;
    }

    @Override
    public Order findPendingOrder(String userId){
        Order order= orderrepo.findFirstByUser_UserIdAndOrderStatusOrderByOrderTimeDesc(userId,0);
        if(order==null){
            throw new IllegalStateException("PendingOrder is empty");
        }
        return order;
    }

    @Override
    public Order findPaidOrder(String userId){
        Order order= orderrepo.findFirstByUser_UserIdAndOrderStatusOrderByOrderTimeDesc(userId,1);
        if(order==null){
            throw new IllegalStateException("PaidOrder is empty");
        }
        return order;
    }

    @Override
    public void cancelOrder(Order order){
        orderrepo.delete(order);
    }

    @Override
    public Order findByOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "orderId");
        }
        return orderrepo.findById(orderId).orElseThrow(() -> new BusinessException(ErrorCode.PARAM_ERROR, "order"));
    }
    
    @Override
    public void updateOrder(Order order) {
        if (order == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Order cannot be null");
        }
        orderrepo.save(order);
    }


}
