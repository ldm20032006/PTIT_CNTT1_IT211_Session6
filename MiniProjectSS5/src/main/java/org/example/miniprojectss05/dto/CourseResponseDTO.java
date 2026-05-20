package org.example.miniprojectss05.dto;

import lombok.*;
import org.example.miniprojectss05.entity.Course;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;

    public static CourseResponseDTO from(Course c) {
        return CourseResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .price(c.getPrice())
                .imageUrl(c.getImageUrl())
                .build();
    }
}
