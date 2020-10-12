package com.mirowidgets.repository;

import com.mirowidgets.model.Widget;
import com.mirowidgets.model.WidgetCreationData;
import com.mirowidgets.model.WidgetModificationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class WidgetBaseRepository implements WidgetRepository{

    private final ConcurrentHashMap<UUID, Widget> widgets = new ConcurrentHashMap<>();
    private final ConcurrentSkipListMap<Integer, Widget> zIndex = new ConcurrentSkipListMap<>();

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    @Autowired
    public WidgetBaseRepository() {
    }

    @Override
    public Widget create(WidgetCreationData data) {
        writeLock.lock();
        try {
            Integer z = data.getZ();
            if (z == null) {
                z = zIndex.isEmpty() ? 0 : zIndex.lastKey() + 1;
            } else {
                if (zIndex.containsKey(z)) {
                    makeGap(z);
                }
            }

            Widget widget = new Widget(UUID.randomUUID()
                    , data.getX()
                    , data.getY()
                    , z
                    , data.getWidth()
                    , data.getHeight()
                    , ZonedDateTime.now()
            );
            widgets.put(widget.getId(),widget);
            zIndex.put(z, widget);

            return widget;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Widget update(UUID id, WidgetModificationData data) {
        writeLock.lock();
        try {
            Widget widget = widgets.get(id);
            if (widget != null) {
                boolean addToZIndex = false;
                final Integer z = data.getZ();
                if (z != null && z != widget.getZ()) {
                    zIndex.remove(widget.getZ());
                    addToZIndex = true;
                    if (zIndex.containsKey(z)) {
                        makeGap(z);
                    }
                }
                data.modifyWidget(widget);
                if (addToZIndex) {
                    zIndex.put(z, widget);
                }
            }

            return widget;

        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Widget delete(UUID id) {
        writeLock.lock();
        try {
            Widget widget = widgets.get(id);
            if (widget != null) {
                widgets.remove(id);
                zIndex.remove(widget.getZ());
            }

            return widget;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void deleteAll() {
        writeLock.lock();
        try {
            widgets.clear();
            zIndex.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Widget findById(UUID id) {
        readLock.lock();
        try {
            return widgets.get(id);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Widget> getWidgets() {
        readLock.lock();
        try {
             return new ArrayList<>(zIndex.values());
        } finally {
            readLock.unlock();
        }
    }

    private void makeGap(Integer z) {
        final ConcurrentNavigableMap<Integer, Widget> checkMap = zIndex.tailMap(z);
        if (checkMap.isEmpty()) {
            return;
        }

        final Collection<Widget> checkValues = checkMap.values();
        final Iterator<Widget> checkIterator = checkValues.iterator();
        final List<Widget> needUpdateZ = new ArrayList<>(checkValues.size());
        int curZ = z;
        boolean doNext = true;
        while (doNext && checkIterator.hasNext()) {
            Widget curWidget = checkIterator.next();
            if (curZ == curWidget.getZ()) {
                needUpdateZ.add(curWidget);
                checkIterator.remove();
                curWidget.setZ(++curZ);
            } else {
                doNext = false;
            }
        }
        for (final Widget widget : needUpdateZ) {
            zIndex.put(widget.getZ(), widget);
        }
    }
}