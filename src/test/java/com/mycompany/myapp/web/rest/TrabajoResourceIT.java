package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Trabajo;
import com.mycompany.myapp.repository.TrabajoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TrabajoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrabajoResourceIT {

    private static final String DEFAULT_TITULO_TRABAJO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO_TRABAJO = "BBBBBBBBBB";

    private static final Long DEFAULT_SALARIO_MIN = 1L;
    private static final Long UPDATED_SALARIO_MIN = 2L;

    private static final Long DEFAULT_SALARIO_MAX = 1L;
    private static final Long UPDATED_SALARIO_MAX = 2L;

    private static final String ENTITY_API_URL = "/api/trabajos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Mock
    private TrabajoRepository trabajoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrabajoMockMvc;

    private Trabajo trabajo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajo createEntity(EntityManager em) {
        Trabajo trabajo = new Trabajo()
            .tituloTrabajo(DEFAULT_TITULO_TRABAJO)
            .salarioMin(DEFAULT_SALARIO_MIN)
            .salarioMax(DEFAULT_SALARIO_MAX);
        return trabajo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajo createUpdatedEntity(EntityManager em) {
        Trabajo trabajo = new Trabajo()
            .tituloTrabajo(UPDATED_TITULO_TRABAJO)
            .salarioMin(UPDATED_SALARIO_MIN)
            .salarioMax(UPDATED_SALARIO_MAX);
        return trabajo;
    }

    @BeforeEach
    public void initTest() {
        trabajo = createEntity(em);
    }

    @Test
    @Transactional
    void createTrabajo() throws Exception {
        int databaseSizeBeforeCreate = trabajoRepository.findAll().size();
        // Create the Trabajo
        restTrabajoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isCreated());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeCreate + 1);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getTituloTrabajo()).isEqualTo(DEFAULT_TITULO_TRABAJO);
        assertThat(testTrabajo.getSalarioMin()).isEqualTo(DEFAULT_SALARIO_MIN);
        assertThat(testTrabajo.getSalarioMax()).isEqualTo(DEFAULT_SALARIO_MAX);
    }

    @Test
    @Transactional
    void createTrabajoWithExistingId() throws Exception {
        // Create the Trabajo with an existing ID
        trabajo.setId(1L);

        int databaseSizeBeforeCreate = trabajoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrabajoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrabajos() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get all the trabajoList
        restTrabajoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajo.getId().intValue())))
            .andExpect(jsonPath("$.[*].tituloTrabajo").value(hasItem(DEFAULT_TITULO_TRABAJO)))
            .andExpect(jsonPath("$.[*].salarioMin").value(hasItem(DEFAULT_SALARIO_MIN.intValue())))
            .andExpect(jsonPath("$.[*].salarioMax").value(hasItem(DEFAULT_SALARIO_MAX.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrabajosWithEagerRelationshipsIsEnabled() throws Exception {
        when(trabajoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrabajoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trabajoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrabajosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trabajoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrabajoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trabajoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        // Get the trabajo
        restTrabajoMockMvc
            .perform(get(ENTITY_API_URL_ID, trabajo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trabajo.getId().intValue()))
            .andExpect(jsonPath("$.tituloTrabajo").value(DEFAULT_TITULO_TRABAJO))
            .andExpect(jsonPath("$.salarioMin").value(DEFAULT_SALARIO_MIN.intValue()))
            .andExpect(jsonPath("$.salarioMax").value(DEFAULT_SALARIO_MAX.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTrabajo() throws Exception {
        // Get the trabajo
        restTrabajoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo
        Trabajo updatedTrabajo = trabajoRepository.findById(trabajo.getId()).get();
        // Disconnect from session so that the updates on updatedTrabajo are not directly saved in db
        em.detach(updatedTrabajo);
        updatedTrabajo.tituloTrabajo(UPDATED_TITULO_TRABAJO).salarioMin(UPDATED_SALARIO_MIN).salarioMax(UPDATED_SALARIO_MAX);

        restTrabajoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrabajo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getTituloTrabajo()).isEqualTo(UPDATED_TITULO_TRABAJO);
        assertThat(testTrabajo.getSalarioMin()).isEqualTo(UPDATED_SALARIO_MIN);
        assertThat(testTrabajo.getSalarioMax()).isEqualTo(UPDATED_SALARIO_MAX);
    }

    @Test
    @Transactional
    void putNonExistingTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trabajo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrabajoWithPatch() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo using partial update
        Trabajo partialUpdatedTrabajo = new Trabajo();
        partialUpdatedTrabajo.setId(trabajo.getId());

        partialUpdatedTrabajo.tituloTrabajo(UPDATED_TITULO_TRABAJO).salarioMax(UPDATED_SALARIO_MAX);

        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getTituloTrabajo()).isEqualTo(UPDATED_TITULO_TRABAJO);
        assertThat(testTrabajo.getSalarioMin()).isEqualTo(DEFAULT_SALARIO_MIN);
        assertThat(testTrabajo.getSalarioMax()).isEqualTo(UPDATED_SALARIO_MAX);
    }

    @Test
    @Transactional
    void fullUpdateTrabajoWithPatch() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();

        // Update the trabajo using partial update
        Trabajo partialUpdatedTrabajo = new Trabajo();
        partialUpdatedTrabajo.setId(trabajo.getId());

        partialUpdatedTrabajo.tituloTrabajo(UPDATED_TITULO_TRABAJO).salarioMin(UPDATED_SALARIO_MIN).salarioMax(UPDATED_SALARIO_MAX);

        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrabajo))
            )
            .andExpect(status().isOk());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
        Trabajo testTrabajo = trabajoList.get(trabajoList.size() - 1);
        assertThat(testTrabajo.getTituloTrabajo()).isEqualTo(UPDATED_TITULO_TRABAJO);
        assertThat(testTrabajo.getSalarioMin()).isEqualTo(UPDATED_SALARIO_MIN);
        assertThat(testTrabajo.getSalarioMax()).isEqualTo(UPDATED_SALARIO_MAX);
    }

    @Test
    @Transactional
    void patchNonExistingTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trabajo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trabajo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrabajo() throws Exception {
        int databaseSizeBeforeUpdate = trabajoRepository.findAll().size();
        trabajo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trabajo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajo in the database
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrabajo() throws Exception {
        // Initialize the database
        trabajoRepository.saveAndFlush(trabajo);

        int databaseSizeBeforeDelete = trabajoRepository.findAll().size();

        // Delete the trabajo
        restTrabajoMockMvc
            .perform(delete(ENTITY_API_URL_ID, trabajo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trabajo> trabajoList = trabajoRepository.findAll();
        assertThat(trabajoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
