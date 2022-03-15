package com.revo.myboard.category;

import com.revo.myboard.category.dto.CategoryDTO;
import com.revo.myboard.category.dto.CreateDTO;
import com.revo.myboard.category.dto.NameDTO;
import com.revo.myboard.security.annotation.ForAdmin;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

/*
 *  Created By Revo
 */

@RestController
@RequestMapping("/categories")
@Validated
@AllArgsConstructor
public class CategoryController {

    private static final String LOCATION = "/categories";

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable long id, HttpServletRequest request) {
        return ResponseEntity.ok(categoryService.getCategoryDTOById(id));
    }

    @PostMapping()
    @ForAdmin
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CreateDTO createDTO, HttpServletRequest request) {
        return ResponseEntity.created(URI.create(LOCATION)).body(categoryService.createCategory(createDTO.getName(), createDTO.getSection()));
    }

    @DeleteMapping("/{id}")
    @ForAdmin
    public ResponseEntity<Void> deleteCategoryByName(@PathVariable long id, HttpServletRequest request) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @ForAdmin
    public ResponseEntity<CategoryDTO> renameCategoryByName(@PathVariable long id, @RequestBody @Valid NameDTO renameDTO,
                                     HttpServletRequest request) {
        var categoryDTO = categoryService.renameCategoryById(id, renameDTO.getNewName());
        return ResponseEntity.ok(categoryDTO);
    }

}
