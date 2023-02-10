package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.repository.DireccionRepository;
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
 * Integration tests for the {@link DireccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DireccionResourceIT {

    private static final String DEFAULT_CALLE = "AAAAAAAAAA";
    private static final String UPDATED_CALLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCIA = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/direccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDireccionMockMvc;

    private Direccion direccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createEntity(EntityManager em) {
        Direccion direccion = new Direccion()
            .calle(DEFAULT_CALLE)
            .codigoPostal(DEFAULT_CODIGO_POSTAL)
            .ciudad(DEFAULT_CIUDAD)
            .provincia(DEFAULT_PROVINCIA);
        return direccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createUpdatedEntity(EntityManager em) {
        Direccion direccion = new Direccion()
            .calle(UPDATED_CALLE)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .ciudad(UPDATED_CIUDAD)
            .provincia(UPDATED_PROVINCIA);
        return direccion;
    }

    @BeforeEach
    public void initTest() {
        direccion = createEntity(em);
    }

    @Test
    @Transactional
    void createDireccion() throws Exception {
        int databaseSizeBeforeCreate = direccionRepository.findAll().size();
        // Create the Direccion
        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccion)))
            .andExpect(status().isCreated());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeCreate + 1);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getCalle()).isEqualTo(DEFAULT_CALLE);
        assertThat(testDireccion.getCodigoPostal()).isEqualTo(DEFAULT_CODIGO_POSTAL);
        assertThat(testDireccion.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testDireccion.getProvincia()).isEqualTo(DEFAULT_PROVINCIA);
    }

    @Test
    @Transactional
    void createDireccionWithExistingId() throws Exception {
        // Create the Direccion with an existing ID
        direccion.setId(1L);

        int databaseSizeBeforeCreate = direccionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccion)))
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDireccions() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)));
    }

    @Test
    @Transactional
    void getDireccion() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get the direccion
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL_ID, direccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direccion.getId().intValue()))
            .andExpect(jsonPath("$.calle").value(DEFAULT_CALLE))
            .andExpect(jsonPath("$.codigoPostal").value(DEFAULT_CODIGO_POSTAL))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.provincia").value(DEFAULT_PROVINCIA));
    }

    @Test
    @Transactional
    void getNonExistingDireccion() throws Exception {
        // Get the direccion
        restDireccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDireccion() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();

        // Update the direccion
        Direccion updatedDireccion = direccionRepository.findById(direccion.getId()).get();
        // Disconnect from session so that the updates on updatedDireccion are not directly saved in db
        em.detach(updatedDireccion);
        updatedDireccion.calle(UPDATED_CALLE).codigoPostal(UPDATED_CODIGO_POSTAL).ciudad(UPDATED_CIUDAD).provincia(UPDATED_PROVINCIA);

        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDireccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getCalle()).isEqualTo(UPDATED_CALLE);
        assertThat(testDireccion.getCodigoPostal()).isEqualTo(UPDATED_CODIGO_POSTAL);
        assertThat(testDireccion.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testDireccion.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void putNonExistingDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion
            .calle(UPDATED_CALLE)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .ciudad(UPDATED_CIUDAD)
            .provincia(UPDATED_PROVINCIA);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getCalle()).isEqualTo(UPDATED_CALLE);
        assertThat(testDireccion.getCodigoPostal()).isEqualTo(UPDATED_CODIGO_POSTAL);
        assertThat(testDireccion.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testDireccion.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void fullUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion
            .calle(UPDATED_CALLE)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .ciudad(UPDATED_CIUDAD)
            .provincia(UPDATED_PROVINCIA);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getCalle()).isEqualTo(UPDATED_CALLE);
        assertThat(testDireccion.getCodigoPostal()).isEqualTo(UPDATED_CODIGO_POSTAL);
        assertThat(testDireccion.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testDireccion.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void patchNonExistingDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, direccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(direccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(direccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(direccion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDireccion() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        int databaseSizeBeforeDelete = direccionRepository.findAll().size();

        // Delete the direccion
        restDireccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, direccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
