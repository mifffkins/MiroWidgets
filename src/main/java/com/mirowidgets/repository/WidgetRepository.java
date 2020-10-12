package com.mirowidgets.repository;

import com.mirowidgets.model.Widget;
import com.mirowidgets.model.WidgetCreationData;
import com.mirowidgets.model.WidgetModificationData;

import java.util.List;
import java.util.UUID;

public interface WidgetRepository {

    Widget create(WidgetCreationData data);

    Widget update(UUID id, WidgetModificationData data);

    Widget delete(UUID id);

    void deleteAll();

    Widget findById(UUID id);

    List<Widget> getWidgets();
}
