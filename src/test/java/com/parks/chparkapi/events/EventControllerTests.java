package com.parks.chparkapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parks.chparkapi.common.RestDocsConfiguration;
import com.parks.chparkapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.JsonPath;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

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
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-event").description("link to update-event"),
                                linkWithRel("profile").description("link to profile")
                        ),

                        //request Docs
                        requestHeaders(
                            headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                            fieldWithPath("name").description("name of New event"),
                            fieldWithPath("description").description("description of New event"),
                            fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of New event"),
                            fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of New event"),
                            fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of New event"),
                            fieldWithPath("beginEventDateTime").description("beginEventDateTime of New event"),
                            fieldWithPath("endEventDateTime").description("endEventDateTime of New event"),
                            fieldWithPath("location").description("location of New event"),
                            fieldWithPath("basePrice").description("basePrice of New event"),
                            fieldWithPath("maxPrice").description("maxPrice of New event"),
                            fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of New event")
                        ),

                        //response Docs
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        //모든 응답이 아닌 일부분의 응답만을 문서화 하고 싶은 경우
                        //prefix : relaxed -> 1. 문서 일부분만 테스트 가능 2. 정확한 문서를 생성하지는 못함
                        //가능한 경우에는 모든 응답을 문서화
                        responseFields(
                                fieldWithPath("id").description("id of New event"),
                                fieldWithPath("name").description("name of New event"),
                                fieldWithPath("description").description("description of New event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of New event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of New event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of New event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of New event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of New event"),
                                fieldWithPath("location").description("location of New event"),
                                fieldWithPath("basePrice").description("basePrice of New event"),
                                fieldWithPath("maxPrice").description("maxPrice of New event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of New event"),
                                fieldWithPath("free").description("free of New event"),
                                fieldWithPath("offline").description("offline of New event"),
                                fieldWithPath("eventStatus").description("eventStatus of New event"),

                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-event.href").description("link to update-event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
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
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회")
    public void queryEvents() throws Exception {
        //Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        //When & Then
        this.mockMvc.perform(get("/api/events")
                    .param("page", "1")
                    .param("size","10")
                    .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
    }

    @Test
    @TestDescription("기존의 이벤트 1개 조회하기")
    public void getEvent() throws Exception {
        //Given
        Event event = this.generateEvent(100);

        //When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
        ;
    }

    @Test
    @TestDescription("없는 이벤트를 조회했을때 404 응답받기")
    public void getEvent404() throws Exception {
        //When & Then
        this.mockMvc.perform(get("/api/events/13333"))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("이벤트 수정하기")
    public void updateEvent() throws Exception {
        //Given
        Event event = this.generateEvent(200);
        String eventName = "Updated Event";
        EventDTO eventDTO = this.modelMapper.map(event, EventDTO.class);
        eventDTO.setName(eventName);

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));
    }

    @Test
    @TestDescription("입력값이 비어있는 경우 이벤트 수정실패")
    public void updateEvent400Empty() throws Exception {
        //Given
        Event event = this.generateEvent(200);
        EventDTO eventDTO = new EventDTO();

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 잘못된 경우 이벤트 수정실패")
    public void updateEvent400_Wrong() throws Exception {
        //Given
        Event event = this.generateEvent(200);
        EventDTO eventDTO = this.modelMapper.map(event, EventDTO.class);
        eventDTO.setBasePrice(2000);
        eventDTO.setMaxPrice(100);

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정실패")
    public void updateEvent404() throws Exception {
        //Given
        Event event = this.generateEvent(200);
        EventDTO eventDTO = this.modelMapper.map(event, EventDTO.class);

        //When & Then
        this.mockMvc.perform(put("/api/events/20000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 10, 31, 17, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 11, 1, 17, 50))
                .beginEventDateTime(LocalDateTime.of(2020,11,1,18,10))
                .endEventDateTime(LocalDateTime.of(2020,11,1,20,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 신분당선")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return this.eventRepository.save(event);
    }
}
