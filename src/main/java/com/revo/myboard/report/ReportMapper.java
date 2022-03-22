package com.revo.myboard.report;

import com.revo.myboard.report.dto.ReportDTO;

public final class ReportMapper {

    public static ReportDTO mapFromReport(Report report) {
        if (report.getComment() != null) {
            return buildForComment(report);
        } else {
            return buildForPost(report);
        }
    }

    private static ReportDTO buildForComment(Report report){
        var reporter = report.getReporter();
        var comment = report.getComment();
        return ReportDTO.builder()
                .id(report.getId())
                .reporter(reporter.getLogin())
                .comment(comment.getId())
                .date(report.getDate())
                .checked(report.isChecked())
                .content(report.getContent())
                .build();
    }

    private static ReportDTO buildForPost(Report report){
        var reporter = report.getReporter();
        var post = report.getPost();
        return ReportDTO.builder()
                .id(report.getId())
                .reporter(reporter.getLogin())
                .post(post.getId())
                .postTitle(post.getTitle())
                .date(report.getDate())
                .checked(report.isChecked())
                .content(report.getContent())
                .build();
    }
}
