package org.example.miniprojectss05.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoursePatchDTO {

    private String name;

    private String description;

    @Positive(message = "price phai > 0")
    private Double price;
}
