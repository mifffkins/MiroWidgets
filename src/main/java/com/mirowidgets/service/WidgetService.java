package com.mirowidgets.service;

import java.util.List;
import java.util.UUID;

import com.mirowidgets.model.Widget;
import com.mirowidgets.model.WidgetCreationData;
import com.mirowidgets.model.WidgetModificationData;

public interface WidgetService {

     Widget create(WidgetCreationData data);

     Widget update(UUID id, WidgetModificationData data);

     Widget delete(UUID id);

     void deleteAll();

     Widget findById(UUID id);

     List<Widget> getWidgets();
}
