package com.example.ShoppingCart.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SortUtil {
    // handle sorting
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

    // generate sorting message
    public static String getSortMessage(String sort) {
        if (sort == null || sort.isEmpty()) {
            return "Default order";
        }

        switch (sort) {
            case "price,asc":
                return "Price ascending";
            case "price,desc":
                return "Price descending";
            default:
                return "Specified order";
        }
    }

}
