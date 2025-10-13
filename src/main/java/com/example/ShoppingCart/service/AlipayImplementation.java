package com.example.ShoppingCart.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.stereotype.Service;

@Service
public class AlipayImplementation {
    private final AlipayClient alipayClient;

    public AlipayClient getAlipayClient() {
        return alipayClient;
    }

    public AlipayImplementation() throws AlipayApiException {
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCeqYl01vUBbapDABJyQSC5EavpZOYWyh6d/6jszdwmJVbmv1Hc+pA5KEHjHEEig83Jz4F1ZuU23DzQt8sjdZ8XFkIb8FRYKQIW3ToM1dsBPcU8++ODCR+VgDvfu79iIHWzZdiRF7fMOWGHbKdGZI/IkZ7LbOJQEV6JyK9ElzRzZtijWDQ2GA/2i7kjOBP7ZnxEk5ujqYc2VCcLNIHOdBrhFn3m8/OWeU0+yL6ECj+KOilbXJ3kPScRTKbz2Fwsis+ceaVkIDfxztKroeBF0Lich8+RPpIdpSJ4e+wI3iurcUt0X+m7VNsLYkhKdJHY/PaTTSu29U6iMy+YbO6PIj8dAgMBAAECggEAf1l/LhwXXcUjQ1H8fF6QIFvxy40kLaif+JurgB8gFDImVWG9GIy5VFdewyb7lPOMKu72b0BevLD701+imlyfjE1eVMSLPpPzzFxXatQATDfDZ9aaX6PkCQB7jnJJBD8fmhYmRRdvfGmWQbfRb+vpiMhxV8CWykOWySsemwRuoizoJ6HC3/U34Nk9sD4mvT3aN0Dd1NO44O9dCTDw1hCAxuYOfFgYddmiYTLY0PBovjj+eH9O0OYLaqKeqPyJGwBwAxnMs7b24Jk9cw8CYmaq3pidDpvIEXe3bRp5oYAc9wzkdo13OXw2tH1dpufsuCEW6LRdJ2vuNEt6St8nSdpA1QKBgQDMYfYxFbiIwuFSVPoT8x89nTe0fkBa+2kc1ja7vc9IPJ8TJp+4Ta5HLnU9XwnoMX1/zcZ2DcXy9bf1KP7y291SSdi5CQHDt8Mp37PndYsAnEB8Ng00mDtYHUNuq6fJKWdaEniDgK8EullEuqdhChPwtHBAYrCjxTwNc597IzbpfwKBgQDGu5gqbkridzROFen8w9D862OvK+e0+EmpgkxL/Xj0p0j6RYB65EHrZTgDQpgjYKbCUSiU5fE2rwo49J+iWD9fOE+LUoLxQzzxkOjZncuWy3Gph14+AOkDxhtyZlG1b0xPycweNncY1dN5rH9PxQHvYf+JT6i7d4+Ix5CzJTWNYwKBgDBehrs7Hr8PD/BCOCT8hso4KPTIZEaj9U5jy/RGiYXZAvSvUWF20OX7srdhhKC1BbR3aJ6Lx+smB/E3UI79RbWx0F6KKSa+mKuSS6tdgBYW5JQA3sMxu+mwk0Lf0QekitcgQ/ErRUBjMbk4S2CF2A5BRceXE6LOjNXKJVYpEVfVAoGAEOPswN5y2uCKv4A5TNRPtM9Ev6SZotXfXNrIJUU7vH9jQnIZw1gXCiymIBDO4WLbiXRgYJdScvVZhxGzZ5bce/lLOWof2uV+DoHkaGpy3TQZSvFIXziwlTOHLafoleUJtVZPVNoIMUIrIRI6F8uEqJXJpB/zFUvKFTPw5mjpLkUCgYEAmx+mijan7RF/PNCs1nPphor13Y8melEPoKk3gUlfr6Qoxwih2kHDPXpQUzJ5PzYPwy9vSFIje1NFDhMzDb8eYYfykQur8npy2OJzagMVFCgFvXmJ66jFjEOD1t2+n5rny2aixRtr3cjt9Wg2XWrogym0yU1mrqzBomgK7stKLJM=";
        String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnA4w58/EegvNhLp0HuR2PZy0eamXPAtaisIU5JxmC3P72XEDmBvLkptlNy+nEOYOI8xhMfvafmApN8iUMYYk4Yf7AbduXHQCDJDPZkOmR7k067Pv1nkLSunAntmID2HSAIyuMl1hNjcv52UBX8sPD4DHdYmZwwbPPHO0edRH/SPtutgj5tEnKKwEQ2KAPlZQpHzbsYhbN4G9zUUkKMONm0npk7ed0yE8QgEI0WeQ4tF8pwmQPM1lD8bIL44bGEZqKDEeovy8JivYG7zQcoO4u+HEvYQviG8P7R0xXcHFinASSI5Mq38ETSjzCkTyF+LUMXuMnsGIGnOVXKPcIHvSaQIDAQAB";
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi-sandbox.dl.alipaydev.com/gateway.do");
        alipayConfig.setAppId("9021000156658754");
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
        this.alipayClient=alipayClient;
    }
    /**
     * 传入订单信息，返回一段可直接回显的 form HTML
     */
    public String createForm(String outTradeNo, String totalAmount, String subject) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(totalAmount);
        model.setSubject(subject);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(model);

        // 必须返回 POST 表单，GET 会 405
        return alipayClient.pageExecute(request, "POST").getBody();
    }
}
