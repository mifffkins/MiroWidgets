package com.mirowidgets.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.constraints.Positive;

public class WidgetModificationData {

    private final Integer x;

    private final Integer y;

    private final Integer z;

    @Positive(message = "value must be positive")
    private final Double width;

    @Positive(message = "value must be positive")
    private final Double height;

    public WidgetModificationData(Integer x
                                  , Integer y
                                  , Integer z
                                  , Double width
                                  , Double height) {

        if (x == null && y == null && z == null && width == null && height == null) {
            throw new IllegalArgumentException("Nothing to update");
        }
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public void modifyWidget(Widget widget) {
        boolean hasChanged = false;

        if (x != null && x != widget.getX()) {
            widget.setX(x);
            hasChanged = true;
        }
        if (y != null && y != widget.getY()) {
            widget.setY(y);
            hasChanged = true;
        }
        if (z != null && z != widget.getZ()) {
            widget.setZ(z);
            hasChanged = true;
        }
        if (width != null && width != widget.getWidth()) {
            widget.setWidth(width);
            hasChanged = true;
        }
        if (height != null && height != widget.getHeight()) {
            widget.setHeight(height);
            hasChanged = true;
        }

        if (hasChanged) {
            widget.setLastModified(ZonedDateTime.now());
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WidgetModificationData that = (WidgetModificationData) o;
        return Objects.equals(x, that.x)
                && Objects.equals(y, that.y)
                && Objects.equals(z, that.z)
                && Objects.equals(width, that.width)
                && Objects.equals(height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, width, height);
    }

    @Override
    public String toString() {
        return "WidgetModificationData{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}