package com.revo.myboard.category;

import com.revo.myboard.category.dto.CategoryDTO;
import com.revo.myboard.category.dto.CreateDTO;
import com.revo.myboard.category.dto.NameDTO;
import com.revo.myboard.security.annotation.ForAdmin;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/categories")
@Validated
@AllArgsConstructor
class CategoryController {

    private static final String CATEGORY_LOCATION = "/categories";

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable long id, HttpServletRequest request) {
        var categoryDTO = categoryService.getCategoryDTOById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @PostMapping()
    @ForAdmin
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CreateDTO createDTO, HttpServletRequest request) {
        var categoryDTO = categoryService.createCategory(createDTO);
        return ResponseEntity.created(URI.create(CATEGORY_LOCATION)).body(categoryDTO);
    }

    @DeleteMapping("/{id}")
    @ForAdmin
    public ResponseEntity<Void> deleteCategoryById(@PathVariable long id, HttpServletRequest request) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @ForAdmin
    public ResponseEntity<CategoryDTO> renameCategoryById(@PathVariable long id, @RequestBody @Valid NameDTO renameDTO,
                                                            HttpServletRequest request) {
        var categoryDTO = categoryService.renameCategoryById(id, renameDTO.getNewName());
        return ResponseEntity.ok(categoryDTO);
    }
}
