package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WaitingRoom;
import com.mycompany.myapp.domain.enumeration.WaitingRoomStatus;
import com.mycompany.myapp.repository.WaitingRoomRepository;
import com.mycompany.myapp.service.WaitingRoomService;
import com.mycompany.myapp.service.dto.WaitingRoomDTO;
import com.mycompany.myapp.service.mapper.WaitingRoomMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link WaitingRoomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WaitingRoomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final WaitingRoomStatus DEFAULT_STATUS = WaitingRoomStatus.ACTIVE;
    private static final WaitingRoomStatus UPDATED_STATUS = WaitingRoomStatus.DISABLED;

    private static final String ENTITY_API_URL = "/api/waiting-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WaitingRoomRepository waitingRoomRepository;

    @Mock
    private WaitingRoomRepository waitingRoomRepositoryMock;

    @Autowired
    private WaitingRoomMapper waitingRoomMapper;

    @Mock
    private WaitingRoomService waitingRoomServiceMock;

    @Autowired
    private MockMvc restWaitingRoomMockMvc;

    private WaitingRoom waitingRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaitingRoom createEntity() {
        WaitingRoom waitingRoom = new WaitingRoom().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return waitingRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WaitingRoom createUpdatedEntity() {
        WaitingRoom waitingRoom = new WaitingRoom().name(UPDATED_NAME).status(UPDATED_STATUS);
        return waitingRoom;
    }

    @BeforeEach
    public void initTest() {
        waitingRoomRepository.deleteAll();
        waitingRoom = createEntity();
    }

    @Test
    void createWaitingRoom() throws Exception {
        int databaseSizeBeforeCreate = waitingRoomRepository.findAll().size();
        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);
        restWaitingRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeCreate + 1);
        WaitingRoom testWaitingRoom = waitingRoomList.get(waitingRoomList.size() - 1);
        assertThat(testWaitingRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWaitingRoom.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createWaitingRoomWithExistingId() throws Exception {
        // Create the WaitingRoom with an existing ID
        waitingRoom.setId(1L);
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        int databaseSizeBeforeCreate = waitingRoomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaitingRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = waitingRoomRepository.findAll().size();
        // set the field null
        waitingRoom.setName(null);

        // Create the WaitingRoom, which fails.
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        restWaitingRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = waitingRoomRepository.findAll().size();
        // set the field null
        waitingRoom.setStatus(null);

        // Create the WaitingRoom, which fails.
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        restWaitingRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllWaitingRooms() throws Exception {
        // Initialize the database
        waitingRoomRepository.save(waitingRoom);

        // Get all the waitingRoomList
        restWaitingRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waitingRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWaitingRoomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(waitingRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWaitingRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(waitingRoomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWaitingRoomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(waitingRoomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWaitingRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(waitingRoomRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getWaitingRoom() throws Exception {
        // Initialize the database
        waitingRoomRepository.save(waitingRoom);

        // Get the waitingRoom
        restWaitingRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, waitingRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waitingRoom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingWaitingRoom() throws Exception {
        // Get the waitingRoom
        restWaitingRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingWaitingRoom() throws Exception {
        // Initialize the database
        waitingRoomRepository.save(waitingRoom);

        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();

        // Update the waitingRoom
        WaitingRoom updatedWaitingRoom = waitingRoomRepository.findById(waitingRoom.getId()).get();
        updatedWaitingRoom.name(UPDATED_NAME).status(UPDATED_STATUS);
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(updatedWaitingRoom);

        restWaitingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waitingRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoom testWaitingRoom = waitingRoomList.get(waitingRoomList.size() - 1);
        assertThat(testWaitingRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaitingRoom.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, waitingRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWaitingRoomWithPatch() throws Exception {
        // Initialize the database
        waitingRoomRepository.save(waitingRoom);

        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();

        // Update the waitingRoom using partial update
        WaitingRoom partialUpdatedWaitingRoom = new WaitingRoom();
        partialUpdatedWaitingRoom.setId(waitingRoom.getId());

        partialUpdatedWaitingRoom.name(UPDATED_NAME).status(UPDATED_STATUS);

        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaitingRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWaitingRoom))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoom testWaitingRoom = waitingRoomList.get(waitingRoomList.size() - 1);
        assertThat(testWaitingRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaitingRoom.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdateWaitingRoomWithPatch() throws Exception {
        // Initialize the database
        waitingRoomRepository.save(waitingRoom);

        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();

        // Update the waitingRoom using partial update
        WaitingRoom partialUpdatedWaitingRoom = new WaitingRoom();
        partialUpdatedWaitingRoom.setId(waitingRoom.getId());

        partialUpdatedWaitingRoom.name(UPDATED_NAME).status(UPDATED_STATUS);

        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWaitingRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWaitingRoom))
            )
            .andExpect(status().isOk());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
        WaitingRoom testWaitingRoom = waitingRoomList.get(waitingRoomList.size() - 1);
        assertThat(testWaitingRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWaitingRoom.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, waitingRoomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWaitingRoom() throws Exception {
        int databaseSizeBeforeUpdate = waitingRoomRepository.findAll().size();
        waitingRoom.setId(count.incrementAndGet());

        // Create the WaitingRoom
        WaitingRoomDTO waitingRoomDTO = waitingRoomMapper.toDto(waitingRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWaitingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(waitingRoomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WaitingRoom in the database
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWaitingRoom() throws Exception {
        // Initialize the database
        waitingRoomRepository.save(waitingRoom);

        int databaseSizeBeforeDelete = waitingRoomRepository.findAll().size();

        // Delete the waitingRoom
        restWaitingRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, waitingRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WaitingRoom> waitingRoomList = waitingRoomRepository.findAll();
        assertThat(waitingRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
