package com.example.ShoppingCart;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.User;
import com.example.ShoppingCart.model.UserAddress;
import com.example.ShoppingCart.repository.CartRepository;
import com.example.ShoppingCart.repository.OrderItemRepository;
import com.example.ShoppingCart.repository.OrderRepository;
import com.example.ShoppingCart.repository.PaymentRecordRepository;
import com.example.ShoppingCart.repository.ProductRepository;
import com.example.ShoppingCart.repository.UserAddressRepository;
import com.example.ShoppingCart.repository.UserRepository;




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
        User user1 =buildUser("avatar1.jpg", "2024-01-15 10:00:00", "hashed_password_1", "80000001", "John Doe","CUSTOMER");
        userRepository.save(user1);
        User user2 =buildUser("avatar2.png", "2024-01-15 10:30:00", "hashed_password_2", "80000002", "Jane Smith","CUSTOMER");
        userRepository.save(user2);
        User user3 =buildUser("avatar3.gif", "2024-01-15 11:00:00", "hashed_password_3", "80000003", "Peter Jones","CUSTOMER");
        userRepository.save(user3);
        User user4 =buildUser("avatar4.jpeg", "2024-01-15 11:30:00", "hashed_password_4", "80000004", "Alice Brown","CUSTOMER");
        userRepository.save(user4);
        User user5 =buildUser("avatar5.svg", "2024-01-15 12:00:00", "hashed_password_5", "80000005", "Bob Wilson","ADMIN");
        userRepository.save(user5);
        //USER ADDRESS
        UserAddress userAddress1 = buildUserAddress("123 Main St, Apt 4B", true, "80000001", "John Doe", "California", user1);
        userAddressRepository.save(userAddress1);
        UserAddress userAddress2 = buildUserAddress("456 Oak Ave", true, "80000002", "Jane Smith", "New York", user2);
        userAddressRepository.save(userAddress2);
        UserAddress userAddress3 = buildUserAddress("789 Pine Ln", true, "80000003", "Peter Jones", "Texas", user3);
        userAddressRepository.save(userAddress3);
        UserAddress userAddress4 = buildUserAddress("101 Elm Rd", true, "80000004", "Alice Brown", "Florida", user4);
        userAddressRepository.save(userAddress4);
        UserAddress userAddress5 = buildUserAddress("222 Maple Dr", true, "80000005", "Bob Wilson", "Illinois", user5);
        userAddressRepository.save(userAddress5);
        //Product
        Product product1 =buildProduct("Apple", "The latest iPhone", "https://images.macrumors.com/t/dQZq21dmJHx3YkK_MZNHsVFt7Zo=/1920x/article-new/2023/01/iPhone-15-General-Mock-Feature.jpg", "999.99", "iPhone 15 Pro",200);
        productRepository.save(product1);
        Product product2 =buildProduct("Samsung", "The newest Galaxy phone", "https://i5.walmartimages.com/seo/Samsung-Galaxy-S23-Ultra-5G-SM-S918B-DS-256GB-12GB-RAM-DUAL-SIM-Global-Model-Factory-Unlocked-GSM-Phantom-Black_a77b9342-adda-4ed0-a9bd-2a3a80c29e21.9b9469de053b4157eb544c0267dbbca2.jpeg", "899.99", "Samsung Galaxy S23",300);
        productRepository.save(product2);
        Product product3 =buildProduct("Apple", "16-inch MacBook Pro", "https://sm.pcmag.com/t/pcmag_au/review/a/apple-macb/apple-macbook-pro-16-inch-2023-m2-max_7zsb.1920.jpg ","2499.99", "MacBook Pro 16",400);
        productRepository.save(product3);
        Product product4 =buildProduct("Nike", "Classic Air Max sneakers", "https://i.pinimg.com/736x/de/42/e2/de42e281438d43a9c7c955ee17232e43.jpg", "129.99", "Nike Air Max",500);
        productRepository.save(product4);
        Product product5 =buildProduct("Mr. Coffee", "Programmable coffee maker", "https://i5.walmartimages.com/seo/Mr-Coffee-Programmable-Coffee-Maker-12-Cup-Capacity-Black_fc037b7b-2057-4366-b6c1-e71b96040c2d.7771323bdac4cebece38935f77ca3ecb.jpeg", "79.99", "Coffee Maker",600);
        productRepository.save(product5);
        Product product6 =buildProduct("Sony", "Noise-cancelling wireless headphones", "https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6347/6347796_sd.jpg", "199.99", "Wireless Headphones",700);
        productRepository.save(product6);
        Product product7 =buildProduct("Gaiam", "Non-slip yoga mat", "https://m.media-amazon.com/images/I/913OwzFwftL._AC_SL1500_.jpg", "39.99", "Yoga Mat",800);
        productRepository.save(product7);
        Product product8 =buildProduct("TaoTronics", "LED desk lamp with USB charging", "https://images-na.ssl-images-amazon.com/images/I/71UgSY4fovL.jpg", "49.99", "Desk Lamp",900);
        productRepository.save(product8);

                Product p1 = buildProduct("Saturnbird", "Flavors 0-6, 3-second cold brew, 0 sugar 0 fat", 
            "https://img.alicdn.com/imgextra/i3/725677994/O1CN01SfeZ1w28vIlug1vMb_!!0-item_pic.jpg", "269.00", "Saturnbird Premium Cold Brew Coffee 36 Capsules", 300);
        productRepository.save(p1);

        Product p2 = buildProduct("Genki Forest", "0 sugar 0 fat 0 calories, Fresh white peach flavor, Bulk pack", 
            "https://static.foodtalks.cn/image/cms/entry/ca5235aa94b4c40df8b2921b07283d5d7eed.jpg", "129.90", "Genki Forest White Peach Sparkling Water 480mL×24 Bottles", 500);
        productRepository.save(p2);

        Product p3 = buildProduct("Haohuanluo", "Authentic Liuzhou, Extra spicy, Individual packaging", 
            "https://img.alicdn.com/i1/2808887230/O1CN01Ghw0te23HOHF5TKTI_!!2808887230.jpg", "59.80", "Haohuanluo Snail Rice Noodles 400g×5 Bags", 800);
        productRepository.save(p3);

        Product p4 = buildProduct("Xiaomi", "Infrared sensor, Electric clamp arm, 30W wireless fast charging", 
            "https://img.alicdn.com/bao/uploaded/i1/2222828811/O1CN01h6D7iz2ExUYcIHC0T_!!0-item_pic.jpg", "149.00", "Xiaomi Car Wireless Charger 30W", 200);
        productRepository.save(p4);

        Product p5 = buildProduct("Michelin", "Silent ribs, Wet brake 2.4m shorter, Comfort upgrade", 
            "https://img.alicdn.com/i1/2302551350/O1CN015GcGHG1LqLTDKfeZV_!!2302551350.jpg", "699.00", "Michelin 215/55R17 Primacy 4 Tire", 40);
        productRepository.save(p5);

        Product p6 = buildProduct("Chongxing", "Flushable, 99% odor elimination, Dust-free, Food-grade materials", 
            "https://img10.360buyimg.com/n1/jfs/t1/98765/7/21374/56912/6409392aFbc5be55b/d98e28ff1b543b17.jpg", "89.00", "Chongxing Tofu Cat Litter 6L×4 Packs", 300);
        productRepository.save(p6);

        Product p7 = buildProduct("Yanxuan", "0 grain 0 attractant, 88% animal ingredients, 42% crude protein", 
            "https://img.alicdn.com/i2/1986966273/O1CN01cRzpuq1wD5GLyZheB_!!1986966273.jpg", "168.00", "Yanxuan All Life Stages Cat Food 2kg×2 Bags", 200);
        productRepository.save(p7);

        Product p8 = buildProduct("Sony", "12MP, 5-axis stabilization, AI smart framing, 4K 60fps", 
            "https://i.ytimg.com/vi/FRkHu_0kqbc/maxresdefault.jpg", "13999.00", "Sony ZV-E1 Full-Frame Vlog Camera", 15);
        productRepository.save(p8);

        Product p9 = buildProduct("XGIMI", "4K ultra HD, 2200 ANSI lumens, Harman Kardon audio", 
            "https://img.alicdn.com/i4/2177009988/O1CN01eVQSDX2NeYmTPpWM2_!!2177009988.jpg", "4999.00", "XGIMI H6 4K Projector", 25);
        productRepository.save(p9);

        Product p10 = buildProduct("Baseus", "2C1A triple port, 65W universal plug, Foldable prongs, Compatible with PD/QC", 
            "https://pic3.zhimg.com/v2-a45fd674a750d7917e28682e9c69e686_r.jpg", "158.00", "Baseus 65W GaN Fast Charger", 500);
        productRepository.save(p10);

        Product p11 = buildProduct("Yilin Press", "French classic by Saint-Exupéry, Hardcover with full-color illustrations", 
            "https://book.goldenhouse.com.my/wp-content/uploads/2021/01/xwz3dlts_des_02-768x938.jpg", "45.00", "The Little Prince Hardcover Edition", 600);
        productRepository.save(p11);

        Product p12 = buildProduct("Chongqing Publishing House", "Liu Cixin's masterpiece, First Asian Hugo Award novel", 
            "https://img.alicdn.com/imgextra/i3/859515618/O1CN01SnCpAB1rN5jTfddWg_!!859515618.jpg", "93.00", "The Three-Body Problem Complete Set 3 Volumes", 400);
        productRepository.save(p12);

        Product p13 = buildProduct("Decathlon", "7-speed freewheel, Aluminum frame, Comfortable geometry, Beginner's choice", 
            "https://img.alicdn.com/bao/uploaded/i4/352469034/O1CN01jHKQ152Gbcq3wZ5vV_!!352469034-0-lubanu-s.jpg", "1799.00", "Decathlon RC100 Road Bike", 35);
        productRepository.save(p13);

        Product p14 = buildProduct("Oakley", "Plutonite lens, 100% UV protection, Lightweight O-Matter frame", 
            "https://img12.360buyimg.com/imgzone/jfs/t1/171822/38/6436/98140/6084ea2aE9eb82bae/03ea2b6714211734.jpg", "1299.00", "Oakley Sport Sunglasses Radar EV", 60);
        productRepository.save(p14);

        Product p15 = buildProduct("Whisper", "Liquid material bonding, 10x absorption, Invisible experience", 
            "https://img.alicdn.com/i3/217101303/O1CN01ObIes81LUosb5D7Ih_!!217101303.jpg", "69.90", "Whisper Liquid Sanitary Pads 270mm×30 Pieces", 300);
        productRepository.save(p15);

        Product p16 = buildProduct("Winona", "Sensitive skin formula, Portulaca + sea buckthorn, 7-day redness relief", 
            "https://th.bing.com/th/id/R.1a6a50dfffe7c91832f517823d6a5320?rik=ElnJFQNR265Wag&riu=http%3a%2f%2fyufumall-img4a.yufu.cn%2fAccount%2f100000001500%2fMainPic%2fDefault%2f6b27e003ae6c43cea9dca43a48d4b66e.jpg&ehk=dBdLafLiBseGQusrWTmPPD9ZCpTWCRdKz8eUrk5GeSI%3d&risl=&pid=ImgRaw&r=0", "268.00", "Winona Soothing Moisturizing Cream 50g", 150);
        productRepository.save(p16);

        Product p17 = buildProduct("Nongfu Spring", "17.5° sugar-acid ratio, Origin direct sourcing, Individual mesh bag per piece", 
            "https://imgservice.suning.cn/uimg1/b2c/image/wueK18eYfQKHcIkOTwGvlA.jpg_800w_800h_4e", "89.90", "Nongfu Spring 17.5° Orange 5kg", 200);
        productRepository.save(p17);

        Product p18 = buildProduct("Three Squirrels", "6 types of nuts and dried fruits, Dry-wet separation packaging, Pregnancy safe", 
            "https://img.alicdn.com/bao/uploaded/O1CN01wkWDkF26ehLN8EbFb_!!6000000007687-0-yinhe.jpg", "149.00", "Three Squirrels Daily Nuts 750g", 400);
        productRepository.save(p18);

        Product p19 = buildProduct("3M", "Front windshield UV400, Side-rear high privacy, 99% heat insulation, 10-year warranty", 
            "https://tse2.mm.bing.net/th/id/OIP.weYGntZYbJe2DlbrLLnFsAHaKc?rs=1&pid=ImgDetMain&o=7&rm=3", "1899.00", "3M Automotive Window Tint Full Car Package", 80);
        productRepository.save(p19);

        Product p20 = buildProduct("Apple", "Lightweight Apple, Only one stock",
            "https://tse2.mm.bing.net/th/id/OIP.SOKeeHGcU3uKk3o8edUv7gHaE7?rs=1&pid=ImgDetMain&o=7&rm=3", "799.00", "iPhone 13 mini 128GB", 1);
        productRepository.save(p20);
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
    private User buildUser( String avatar, String createTime, String password, String phoneNumber, String userName,String role) {
        User user = new User();
        user.setAvatar(avatar);
        user.setCreateTime(LocalDateTime.parse(createTime.replace(" ", "T")));
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setUserName(userName);
        user.setRole(role);
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
