package com.parks.chparkapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parks.chparkapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("Test Operation")
    public void createEvent() throws Exception {

        EventDTO event = EventDTO.builder()
                .name("Spring")
                .description("rest api development")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 10, 31, 17, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 11, 1, 17, 50))
                .beginEventDateTime(LocalDateTime.of(2020,11,1,18,10))
                .endEventDateTime(LocalDateTime.of(2020,11,1,20,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 신분당선")
                .build();

        mockMvc.perform( post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect( status().isCreated() )
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    @TestDescription("Test Operation - BadRequest")
    public void createEvent_Bad_Request() throws Exception {

        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("rest api development")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 10, 31, 17, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 11, 1, 17, 50))
                .beginEventDateTime(LocalDateTime.of(2020,11,1,18,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 신분당선")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform( post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        )
                .andDo(print())
                .andExpect( status().isBadRequest() );
    }

    @Test
    @TestDescription("Test Operation - emptyInput")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
         EventDTO eventDTO = EventDTO.builder().build();

         this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDTO)))
                 .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("Test Operation - wrongInput")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .name("Spring")
                .description("rest api development")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 10, 31, 17, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 10, 31, 17, 50))
                .beginEventDateTime(LocalDateTime.of(2020,10,31,18,10))
                .endEventDateTime(LocalDateTime.of(2020,10,1,18,10))
                .basePrice(100)
                .maxPrice(50)
                .limitOfEnrollment(100)
                .location("강남역 신분당선")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());

    }

}
