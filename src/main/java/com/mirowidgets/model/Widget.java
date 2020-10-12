package com.mirowidgets.model;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

public class Widget {

    private final UUID id;
    private int x;
    private int y;
    private int z;
    private double width;
    private double height;
    private ZonedDateTime lastModified;

    public Widget(UUID id,
                  Integer x,
                  Integer y,
                  Integer z,
                  Double width,
                  Double height,
                  ZonedDateTime lastModified) {
        this.id = requireNonNull(id, "id cannot be empty.");
        this.x = requireNonNull(x, "x cannot be empty.");
        this.y = requireNonNull(y, "y cannot be empty.");
        this.z = requireNonNull(z, "z cannot be empty.");
        this.width = requireNonNull(width, "width cannot be empty.");
        this.height = requireNonNull(height, "height cannot be empty.");
        this.lastModified = requireNonNull(lastModified, "lastModified cannot be empty.");
    }

    public UUID getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Widget widget = (Widget) o;
        return x == widget.x
                && y == widget.y
                && z == widget.z
                && Double.compare(widget.width, width) == 0
                && Double.compare(widget.height, height) == 0
                && id.equals(widget.id)
                && lastModified.equals(widget.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, z, width, height, lastModified);
    }

    @Override
    public String toString() {
        return "Widget{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", width=" + width +
                ", height=" + height +
                ", lastModified=" + lastModified +
                '}';
    }
}