package com.revo.myboard.section;

import com.revo.myboard.section.dto.NameDTO;
import com.revo.myboard.section.dto.SectionDTO;
import com.revo.myboard.security.annotation.ForAdmin;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sections")
@Validated
@AllArgsConstructor
class SectionController {

    private static final String SECTION_LOCATION = "/sections";

    private final SectionService serverService;

    @GetMapping("/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable long id, HttpServletRequest request) {
        var sectionDTO = serverService.getSectionDTOById(id);
        return ResponseEntity.ok(sectionDTO);
    }

    @GetMapping()
    public ResponseEntity<List<SectionDTO>> getAllSections(HttpServletRequest request) {
        var sectionsDTO = serverService.getAllSections();
        return ResponseEntity.ok(sectionsDTO);
    }

    @PostMapping()
    @ForAdmin
    public ResponseEntity<SectionDTO> createSection(@RequestBody @Valid NameDTO createDTO, HttpServletRequest request) {
        var sectionDTO = serverService.createSection(createDTO.getName());
        return ResponseEntity.created(URI.create(SECTION_LOCATION)).body(sectionDTO);
    }

    @DeleteMapping("/{id}")
    @ForAdmin
    public ResponseEntity<Void> deleteSectionById(@PathVariable long id, HttpServletRequest request) {
        serverService.deleteSectionById(id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}")
    @ForAdmin
    public ResponseEntity<SectionDTO> renameSectionById(@PathVariable long id, @RequestBody @Valid NameDTO nameDTO, HttpServletRequest request) {
        var sectionDTO = serverService.renameSectionById(id, nameDTO.getName());
        return ResponseEntity.ok(sectionDTO);
    }
}
