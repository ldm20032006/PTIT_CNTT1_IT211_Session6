package org.example.miniprojectss05.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDTO {

    @NotBlank(message = "name khong duoc rong")
    private String name;

    private String description;

    @NotNull(message = "price khong duoc null")
    @Positive(message = "price phai > 0")
    private Double price;
}
