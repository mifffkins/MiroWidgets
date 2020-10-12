package com.mirowidgets.controller;

import com.mirowidgets.model.WidgetModificationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import com.mirowidgets.model.Widget;
import com.mirowidgets.model.WidgetCreationData;
import com.mirowidgets.service.WidgetService;


@RestController
@RequestMapping(path = WidgetController.ROOT_ENDPOINT_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class WidgetController {

    public static final String ROOT_ENDPOINT_NAME = "/widgets";

    private final WidgetService widgetService;

    @Autowired
    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping
    public ResponseEntity<Widget> create(@Valid @RequestBody WidgetCreationData data) {
        final Widget widget = widgetService.create(data);
        return new ResponseEntity<>(widget, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Widget>> getWidgets() {
        final List<Widget> widgets = widgetService.getWidgets();

        return new ResponseEntity<>(widgets, HttpStatus.OK);
     }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Widget> findById(@PathVariable("id") UUID id) {
        final Widget widget = widgetService.findById(id);

        return new ResponseEntity<>(widget, HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Widget> update(@PathVariable("id") UUID id, @Valid @RequestBody WidgetModificationData data) {
        final Widget widget = widgetService.update(id, data);

        return new ResponseEntity<>(widget, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Widget> delete(@PathVariable("id") UUID id) {
        final Widget widget = widgetService.delete(id);

        return new ResponseEntity<>(widget, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Widget> deleteAll() {
        widgetService.deleteAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
