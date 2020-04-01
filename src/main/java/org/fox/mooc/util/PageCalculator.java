package org.fox.mooc.util;

public class PageCalculator {
    /**
     * 计算当前页对应数据库中多少行
     * @param pageIndex
     * @param pageSize
     * @return rowIndex
     */
    public static int calculateRowIndex(int pageIndex,int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }
}
