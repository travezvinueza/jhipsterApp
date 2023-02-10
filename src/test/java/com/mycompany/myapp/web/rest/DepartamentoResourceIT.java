package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Departamento;
import com.mycompany.myapp.repository.DepartamentoRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DepartamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartamentoResourceIT {

    private static final String DEFAULT_NOMBRE_DEPARTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_DEPARTAMENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartamentoMockMvc;

    private Departamento departamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departamento createEntity(EntityManager em) {
        Departamento departamento = new Departamento().nombreDepartamento(DEFAULT_NOMBRE_DEPARTAMENTO);
        return departamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departamento createUpdatedEntity(EntityManager em) {
        Departamento departamento = new Departamento().nombreDepartamento(UPDATED_NOMBRE_DEPARTAMENTO);
        return departamento;
    }

    @BeforeEach
    public void initTest() {
        departamento = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartamento() throws Exception {
        int databaseSizeBeforeCreate = departamentoRepository.findAll().size();
        // Create the Departamento
        restDepartamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departamento)))
            .andExpect(status().isCreated());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNombreDepartamento()).isEqualTo(DEFAULT_NOMBRE_DEPARTAMENTO);
    }

    @Test
    @Transactional
    void createDepartamentoWithExistingId() throws Exception {
        // Create the Departamento with an existing ID
        departamento.setId(1L);

        int databaseSizeBeforeCreate = departamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departamento)))
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreDepartamentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = departamentoRepository.findAll().size();
        // set the field null
        departamento.setNombreDepartamento(null);

        // Create the Departamento, which fails.

        restDepartamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departamento)))
            .andExpect(status().isBadRequest());

        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartamentos() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get all the departamentoList
        restDepartamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreDepartamento").value(hasItem(DEFAULT_NOMBRE_DEPARTAMENTO)));
    }

    @Test
    @Transactional
    void getDepartamento() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        // Get the departamento
        restDepartamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, departamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departamento.getId().intValue()))
            .andExpect(jsonPath("$.nombreDepartamento").value(DEFAULT_NOMBRE_DEPARTAMENTO));
    }

    @Test
    @Transactional
    void getNonExistingDepartamento() throws Exception {
        // Get the departamento
        restDepartamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartamento() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();

        // Update the departamento
        Departamento updatedDepartamento = departamentoRepository.findById(departamento.getId()).get();
        // Disconnect from session so that the updates on updatedDepartamento are not directly saved in db
        em.detach(updatedDepartamento);
        updatedDepartamento.nombreDepartamento(UPDATED_NOMBRE_DEPARTAMENTO);

        restDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepartamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDepartamento))
            )
            .andExpect(status().isOk());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNombreDepartamento()).isEqualTo(UPDATED_NOMBRE_DEPARTAMENTO);
    }

    @Test
    @Transactional
    void putNonExistingDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departamento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartamentoWithPatch() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();

        // Update the departamento using partial update
        Departamento partialUpdatedDepartamento = new Departamento();
        partialUpdatedDepartamento.setId(departamento.getId());

        partialUpdatedDepartamento.nombreDepartamento(UPDATED_NOMBRE_DEPARTAMENTO);

        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartamento))
            )
            .andExpect(status().isOk());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNombreDepartamento()).isEqualTo(UPDATED_NOMBRE_DEPARTAMENTO);
    }

    @Test
    @Transactional
    void fullUpdateDepartamentoWithPatch() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();

        // Update the departamento using partial update
        Departamento partialUpdatedDepartamento = new Departamento();
        partialUpdatedDepartamento.setId(departamento.getId());

        partialUpdatedDepartamento.nombreDepartamento(UPDATED_NOMBRE_DEPARTAMENTO);

        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartamento))
            )
            .andExpect(status().isOk());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
        Departamento testDepartamento = departamentoList.get(departamentoList.size() - 1);
        assertThat(testDepartamento.getNombreDepartamento()).isEqualTo(UPDATED_NOMBRE_DEPARTAMENTO);
    }

    @Test
    @Transactional
    void patchNonExistingDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartamento() throws Exception {
        int databaseSizeBeforeUpdate = departamentoRepository.findAll().size();
        departamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartamentoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(departamento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departamento in the database
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartamento() throws Exception {
        // Initialize the database
        departamentoRepository.saveAndFlush(departamento);

        int databaseSizeBeforeDelete = departamentoRepository.findAll().size();

        // Delete the departamento
        restDepartamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, departamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departamento> departamentoList = departamentoRepository.findAll();
        assertThat(departamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
