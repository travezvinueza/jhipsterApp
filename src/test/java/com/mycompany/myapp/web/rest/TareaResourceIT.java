package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Tarea;
import com.mycompany.myapp.repository.TareaRepository;
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
 * Integration tests for the {@link TareaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TareaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tareas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTareaMockMvc;

    private Tarea tarea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createEntity(EntityManager em) {
        Tarea tarea = new Tarea().titulo(DEFAULT_TITULO).descripcion(DEFAULT_DESCRIPCION);
        return tarea;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarea createUpdatedEntity(EntityManager em) {
        Tarea tarea = new Tarea().titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);
        return tarea;
    }

    @BeforeEach
    public void initTest() {
        tarea = createEntity(em);
    }

    @Test
    @Transactional
    void createTarea() throws Exception {
        int databaseSizeBeforeCreate = tareaRepository.findAll().size();
        // Create the Tarea
        restTareaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarea)))
            .andExpect(status().isCreated());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeCreate + 1);
        Tarea testTarea = tareaList.get(tareaList.size() - 1);
        assertThat(testTarea.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testTarea.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createTareaWithExistingId() throws Exception {
        // Create the Tarea with an existing ID
        tarea.setId(1L);

        int databaseSizeBeforeCreate = tareaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTareaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarea)))
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTareas() throws Exception {
        // Initialize the database
        tareaRepository.saveAndFlush(tarea);

        // Get all the tareaList
        restTareaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarea.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTarea() throws Exception {
        // Initialize the database
        tareaRepository.saveAndFlush(tarea);

        // Get the tarea
        restTareaMockMvc
            .perform(get(ENTITY_API_URL_ID, tarea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarea.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingTarea() throws Exception {
        // Get the tarea
        restTareaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTarea() throws Exception {
        // Initialize the database
        tareaRepository.saveAndFlush(tarea);

        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();

        // Update the tarea
        Tarea updatedTarea = tareaRepository.findById(tarea.getId()).get();
        // Disconnect from session so that the updates on updatedTarea are not directly saved in db
        em.detach(updatedTarea);
        updatedTarea.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);

        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTarea.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
        Tarea testTarea = tareaList.get(tareaList.size() - 1);
        assertThat(testTarea.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testTarea.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();
        tarea.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarea.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();
        tarea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();
        tarea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        tareaRepository.saveAndFlush(tarea);

        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
        Tarea testTarea = tareaList.get(tareaList.size() - 1);
        assertThat(testTarea.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testTarea.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateTareaWithPatch() throws Exception {
        // Initialize the database
        tareaRepository.saveAndFlush(tarea);

        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();

        // Update the tarea using partial update
        Tarea partialUpdatedTarea = new Tarea();
        partialUpdatedTarea.setId(tarea.getId());

        partialUpdatedTarea.titulo(UPDATED_TITULO).descripcion(UPDATED_DESCRIPCION);

        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarea))
            )
            .andExpect(status().isOk());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
        Tarea testTarea = tareaList.get(tareaList.size() - 1);
        assertThat(testTarea.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testTarea.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();
        tarea.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();
        tarea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarea))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarea() throws Exception {
        int databaseSizeBeforeUpdate = tareaRepository.findAll().size();
        tarea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTareaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tarea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarea in the database
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarea() throws Exception {
        // Initialize the database
        tareaRepository.saveAndFlush(tarea);

        int databaseSizeBeforeDelete = tareaRepository.findAll().size();

        // Delete the tarea
        restTareaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarea.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tarea> tareaList = tareaRepository.findAll();
        assertThat(tareaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
