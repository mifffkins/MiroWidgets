package com.mirowidgets.page;

import java.util.List;

public interface Page<T> {

    int getOffset();

    int getLimit();

    int getTotalElements();

    List<T> getData();
}

