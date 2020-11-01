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

    @Test
    public void testFree() {
        //Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isFree()).isTrue();


        //Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isFree()).isFalse();


        //Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isFree()).isTrue();
    }

    @Test
    public void testOffline() {
        //Given
        Event event = Event.builder()
                .location("강남역 신분당선")
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isOffline()).isTrue();

        //Given
        event = Event.builder()
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isOffline()).isFalse();
    }

}