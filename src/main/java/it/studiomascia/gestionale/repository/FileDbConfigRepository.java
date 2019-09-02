/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.repository;

import it.studiomascia.gestionale.models.FileDbConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author luigi
 */
@Repository
public interface FileDbConfigRepository extends JpaRepository<FileDbConfig, String> {

}
