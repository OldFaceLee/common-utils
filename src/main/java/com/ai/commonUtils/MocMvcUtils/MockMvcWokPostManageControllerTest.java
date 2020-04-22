/*
package com.ai.commonUtils.MocMvcUtils;


import com.ai.rms.controller.WorkPostManageController;
import com.ai.rms.controller.api.request.workpost.WorkPostManageQueryRequest;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * lixj5
 *//*

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
public class MockMvcWokPostManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkPostManageController workPostManageController;


    @Before
    public void initializeMockMvc(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.workPostManageController).build();
    }


    @Test
    public void test() throws Exception {
        Map<String,Long> params = new HashMap<>();
        params.put("workRoleId", Long.parseLong("9900000001"));
        params.put("roleId",Long.parseLong("3200000001"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1.0.0/rms/workpost/manage/query");


        HttpHeaders header = new HttpHeaders();
        header.add("authorization","bearer X2hvRcJfFdgnK2gK0GJLrpCT88AgJ6sn");
        header.add("Content-Type","application/json");
        header.add("Tenant-Id","32");


        MvcResult result = mockMvc.perform(request.contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(params)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());


    }

}
*/
