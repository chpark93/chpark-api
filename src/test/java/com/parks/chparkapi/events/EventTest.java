package com.parks.chparkapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    public void builder() {

        Event event = Event.builder()
                .name("chpark's rest")
                .description("chpark's rest description")
                .build();
        assertThat(event).isNotNull();
    }

}