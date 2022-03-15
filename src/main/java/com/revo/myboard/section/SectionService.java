package com.revo.myboard.section;

import com.revo.myboard.exception.SectionNameInUseException;
import com.revo.myboard.exception.SectionNotExistsException;
import com.revo.myboard.section.dto.SectionDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Created By Revo
 */

@Service
@AllArgsConstructor
public class SectionService {

    private final SectionRepository repository;

    SectionDTO createSection(String name) {
        if (existsByName(name)) {
            throw new SectionNameInUseException(name);
        }
        var section = repository.save(buildSection(name));
        return mapFromSection(section);
    }

    private Section buildSection(String name){
        return Section.builder().name(name).build();
    }

    private boolean existsByName(String name){
        return repository.existsByName(name);
    }

    private SectionDTO mapFromSection(Section section){
        return SectionMapper.mapFromSection(section);
    }

    void deleteSectionById(long id) {
        repository.delete(getSectionById(id));
    }

    public Section getSectionById(long id) {
        return repository.findById(id).orElseThrow(() -> new SectionNotExistsException(id));
    }

    SectionDTO getSectionDTOById(long id) {
        return mapFromSection(getSectionById(id));
    }

    @Transactional
    SectionDTO renameSectionById(long id, String new_name) {
        if (existsByName(new_name)) {
            throw new SectionNameInUseException(new_name);
        }
        var section = getSectionById(id);
        section.setName(new_name);
        return mapFromSection(section);
    }

    List<SectionDTO> getAllSections() {
        var result = getAll();
        return mapFromList(result);
    }

    private List<Section> getAll(){
        return repository.findAll();
    }

    private List<SectionDTO> mapFromList(List<Section> sections){
        return sections.stream().map(this::mapFromSection).collect(Collectors.toList());
    }

}
