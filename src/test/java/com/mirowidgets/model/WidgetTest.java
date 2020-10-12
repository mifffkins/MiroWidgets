package com.mirowidgets.model;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WidgetTest {

    @Test
    public void testWidgetCreation() {
        ZonedDateTime time = ZonedDateTime.now();
        UUID id = UUID.randomUUID();
        final Widget widget = new Widget(id
                                        , 50
                                        , 20
                                        , 0
                                        , 50.5
                                        , 10.4
                                        , time
        );

        assertEquals(widget.getId(),id);
        assertEquals(widget.getX(),50);
        assertEquals(widget.getY(),20);
        assertEquals(widget.getZ(),0);
        assertEquals(widget.getWidth(),50.5);
        assertEquals(widget.getHeight(),10.4);
        assertEquals(widget.getLastModified(), time);

        widget.setX(51);
        widget.setY(21);
        widget.setZ(1);
        widget.setWidth(10.2);
        widget.setHeight(5.1);
        widget.setLastModified(time);

        assertEquals(widget.toString(), "Widget{" +
                "id=" + id +
                ", x=51" +
                ", y=21" +
                ", z=1" +
                ", width=10.2" +
                ", height=5.1" +
                ", lastModified=" + time +
                '}'
        );
    }
}