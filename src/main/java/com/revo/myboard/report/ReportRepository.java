package com.revo.myboard.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Created By Revo
 */

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("FROM Report r where r.checked = false")
    List<Report> findByCheckedFalse();

}
