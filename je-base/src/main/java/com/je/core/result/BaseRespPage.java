package com.je.core.result;

import java.io.Serializable;
import java.util.List;

public class BaseRespPage <T> implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long totalCount;
    private List<T> list;

    public BaseRespPage(Long totalCount, List<T> list) {
        this.totalCount = totalCount;
        this.list = list;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
