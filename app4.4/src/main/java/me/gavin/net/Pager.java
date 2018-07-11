package me.gavin.net;

import java.util.List;

/**
 * 分页数据
 * <p>
 * private int total;
 * private int totalCount;
 * private boolean pagination;
 * private int pageSize;
 * private int totalPage;
 * private int pageNo;
 * private boolean firstPage;
 * private boolean lastPage;
 * private int prePage;
 * private int nextPage;
 * private Object clazz;
 * private int firstResult;
 * private String totalFieldName;
 * private List<?> footer;
 * private List<T> list;
 *
 * @author gavin.xiong 2018/7/2 0002
 */
public final class Pager<T> {

    private int totalCount;
    private int pageSize;
    private int totalPage;
    private int pageNo;
    private boolean firstPage;
    private boolean lastPage;
    private int prePage;
    private int nextPage;
    private String totalFieldName;
    private List<T> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public String getTotalFieldName() {
        return totalFieldName;
    }

    public void setTotalFieldName(String totalFieldName) {
        this.totalFieldName = totalFieldName;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
