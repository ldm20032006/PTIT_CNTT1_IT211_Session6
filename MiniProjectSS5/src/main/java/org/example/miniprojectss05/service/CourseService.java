package org.example.miniprojectss05.service;

import lombok.RequiredArgsConstructor;
import org.example.miniprojectss05.dto.CoursePatchDTO;
import org.example.miniprojectss05.dto.CourseRequestDTO;
import org.example.miniprojectss05.dto.CourseResponseDTO;
import org.example.miniprojectss05.entity.Course;
import org.example.miniprojectss05.exception.BadRequestException;
import org.example.miniprojectss05.exception.ResourceNotFoundException;
import org.example.miniprojectss05.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private static final List<String> ALLOWED_SORT = List.of("id", "name", "price");

    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> findAll(Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            if (!ALLOWED_SORT.contains(order.getProperty())) {
                throw new BadRequestException("Khong the sort theo field: " + order.getProperty());
            }
        }
        return courseRepository.findAll(pageable).map(CourseResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public CourseResponseDTO findById(Long id) {
        return CourseResponseDTO.from(getCourseOrThrow(id));
    }

    @Transactional
    public CourseResponseDTO create(CourseRequestDTO dto) {
        Course c = Course.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
        return CourseResponseDTO.from(courseRepository.save(c));
    }

    @Transactional
    public CourseResponseDTO updateFull(Long id, CourseRequestDTO dto) {
        Course c = getCourseOrThrow(id);
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setPrice(dto.getPrice());
        return CourseResponseDTO.from(courseRepository.save(c));
    }

    @Transactional
    public CourseResponseDTO updatePartial(Long id, CoursePatchDTO dto) {
        Course c = getCourseOrThrow(id);
        if (dto.getName() != null) {
            if (dto.getName().isBlank()) {
                throw new BadRequestException("name khong duoc rong");
            }
            c.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            c.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            c.setPrice(dto.getPrice());
        }
        return CourseResponseDTO.from(courseRepository.save(c));
    }

    @Transactional
    public void delete(Long id) {
        Course c = getCourseOrThrow(id);
        if (c.getImageUrl() != null) {
            fileStorageService.deleteFile(c.getImageUrl());
        }
        courseRepository.delete(c);
    }

    @Transactional
    public CourseResponseDTO uploadImage(Long id, MultipartFile file) {
        Course c = getCourseOrThrow(id);
        if (c.getImageUrl() != null) {
            fileStorageService.deleteFile(c.getImageUrl());
        }
        String url = fileStorageService.storeFile(file);
        c.setImageUrl(url);
        return CourseResponseDTO.from(courseRepository.save(c));
    }

    @Transactional
    public void deleteImage(Long id) {
        Course c = getCourseOrThrow(id);
        if (c.getImageUrl() == null) {
            throw new ResourceNotFoundException("Khoa hoc id=" + id + " chua co anh");
        }
        fileStorageService.deleteFile(c.getImageUrl());
        c.setImageUrl(null);
        courseRepository.save(c);
    }

    private Course getCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc id=" + id));
    }
}
