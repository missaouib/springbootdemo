package com.example.demo.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageUtils {

    private PageUtils() {
    }

    /**
     * @param list
     * @param start 起始编号，0开始
     * @param size
     * @param <T>
     * @return
     */
    public static <T> Page<T> buildByStart(List<T> list, int start, int size) {
        if (start < 0 || size <= 0) {
            return EMPTY_PAGE;
        }
        if (list == null || list.size() == 0) {
            return EMPTY_PAGE;
        }
        List<T> subList = list.stream()
                .skip(start)
                .limit(size)
                .collect(Collectors.toList());
        boolean isLastPage = (start + size) >= list.size();
        return new Page<>(subList, isLastPage);
    }

    /**
     * 分页
     *
     * @param list     对象列表
     * @param pageNum  页码 页码必须从1开始
     * @param pageSize 页容量
     * @return 分页结果
     */
    public static <T> Page<T> buildByPageNum(List<T> list, int pageNum, int pageSize) {
        if (pageNum <= 0 || pageSize <= 0) {
            return EMPTY_PAGE;
        }
        if (list == null || list.size() == 0) {
            return EMPTY_PAGE;
        }

        int skip = (pageNum - 1) * pageSize;
        List<T> subList = list.stream()
                .skip(skip)
                .limit(pageSize)
                .collect(Collectors.toList());
        boolean isLastPage = (skip + pageSize) >= list.size();
        return new Page<>(subList, isLastPage);
    }

    public static boolean isLastPage(List totalList, int pageNum, int pageSize) {
        int skipNextPage = pageNum * pageSize;
        return totalList.size() < skipNextPage;
    }

    private static final Page EMPTY_PAGE = new Page<>(new ArrayList<>(0), true);

    @Data
    @AllArgsConstructor
    public static class Page<T> {
        private List<T> data;
        private boolean isLastPage;
    }
}
