package org.example.miniprojectss05.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.miniprojectss05.dto.CoursePatchDTO;
import org.example.miniprojectss05.dto.CourseRequestDTO;
import org.example.miniprojectss05.dto.CourseResponseDTO;
import org.example.miniprojectss05.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<Page<CourseResponseDTO>> getAll(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(courseService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@Valid @RequestBody CourseRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody CourseRequestDTO dto) {
        return ResponseEntity.ok(courseService.updateFull(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> patch(@PathVariable Long id,
            @Valid @RequestBody CoursePatchDTO dto) {
        return ResponseEntity.ok(courseService.updatePartial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<CourseResponseDTO> uploadImage(@PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(courseService.uploadImage(id, file));
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        courseService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
