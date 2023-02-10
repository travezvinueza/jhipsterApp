package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Contrato;
import com.mycompany.myapp.domain.enumeration.Idioma;
import com.mycompany.myapp.repository.ContratoRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ContratoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContratoResourceIT {

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Idioma DEFAULT_LENGUAJE = Idioma.ESPANOL;
    private static final Idioma UPDATED_LENGUAJE = Idioma.INGLES;

    private static final String ENTITY_API_URL = "/api/contratoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContratoMockMvc;

    private Contrato contrato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrato createEntity(EntityManager em) {
        Contrato contrato = new Contrato().fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN).lenguaje(DEFAULT_LENGUAJE);
        return contrato;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrato createUpdatedEntity(EntityManager em) {
        Contrato contrato = new Contrato().fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).lenguaje(UPDATED_LENGUAJE);
        return contrato;
    }

    @BeforeEach
    public void initTest() {
        contrato = createEntity(em);
    }

    @Test
    @Transactional
    void createContrato() throws Exception {
        int databaseSizeBeforeCreate = contratoRepository.findAll().size();
        // Create the Contrato
        restContratoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrato)))
            .andExpect(status().isCreated());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeCreate + 1);
        Contrato testContrato = contratoList.get(contratoList.size() - 1);
        assertThat(testContrato.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testContrato.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testContrato.getLenguaje()).isEqualTo(DEFAULT_LENGUAJE);
    }

    @Test
    @Transactional
    void createContratoWithExistingId() throws Exception {
        // Create the Contrato with an existing ID
        contrato.setId(1L);

        int databaseSizeBeforeCreate = contratoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContratoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrato)))
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContratoes() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList
        restContratoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contrato.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].lenguaje").value(hasItem(DEFAULT_LENGUAJE.toString())));
    }

    @Test
    @Transactional
    void getContrato() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get the contrato
        restContratoMockMvc
            .perform(get(ENTITY_API_URL_ID, contrato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contrato.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.lenguaje").value(DEFAULT_LENGUAJE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContrato() throws Exception {
        // Get the contrato
        restContratoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContrato() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();

        // Update the contrato
        Contrato updatedContrato = contratoRepository.findById(contrato.getId()).get();
        // Disconnect from session so that the updates on updatedContrato are not directly saved in db
        em.detach(updatedContrato);
        updatedContrato.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).lenguaje(UPDATED_LENGUAJE);

        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContrato.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContrato))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
        Contrato testContrato = contratoList.get(contratoList.size() - 1);
        assertThat(testContrato.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testContrato.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testContrato.getLenguaje()).isEqualTo(UPDATED_LENGUAJE);
    }

    @Test
    @Transactional
    void putNonExistingContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contrato.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contrato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contrato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contrato)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContratoWithPatch() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();

        // Update the contrato using partial update
        Contrato partialUpdatedContrato = new Contrato();
        partialUpdatedContrato.setId(contrato.getId());

        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrato.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContrato))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
        Contrato testContrato = contratoList.get(contratoList.size() - 1);
        assertThat(testContrato.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testContrato.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testContrato.getLenguaje()).isEqualTo(DEFAULT_LENGUAJE);
    }

    @Test
    @Transactional
    void fullUpdateContratoWithPatch() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();

        // Update the contrato using partial update
        Contrato partialUpdatedContrato = new Contrato();
        partialUpdatedContrato.setId(contrato.getId());

        partialUpdatedContrato.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).lenguaje(UPDATED_LENGUAJE);

        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrato.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContrato))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
        Contrato testContrato = contratoList.get(contratoList.size() - 1);
        assertThat(testContrato.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testContrato.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testContrato.getLenguaje()).isEqualTo(UPDATED_LENGUAJE);
    }

    @Test
    @Transactional
    void patchNonExistingContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contrato.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contrato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contrato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contrato)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContrato() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        int databaseSizeBeforeDelete = contratoRepository.findAll().size();

        // Delete the contrato
        restContratoMockMvc
            .perform(delete(ENTITY_API_URL_ID, contrato.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
