package com.mirowidgets.service;

import com.mirowidgets.exception.WidgetNotFoundException;
import com.mirowidgets.model.Widget;
import com.mirowidgets.model.WidgetCreationData;
import com.mirowidgets.model.WidgetModificationData;
import com.mirowidgets.repository.WidgetRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WidgetServiceImpl implements WidgetService {

    private final WidgetRepository widgetRepository;

    public WidgetServiceImpl(final WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public Widget create(WidgetCreationData data) {
        return widgetRepository.create(data);
    }

    @Override
    public Widget update(UUID id, WidgetModificationData data) {
        final Widget widget = widgetRepository.update(id, data);
        if (widget == null)
            throw new WidgetNotFoundException(id);
        return widget;
    }

    @Override
    public Widget delete(UUID id) {
        final Widget widget = widgetRepository.delete(id);
        if (widget == null)
            throw new WidgetNotFoundException(id);
        return widget;
    }

    @Override
    public void deleteAll() {
        widgetRepository.deleteAll();
    }

    @Override
    public Widget findById(UUID id) {
        final Widget widget = widgetRepository.findById(id);
        if (widget == null)
            throw new WidgetNotFoundException(id);
        return widget;
    }

    @Override
    public List<Widget> getWidgets() {
        return widgetRepository.getWidgets();
    }
}