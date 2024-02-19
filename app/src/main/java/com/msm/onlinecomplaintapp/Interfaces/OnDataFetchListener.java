package com.msm.onlinecomplaintapp.Interfaces;

import java.util.List;

public interface OnDataFetchListener<T> {
    public void onDataFetched(List<T> tList);
}
