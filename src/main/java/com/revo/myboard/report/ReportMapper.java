package com.revo.myboard.report;

/*
 * Created By Revo
 */

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
        return ReportDTO.builder()
                .id(report.getId())
                .reporter(report.getReporter().getLogin())
                .comment(report.getComment().getId())
                .date(report.getDate())
                .checked(report.isChecked())
                .content(report.getContent())
                .build();
    }

    private static ReportDTO buildForPost(Report report){
        return ReportDTO.builder()
                .id(report.getId())
                .reporter(report.getReporter().getLogin())
                .postTitle(report.getPost().getTitle())
                .date(report.getDate())
                .checked(report.isChecked())
                .content(report.getContent())
                .build();
    }

}
