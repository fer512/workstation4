package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Queue;
import com.mycompany.myapp.domain.enumeration.QueueStatus;
import com.mycompany.myapp.repository.QueueRepository;
import com.mycompany.myapp.service.QueueService;
import com.mycompany.myapp.service.dto.QueueDTO;
import com.mycompany.myapp.service.mapper.QueueMapper;
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
 * Integration tests for the {@link QueueResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QueueResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final QueueStatus DEFAULT_STATUS = QueueStatus.ACTIVE;
    private static final QueueStatus UPDATED_STATUS = QueueStatus.DISABLED;

    private static final String ENTITY_API_URL = "/api/queues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QueueRepository queueRepository;

    @Mock
    private QueueRepository queueRepositoryMock;

    @Autowired
    private QueueMapper queueMapper;

    @Mock
    private QueueService queueServiceMock;

    @Autowired
    private MockMvc restQueueMockMvc;

    private Queue queue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Queue createEntity() {
        Queue queue = new Queue().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return queue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Queue createUpdatedEntity() {
        Queue queue = new Queue().name(UPDATED_NAME).status(UPDATED_STATUS);
        return queue;
    }

    @BeforeEach
    public void initTest() {
        queueRepository.deleteAll();
        queue = createEntity();
    }

    @Test
    void createQueue() throws Exception {
        int databaseSizeBeforeCreate = queueRepository.findAll().size();
        // Create the Queue
        QueueDTO queueDTO = queueMapper.toDto(queue);
        restQueueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queueDTO)))
            .andExpect(status().isCreated());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeCreate + 1);
        Queue testQueue = queueList.get(queueList.size() - 1);
        assertThat(testQueue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQueue.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createQueueWithExistingId() throws Exception {
        // Create the Queue with an existing ID
        queue.setId(1L);
        QueueDTO queueDTO = queueMapper.toDto(queue);

        int databaseSizeBeforeCreate = queueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQueueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = queueRepository.findAll().size();
        // set the field null
        queue.setName(null);

        // Create the Queue, which fails.
        QueueDTO queueDTO = queueMapper.toDto(queue);

        restQueueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queueDTO)))
            .andExpect(status().isBadRequest());

        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = queueRepository.findAll().size();
        // set the field null
        queue.setStatus(null);

        // Create the Queue, which fails.
        QueueDTO queueDTO = queueMapper.toDto(queue);

        restQueueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queueDTO)))
            .andExpect(status().isBadRequest());

        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllQueues() throws Exception {
        // Initialize the database
        queueRepository.save(queue);

        // Get all the queueList
        restQueueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(queue.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQueuesWithEagerRelationshipsIsEnabled() throws Exception {
        when(queueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQueueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(queueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQueuesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(queueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQueueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(queueRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getQueue() throws Exception {
        // Initialize the database
        queueRepository.save(queue);

        // Get the queue
        restQueueMockMvc
            .perform(get(ENTITY_API_URL_ID, queue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(queue.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingQueue() throws Exception {
        // Get the queue
        restQueueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingQueue() throws Exception {
        // Initialize the database
        queueRepository.save(queue);

        int databaseSizeBeforeUpdate = queueRepository.findAll().size();

        // Update the queue
        Queue updatedQueue = queueRepository.findById(queue.getId()).get();
        updatedQueue.name(UPDATED_NAME).status(UPDATED_STATUS);
        QueueDTO queueDTO = queueMapper.toDto(updatedQueue);

        restQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(queueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
        Queue testQueue = queueList.get(queueList.size() - 1);
        assertThat(testQueue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQueue.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(count.incrementAndGet());

        // Create the Queue
        QueueDTO queueDTO = queueMapper.toDto(queue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, queueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(queueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(count.incrementAndGet());

        // Create the Queue
        QueueDTO queueDTO = queueMapper.toDto(queue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(queueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(count.incrementAndGet());

        // Create the Queue
        QueueDTO queueDTO = queueMapper.toDto(queue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(queueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQueueWithPatch() throws Exception {
        // Initialize the database
        queueRepository.save(queue);

        int databaseSizeBeforeUpdate = queueRepository.findAll().size();

        // Update the queue using partial update
        Queue partialUpdatedQueue = new Queue();
        partialUpdatedQueue.setId(queue.getId());

        partialUpdatedQueue.status(UPDATED_STATUS);

        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQueue))
            )
            .andExpect(status().isOk());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
        Queue testQueue = queueList.get(queueList.size() - 1);
        assertThat(testQueue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQueue.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdateQueueWithPatch() throws Exception {
        // Initialize the database
        queueRepository.save(queue);

        int databaseSizeBeforeUpdate = queueRepository.findAll().size();

        // Update the queue using partial update
        Queue partialUpdatedQueue = new Queue();
        partialUpdatedQueue.setId(queue.getId());

        partialUpdatedQueue.name(UPDATED_NAME).status(UPDATED_STATUS);

        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQueue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQueue))
            )
            .andExpect(status().isOk());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
        Queue testQueue = queueList.get(queueList.size() - 1);
        assertThat(testQueue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQueue.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(count.incrementAndGet());

        // Create the Queue
        QueueDTO queueDTO = queueMapper.toDto(queue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, queueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(queueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(count.incrementAndGet());

        // Create the Queue
        QueueDTO queueDTO = queueMapper.toDto(queue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(queueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQueue() throws Exception {
        int databaseSizeBeforeUpdate = queueRepository.findAll().size();
        queue.setId(count.incrementAndGet());

        // Create the Queue
        QueueDTO queueDTO = queueMapper.toDto(queue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQueueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(queueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Queue in the database
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQueue() throws Exception {
        // Initialize the database
        queueRepository.save(queue);

        int databaseSizeBeforeDelete = queueRepository.findAll().size();

        // Delete the queue
        restQueueMockMvc
            .perform(delete(ENTITY_API_URL_ID, queue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Queue> queueList = queueRepository.findAll();
        assertThat(queueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
