package com.revo.myboard.report;

import com.revo.myboard.comment.Comment;
import com.revo.myboard.comment.CommentServiceApi;
import com.revo.myboard.exception.ReportNotExistsException;
import com.revo.myboard.exception.SameAccessStatusException;
import com.revo.myboard.exception.UserIsOwnerException;
import com.revo.myboard.group.Authority;
import com.revo.myboard.post.Post;
import com.revo.myboard.post.PostServiceApi;
import com.revo.myboard.report.dto.ReportDTO;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserServiceApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
class ReportService {

    private static final String WHO = "Admin";
    private final ReportRepository repository;
    private final UserServiceApi userService;
    private final PostServiceApi postService;
    private final CommentServiceApi commentService;

    Report getReportById(String token, long id) {
        var report = getReport(id);
        var user = getUser(token);
        if (hasUserRole(user) && !isOwnerOfReport(user, report)) {
            throw new ReportNotExistsException(id);
        }
        return report;
    }

    private boolean isOwnerOfReport(User user, Report report) {
        var reports = user.getReports();
        return reports.contains(report);
    }

    private boolean hasUserRole(User user) {
        var group = user.getGroup();
        return Objects.equals(group.getAuthority(), Authority.USER);
    }

    private Report getReport(long id){
        return repository.findById(id)
                .orElseThrow(() -> new ReportNotExistsException(id));
    }

    private User getUser(String token){
        return userService.currentLoggedUser(token);
    }

    ReportDTO getReportDTOById(String token, long id) {
        return mapFromReport(getReportById(token, id));
    }

    private ReportDTO mapFromReport(Report report){
        return ReportMapper.mapFromReport(report);
    }

    ReportDTO createReportForPost(String token, long post_id, String content) {
        var user = getUser(token);
        var post = getPost(post_id);
        if (isOwnerOfPost(user, post)) {
            throw new UserIsOwnerException(user, post_id);
        }
        var report = repository.save(buildReport(user, post, content));
        return mapFromReport(report);
    }

    private boolean isOwnerOfPost(User user, Post post) {
        return Objects.equals(post.getAuthor(), user);
    }

    private Report buildReport(User user, Post post, String content){
        return Report.builder()
                .reporter(user)
                .post(post)
                .checked(false)
                .date(LocalDateTime.now())
                .content(content)
                .build();
    }

    private Post getPost(long id){
        return postService.getPostById(id);
    }

    ReportDTO createReportForComment(String token, long comment_id, String content) {
        var user = getUser(token);
        var comment = getComment(comment_id);
        if (isOwnerOfComment(user, comment)) {
            throw new UserIsOwnerException(user, comment_id);
        }
        var report = repository.save(buildReport(user,comment,content));
        return mapFromReport(report);
    }

    private boolean isOwnerOfComment(User user, Comment comment) {
        return Objects.equals(comment.getAuthor(), user);
    }

    private Report buildReport(User user, Comment comment, String content){
        return Report.builder()
                .reporter(user)
                .comment(comment)
                .checked(false)
                .date(LocalDateTime.now())
                .content(content)
                .build();
    }

    private Comment getComment(long id){
        return commentService.getCommentById(id);
    }

    List<ReportDTO> getAllNotCheckedReports() {
        var result = findByCheckedFalse();
        return mapFromList(result);
    }

    private List<ReportDTO> mapFromList(List<Report> reports){
        return reports.stream().map(this::mapFromReport).toList();
    }

    private List<Report> findByCheckedFalse(){
        return repository.findByCheckedFalse();
    }

    ReportDTO setReportAsChecked(long id) {
        var report = getReport(id);
        if (report.isChecked()) {
            throw new SameAccessStatusException(WHO, true);
        }
        report.setChecked(true);
        return mapFromReport(report);
    }

    void deleteReportById(long id) {
        repository.delete(repository.getById(id));
    }
}
