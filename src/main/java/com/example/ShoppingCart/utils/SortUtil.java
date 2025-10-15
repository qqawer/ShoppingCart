package com.example.ShoppingCart.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SortUtil {
    // 处理排序的辅助方法
    public static Pageable handleSorting(String sortParam, Pageable originalPageable) {
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
    public static String getSortMessage(String sort) {
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

}
