package com.revo.myboard.report;

import com.revo.myboard.report.dto.ContentDTO;
import com.revo.myboard.report.dto.ReportDTO;
import com.revo.myboard.security.annotation.ForAdmin;
import com.revo.myboard.security.annotation.ForModerator;
import com.revo.myboard.security.annotation.ForUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/*
 *  Created By Revo
 */

@RestController
@RequestMapping("/reports")
@Validated
@AllArgsConstructor
public class ReportController {

    private static final String LOCATION = "/reports";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final ReportService reportService;

    @GetMapping("/{id}")
    @ForUser
    public ResponseEntity<ReportDTO> getReportById(@PathVariable long id, @RequestHeader(AUTHORIZATION_HEADER) String token, HttpServletRequest request) {
        return ResponseEntity.ok(reportService.getReportDTOById(token, id));
    }

    @PostMapping("/post/{post_id}")
    @ForUser
    public ResponseEntity<ReportDTO> postReportById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long post_id, @RequestBody @Valid ContentDTO contentDTO, HttpServletRequest request) {
        return ResponseEntity.created(URI.create(LOCATION)).body(reportService.createReportForPost(token, post_id, contentDTO.getContent()));
    }

    @PostMapping("/comment/{comment_id}")
    @ForUser
    public ResponseEntity<ReportDTO> commentReportById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long comment_id, @RequestBody @Valid ContentDTO contentDTO, HttpServletRequest request) {
        return ResponseEntity.created(URI.create(LOCATION)).body(reportService.createReportForComment(token, comment_id, contentDTO.getContent()));
    }

    @PatchMapping("/{id}")
    @ForModerator
    public ResponseEntity<ReportDTO> setReportAsChecked(@PathVariable long id, HttpServletRequest request) {
        var reportDTO = reportService.setReportAsChecked(id);
        return ResponseEntity.ok(reportDTO);
    }

    @GetMapping("/not-checked")
    @ForModerator
    public ResponseEntity<List<ReportDTO>> getAllNotCheckedReports(HttpServletRequest request) {
        return ResponseEntity.ok(reportService.getAllNotCheckedReports());
    }

    @DeleteMapping("/{id}")
    @ForAdmin
    public ResponseEntity<Void> deleteReportById(@PathVariable long id, HttpServletRequest request) {
        reportService.deleteReportById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
