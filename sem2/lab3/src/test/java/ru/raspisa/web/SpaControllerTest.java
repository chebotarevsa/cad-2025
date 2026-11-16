package ru.raspisa.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpaControllerTest {
    @Test void mapCanBeOpenedDirectlyWithOrWithoutTrailingSlash() throws Exception {
        var mvc = MockMvcBuilders.standaloneSetup(new SpaController()).build();
        for (String path : new String[]{"/map", "/map/"}) {
            mvc.perform(get(path)).andExpect(status().isOk()).andExpect(forwardedUrl("/index.html"));
        }
    }
}
