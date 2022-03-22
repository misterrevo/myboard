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

@RestController
@RequestMapping("/reports")
@Validated
@AllArgsConstructor
class ReportController {

    private static final String REPORT_LOCATION = "/reports";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final ReportService reportService;

    @GetMapping("/{id}")
    @ForUser
    public ResponseEntity<ReportDTO> getReportById(@PathVariable long id, @RequestHeader(AUTHORIZATION_HEADER) String token, HttpServletRequest request) {
        var reportDTO = reportService.getReportDTOById(token, id);
        return ResponseEntity.ok(reportDTO);
    }

    @PostMapping("/posts/{post_id}")
    @ForUser
    public ResponseEntity<ReportDTO> postReportById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long post_id, @RequestBody @Valid ContentDTO contentDTO, HttpServletRequest request) {
        var reportDTO = reportService.createReportForPost(token, post_id, contentDTO.getContent());
        return ResponseEntity.created(URI.create(REPORT_LOCATION)).body(reportDTO);
    }

    @PostMapping("/comments/{comment_id}")
    @ForUser
    public ResponseEntity<ReportDTO> commentReportById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long comment_id, @RequestBody @Valid ContentDTO contentDTO, HttpServletRequest request) {
        var reportDTO = reportService.createReportForComment(token, comment_id, contentDTO.getContent());
        return ResponseEntity.created(URI.create(REPORT_LOCATION)).body(reportDTO);
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
        var reportsDTO = reportService.getAllNotCheckedReports();
        return ResponseEntity.ok(reportsDTO);
    }

    @DeleteMapping("/{id}")
    @ForAdmin
    public ResponseEntity<Void> deleteReportById(@PathVariable long id, HttpServletRequest request) {
        reportService.deleteReportById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
