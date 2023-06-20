package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WorkerProfile;
import com.mycompany.myapp.domain.enumeration.WorkerProfileStatus;
import com.mycompany.myapp.repository.WorkerProfileRepository;
import com.mycompany.myapp.service.WorkerProfileService;
import com.mycompany.myapp.service.dto.WorkerProfileDTO;
import com.mycompany.myapp.service.mapper.WorkerProfileMapper;
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
 * Integration tests for the {@link WorkerProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkerProfileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final WorkerProfileStatus DEFAULT_STATUS = WorkerProfileStatus.ACTIVE;
    private static final WorkerProfileStatus UPDATED_STATUS = WorkerProfileStatus.DISABLED;

    private static final String ENTITY_API_URL = "/api/worker-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkerProfileRepository workerProfileRepository;

    @Mock
    private WorkerProfileRepository workerProfileRepositoryMock;

    @Autowired
    private WorkerProfileMapper workerProfileMapper;

    @Mock
    private WorkerProfileService workerProfileServiceMock;

    @Autowired
    private MockMvc restWorkerProfileMockMvc;

    private WorkerProfile workerProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkerProfile createEntity() {
        WorkerProfile workerProfile = new WorkerProfile().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return workerProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkerProfile createUpdatedEntity() {
        WorkerProfile workerProfile = new WorkerProfile().name(UPDATED_NAME).status(UPDATED_STATUS);
        return workerProfile;
    }

    @BeforeEach
    public void initTest() {
        workerProfileRepository.deleteAll();
        workerProfile = createEntity();
    }

    @Test
    void createWorkerProfile() throws Exception {
        int databaseSizeBeforeCreate = workerProfileRepository.findAll().size();
        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);
        restWorkerProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeCreate + 1);
        WorkerProfile testWorkerProfile = workerProfileList.get(workerProfileList.size() - 1);
        assertThat(testWorkerProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkerProfile.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createWorkerProfileWithExistingId() throws Exception {
        // Create the WorkerProfile with an existing ID
        workerProfile.setId(1L);
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        int databaseSizeBeforeCreate = workerProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkerProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerProfileRepository.findAll().size();
        // set the field null
        workerProfile.setName(null);

        // Create the WorkerProfile, which fails.
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        restWorkerProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = workerProfileRepository.findAll().size();
        // set the field null
        workerProfile.setStatus(null);

        // Create the WorkerProfile, which fails.
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        restWorkerProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllWorkerProfiles() throws Exception {
        // Initialize the database
        workerProfileRepository.save(workerProfile);

        // Get all the workerProfileList
        restWorkerProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkerProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(workerProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkerProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workerProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkerProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workerProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkerProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(workerProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getWorkerProfile() throws Exception {
        // Initialize the database
        workerProfileRepository.save(workerProfile);

        // Get the workerProfile
        restWorkerProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, workerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workerProfile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingWorkerProfile() throws Exception {
        // Get the workerProfile
        restWorkerProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingWorkerProfile() throws Exception {
        // Initialize the database
        workerProfileRepository.save(workerProfile);

        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();

        // Update the workerProfile
        WorkerProfile updatedWorkerProfile = workerProfileRepository.findById(workerProfile.getId()).get();
        updatedWorkerProfile.name(UPDATED_NAME).status(UPDATED_STATUS);
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(updatedWorkerProfile);

        restWorkerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workerProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfile testWorkerProfile = workerProfileList.get(workerProfileList.size() - 1);
        assertThat(testWorkerProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkerProfile.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workerProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWorkerProfileWithPatch() throws Exception {
        // Initialize the database
        workerProfileRepository.save(workerProfile);

        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();

        // Update the workerProfile using partial update
        WorkerProfile partialUpdatedWorkerProfile = new WorkerProfile();
        partialUpdatedWorkerProfile.setId(workerProfile.getId());

        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkerProfile))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfile testWorkerProfile = workerProfileList.get(workerProfileList.size() - 1);
        assertThat(testWorkerProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkerProfile.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void fullUpdateWorkerProfileWithPatch() throws Exception {
        // Initialize the database
        workerProfileRepository.save(workerProfile);

        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();

        // Update the workerProfile using partial update
        WorkerProfile partialUpdatedWorkerProfile = new WorkerProfile();
        partialUpdatedWorkerProfile.setId(workerProfile.getId());

        partialUpdatedWorkerProfile.name(UPDATED_NAME).status(UPDATED_STATUS);

        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkerProfile))
            )
            .andExpect(status().isOk());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
        WorkerProfile testWorkerProfile = workerProfileList.get(workerProfileList.size() - 1);
        assertThat(testWorkerProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkerProfile.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workerProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWorkerProfile() throws Exception {
        int databaseSizeBeforeUpdate = workerProfileRepository.findAll().size();
        workerProfile.setId(count.incrementAndGet());

        // Create the WorkerProfile
        WorkerProfileDTO workerProfileDTO = workerProfileMapper.toDto(workerProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workerProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkerProfile in the database
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWorkerProfile() throws Exception {
        // Initialize the database
        workerProfileRepository.save(workerProfile);

        int databaseSizeBeforeDelete = workerProfileRepository.findAll().size();

        // Delete the workerProfile
        restWorkerProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, workerProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkerProfile> workerProfileList = workerProfileRepository.findAll();
        assertThat(workerProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
