package com.mirowidgets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirowidgets.model.Widget;
import com.mirowidgets.model.WidgetCreationData;
import com.mirowidgets.model.WidgetModificationData;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class WidgetControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @After
    public void after() throws Exception{
        mvc.perform(delete(WidgetController.ROOT_ENDPOINT_NAME));
    }

    @Test
    public void testCreateWidgetWithAllDefinedProperties() throws Exception {
        WidgetCreationData creationData = new WidgetCreationData(0, 0, 0, 10.0, 10.0);
        String json = objectMapper.writeValueAsString(creationData);
        ResultActions resultAction = postAndCheckStatus(json, status().isCreated());
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();
        Widget widget = objectMapper.readValue(responseBody, Widget.class);

        assertThat(widget.getId()).isNotNull();
        assertEquals("x is different", creationData.getX(), widget.getX());
        assertEquals("y is different", creationData.getY(), widget.getY());
        assertEquals("z  is different", creationData.getZ(), widget.getZ());
        assertEquals("width is different", creationData.getWidth(), widget.getWidth());
        assertEquals("height is different", creationData.getHeight(), widget.getHeight());
        assertThat(widget.getLastModified()).isNotNull();
    }

    @Test
    public void testCreateWidgetWithEmptyZIndex() throws Exception {
        WidgetCreationData creationData = new WidgetCreationData(0, 0, null, 10.0, 10.0);
        String json = objectMapper.writeValueAsString(creationData);
        ResultActions resultAction = postAndCheckStatus(json, status().isCreated());
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();
        Widget widget = objectMapper.readValue(responseBody, Widget.class);

        assertThat(widget.getId()).isNotNull();
        assertEquals("x is different", creationData.getX(), widget.getX());
        assertEquals("y is different", creationData.getY(), widget.getY());
        assertEquals("z is different", 0, widget.getZ());
        assertEquals("width is different", creationData.getWidth(), widget.getWidth());
        assertEquals("height is different", creationData.getHeight(), widget.getHeight());
        assertThat(widget.getLastModified()).isNotNull();
    }

    @Test
    public void testCreateWidgetAllParamsNull() throws Exception {
        WidgetCreationData creationData = new WidgetCreationData(null, null, null, null, null);
        String json = objectMapper.writeValueAsString(creationData);
        postAndCheckStatus(json, status().isBadRequest());
    }

    @Test
    public void testCreateWidgetWithShift() throws Exception {
        List<WidgetCreationData> datas = new LinkedList<>();
        datas.add(new WidgetCreationData(0, 0, 0, 10.0, 10.0));
        datas.add(new WidgetCreationData(10, 10, 0, 10.0, 10.0));

        for (WidgetCreationData data : datas) {
            String json = objectMapper.writeValueAsString(data);
            postAndCheckStatus(json, status().isCreated());
        }

        ResultActions resultAction = getAndCheckStatus("", status().isOk());
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();

        final int wc = datas.size();
        List<Widget> widgets = Arrays.asList(objectMapper.readValue(responseBody, Widget[].class));
        assertThat(widgets).hasSize(wc);

        WidgetCreationData creationData = datas.get(0);
        Widget widget = widgets.get(1);

        assertEquals("x is different", creationData.getX(), widget.getX());
        assertEquals("y is different", creationData.getY(), widget.getY());
        assertEquals("z is different", 1, widget.getZ());
        assertEquals("width is different", creationData.getWidth(), widget.getWidth());
        assertEquals("height is different", creationData.getHeight(), widget.getHeight());
    }

    @Test
    public void testGetEmptyWidgetList() throws Exception {
        ResultActions resultAction = getAndCheckStatus("", status().isOk());
        resultAction.andExpect(jsonPath("$").value(Matchers.empty()));
    }

    @Test
    public void testGetWidgets() throws Exception {
        List<WidgetCreationData> datas = new LinkedList<>();
        datas.add(new WidgetCreationData(0, 0, 0, 10.0, 10.0));
        datas.add(new WidgetCreationData(10, 10, 10, 10.0, 10.0));

        for (WidgetCreationData data : datas) {
            String json = objectMapper.writeValueAsString(data);
            postAndCheckStatus(json, status().isCreated());
        }

        ResultActions resultAction = getAndCheckStatus("", status().isOk());
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();

        final int wc = datas.size();
        List<Widget> widgets = Arrays.asList(objectMapper.readValue(responseBody, Widget[].class));
        assertThat(widgets).hasSize(wc);

        for (int wi = 1; wi < wc; wi++) {
            final Widget widget = widgets.get(wi);
            final WidgetCreationData creationData = datas.get(wi);

            assertThat(widget.getId()).isNotNull();
            assertEquals("x is different", creationData.getX(), widget.getX());
            assertEquals("y is different", creationData.getY(), widget.getY());
            assertEquals("z is different", creationData.getZ(), widget.getZ());
            assertEquals("width is different", creationData.getWidth(), widget.getWidth());
            assertEquals("height is different", creationData.getHeight(), widget.getHeight());
            assertThat(widget.getLastModified()).isNotNull();
        }
    }

    @Test
    public void testGetWidget() throws Exception {
        WidgetCreationData creationData = new WidgetCreationData(0, 0, 0, 10.0, 10.0);
        String json = objectMapper.writeValueAsString(creationData);
        ResultActions resultAction = postAndCheckStatus(json, status().isCreated());
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();
        Widget widgetResponse = objectMapper.readValue(responseBody, Widget.class);

        ResultActions resultAction1 = getAndCheckStatus("/" + widgetResponse.getId().toString(), status().isOk());
        String responseBody1 = resultAction1.andReturn().getResponse().getContentAsString();
        Widget widgetResponse1 = objectMapper.readValue(responseBody1, Widget.class);
        assertThat(widgetResponse).isEqualTo(widgetResponse1);
    }

    @Test
    public void testGetWidgetThatNotExists() throws Exception {
        getAndCheckStatus("/" + UUID.randomUUID().toString(), status().isNotFound());
    }

    @Test
    public void testModifyWidgetThatNotExists() throws Exception {
        WidgetModificationData modificationData = new WidgetModificationData(1, null, null, null, null);
        String json = objectMapper.writeValueAsString(modificationData);
        patchAndCheckStatus("/" + UUID.randomUUID().toString(), json, status().isNotFound());
    }

    @Test
    public void testSimpleModifyWidgetWithAllDefinedProperties() throws Exception {
        WidgetCreationData creationData = new WidgetCreationData(0, 0, 0, 10.0, 10.0);
        String json = objectMapper.writeValueAsString(creationData);
        ResultActions resultAction = postAndCheckStatus(json, status().isCreated());
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();
        Widget widgetResponse = objectMapper.readValue(responseBody, Widget.class);

        WidgetModificationData modificationData = new WidgetModificationData(15, 10, 20, 5.0, 11.2);
        String json1 = objectMapper.writeValueAsString(modificationData);
        ResultActions resultAction1 = patchAndCheckStatus("/" + widgetResponse.getId(), json1, status().isOk());

        String responseBody1 = resultAction1.andReturn().getResponse().getContentAsString();
        Widget widgetResponse1 = objectMapper.readValue(responseBody1, Widget.class);

        assertEquals("id is different", widgetResponse.getId(), widgetResponse1.getId());
        assertEquals("x is different", 15, widgetResponse1.getX());
        assertEquals("y is different", 10, widgetResponse1.getY());
        assertEquals("z is different", 20, widgetResponse1.getZ());
        assertEquals("width is different", 5.0, widgetResponse1.getWidth());
        assertEquals("height is different", 11.2, widgetResponse1.getHeight());
    }

    @Test
    public void testModifyWidgetWithShift() throws Exception {
        List<WidgetCreationData> datas = new LinkedList<>();
        datas.add(new WidgetCreationData(0, 0, 0, 10.0, 10.0));
        datas.add(new WidgetCreationData(10, 10, 5, 10.0, 10.0));

        for (WidgetCreationData data : datas) {
            String json = objectMapper.writeValueAsString(data);
            postAndCheckStatus(json, status().isCreated());
        }

        ResultActions resultAction = getAndCheckStatus("", status().isOk());
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();

        final int wc = datas.size();
        List<Widget> widgets = Arrays.asList(objectMapper.readValue(responseBody, Widget[].class));

        WidgetModificationData modificationData = new WidgetModificationData(null, null, 5, null, null);
        String json1 = objectMapper.writeValueAsString(modificationData);
        patchAndCheckStatus("/" + widgets.get(0).getId(), json1, status().isOk());

        ResultActions resultAction1 = getAndCheckStatus("", status().isOk());
        String responseBody1 = resultAction1.andReturn().getResponse().getContentAsString();

        List<Widget> widgets1 = Arrays.asList(objectMapper.readValue(responseBody1, Widget[].class));
        assertThat(widgets).hasSize(wc);

        assertEquals("z is different", 5, widgets1.get(0).getZ());
        assertEquals("z is different", 6, widgets1.get(1).getZ());
    }

    @Test
    public void testDeleteWidget() throws Exception {
        WidgetCreationData creationData = new WidgetCreationData(0, 0, 0, 10.0, 10.0);
        String json = objectMapper.writeValueAsString(creationData);
        ResultActions resultAction = postAndCheckStatus(json, status().isCreated());
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();
        Widget widgetResponse = objectMapper.readValue(responseBody, Widget.class);

        ResultActions resultAction1 = deleteAndCheckStatus("/" + widgetResponse.getId().toString(), status().isOk());
        String responseBody1 = resultAction1.andReturn().getResponse().getContentAsString();
        Widget widgetResponse1 = objectMapper.readValue(responseBody1, Widget.class);

        assertThat(widgetResponse).isEqualTo(widgetResponse1);

        ResultActions resultAction2 = getAndCheckStatus("", status().isOk());
        resultAction2.andExpect(jsonPath("$").value(Matchers.empty()));
    }

    @Test
    public void testDeleteWidgetThatNotExists() throws Exception {
        deleteAndCheckStatus("/" + UUID.randomUUID().toString(), status().isNotFound());
    }

    @Test
    public void testDeleteAllWidgets() throws Exception {
        List<WidgetCreationData> datas = new LinkedList<>();
        datas.add(new WidgetCreationData(0, 0, 0, 10.0, 10.0));
        datas.add(new WidgetCreationData(10, 10, 5, 10.0, 10.0));

        for (WidgetCreationData data : datas) {
            String json = objectMapper.writeValueAsString(data);
            postAndCheckStatus(json, status().isCreated());
        }

        deleteAndCheckStatus("", status().isOk());

        ResultActions resultAction = getAndCheckStatus("", status().isOk());
        resultAction.andExpect(jsonPath("$").value(Matchers.empty()));
    }

    private ResultActions postAndCheckStatus(String json, ResultMatcher resultMatcher) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .post(WidgetController.ROOT_ENDPOINT_NAME)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(resultMatcher);
    }

    private ResultActions getAndCheckStatus(String endPoint, ResultMatcher resultMatcher) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .get(WidgetController.ROOT_ENDPOINT_NAME + endPoint))
                .andExpect(resultMatcher);
    }

    private ResultActions patchAndCheckStatus(String endPoint, String json, ResultMatcher resultMatcher) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .patch(WidgetController.ROOT_ENDPOINT_NAME + endPoint)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(resultMatcher);
    }

    private ResultActions deleteAndCheckStatus(String endPoint, ResultMatcher resultMatcher) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                .delete(WidgetController.ROOT_ENDPOINT_NAME + endPoint))
                .andExpect(resultMatcher);
    }
}
