package com.monyets.inthemoment.repository;

import com.monyets.inthemoment.domain.Setting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Setting entity.
 */
@SuppressWarnings("unused")
public interface SettingRepository extends JpaRepository<Setting,Long> {

}
