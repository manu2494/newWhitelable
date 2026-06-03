package com.merkle.xp.whitelabel.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.merkle.xp.whitelabel.core.config.InstanceTypeConfig;

@ExtendWith({MockitoExtension.class})
public class InstanceTypeServiceImplTest {

    private final InstanceTypeServiceImpl service = new InstanceTypeServiceImpl();

    @Mock
    InstanceTypeConfig configuration;

    @BeforeEach
    void setUp() {

        when(configuration.isAuthorInstance()).thenReturn(true);
        when(configuration.runmodes()).thenReturn("author");

        service.activate(configuration);
    }
    
    @Test
    void test_init_service() {
    	assertEquals(service.getRunmodes(), "author");
    	assertEquals(service.runmodes(), "author");
    	assertEquals(service.isAuthorInstance(), true);
    }

}
