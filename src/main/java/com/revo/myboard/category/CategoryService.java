package com.revo.myboard.category;

import com.revo.myboard.category.dto.CategoryDTO;
import com.revo.myboard.category.dto.CreateDTO;
import com.revo.myboard.exception.CategoryNameInUseException;
import com.revo.myboard.exception.CategoryNotExistsException;
import com.revo.myboard.section.Section;
import com.revo.myboard.section.SectionServiceApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
class CategoryService implements CategoryServiceApi{

    private final CategoryRepository repository;
    private final SectionServiceApi sectionService;

    CategoryDTO createCategory(CreateDTO createDTO) {
        var name = createDTO.getName();
        var section_id = createDTO.getSection();
        if(existsByName(name)){
            throw new CategoryNameInUseException(name);
        }
        Section section = getSection(section_id);
        Category category = buildCategory(name, section);
        Category savedCategory = repository.save(category);
        return mapCategory(savedCategory);
    }

    private boolean existsByName(String name){
        return repository.existsByName(name);
    }

    private CategoryDTO mapCategory(Category category){
        return CategoryMapper.mapCategoryDTOFromCategory(category);
    }

    private Category buildCategory(String name, Section section){
        return Category.builder()
                .name(name)
                .section(section)
                .build();
    }

    private Section getSection(long id){
        return sectionService.getSectionById(id);
    }

    void deleteCategoryById(long id) {
        repository.delete(getCategoryById(id));
    }

    CategoryDTO getCategoryDTOById(long id) {
        var category = getCategoryById(id);
        return mapCategory(category);
    }

    @Override
    public Category getCategoryById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotExistsException(id));
    }

    CategoryDTO renameCategoryById(long id, String new_name) {
        if (existsByName(new_name)) {
            throw new CategoryNameInUseException(new_name);
        }
        var category = getCategoryById(id);
        category.setName(new_name);
        return mapCategory(category);
    }
}