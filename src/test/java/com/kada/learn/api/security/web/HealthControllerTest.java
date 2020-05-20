package com.kada.learn.api.security.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HealthControllerTest {

    @Test
    public void testHealthController(){
        Assertions.assertEquals("Greeting from health controller", new HealthController().getHealth().getBody());
    }
}