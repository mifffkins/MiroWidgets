package com.mirowidgets.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class WidgetCreationData {

    @NotNull
    private final Integer x;

    @NotNull
    private final Integer y;

    private final Integer z;

    @NotNull
    @Positive(message = "value must be positive")
    private final Double width;

    @NotNull
    @Positive(message = "value must be positive")
    private final Double height;

    public WidgetCreationData(@NotNull Integer x
            , @NotNull Integer y
            , Integer z
            , @NotNull @Positive Double width
            , @NotNull @Positive Double height) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WidgetCreationData that = (WidgetCreationData) o;
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