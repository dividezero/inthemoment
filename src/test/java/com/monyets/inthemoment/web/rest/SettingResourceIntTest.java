package com.monyets.inthemoment.web.rest;

import com.monyets.inthemoment.InthemomentbotApp;

import com.monyets.inthemoment.domain.Setting;
import com.monyets.inthemoment.repository.SettingRepository;
import com.monyets.inthemoment.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SettingResource REST controller.
 *
 * @see SettingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InthemomentbotApp.class)
public class SettingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSettingMockMvc;

    private Setting setting;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            SettingResource settingResource = new SettingResource(settingRepository);
        this.restSettingMockMvc = MockMvcBuilders.standaloneSetup(settingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Setting createEntity(EntityManager em) {
        Setting setting = new Setting()
                .name(DEFAULT_NAME)
                .value(DEFAULT_VALUE);
        return setting;
    }

    @Before
    public void initTest() {
        setting = createEntity(em);
    }

    @Test
    @Transactional
    public void createSetting() throws Exception {
        int databaseSizeBeforeCreate = settingRepository.findAll().size();

        // Create the Setting

        restSettingMockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(setting)))
            .andExpect(status().isCreated());

        // Validate the Setting in the database
        List<Setting> settingList = settingRepository.findAll();
        assertThat(settingList).hasSize(databaseSizeBeforeCreate + 1);
        Setting testSetting = settingList.get(settingList.size() - 1);
        assertThat(testSetting.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSetting.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createSettingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = settingRepository.findAll().size();

        // Create the Setting with an existing ID
        Setting existingSetting = new Setting();
        existingSetting.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettingMockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSetting)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Setting> settingList = settingRepository.findAll();
        assertThat(settingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = settingRepository.findAll().size();
        // set the field null
        setting.setName(null);

        // Create the Setting, which fails.

        restSettingMockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(setting)))
            .andExpect(status().isBadRequest());

        List<Setting> settingList = settingRepository.findAll();
        assertThat(settingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSettings() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);

        // Get all the settingList
        restSettingMockMvc.perform(get("/api/settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setting.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getSetting() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);

        // Get the setting
        restSettingMockMvc.perform(get("/api/settings/{id}", setting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(setting.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSetting() throws Exception {
        // Get the setting
        restSettingMockMvc.perform(get("/api/settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSetting() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);
        int databaseSizeBeforeUpdate = settingRepository.findAll().size();

        // Update the setting
        Setting updatedSetting = settingRepository.findOne(setting.getId());
        updatedSetting
                .name(UPDATED_NAME)
                .value(UPDATED_VALUE);

        restSettingMockMvc.perform(put("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSetting)))
            .andExpect(status().isOk());

        // Validate the Setting in the database
        List<Setting> settingList = settingRepository.findAll();
        assertThat(settingList).hasSize(databaseSizeBeforeUpdate);
        Setting testSetting = settingList.get(settingList.size() - 1);
        assertThat(testSetting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSetting.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSetting() throws Exception {
        int databaseSizeBeforeUpdate = settingRepository.findAll().size();

        // Create the Setting

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSettingMockMvc.perform(put("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(setting)))
            .andExpect(status().isCreated());

        // Validate the Setting in the database
        List<Setting> settingList = settingRepository.findAll();
        assertThat(settingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSetting() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);
        int databaseSizeBeforeDelete = settingRepository.findAll().size();

        // Get the setting
        restSettingMockMvc.perform(delete("/api/settings/{id}", setting.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Setting> settingList = settingRepository.findAll();
        assertThat(settingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Setting.class);
    }
}
