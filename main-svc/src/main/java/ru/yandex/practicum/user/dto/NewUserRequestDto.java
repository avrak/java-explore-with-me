package ru.yandex.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequestDto {
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Length(min = 2, max = 250)
    private String name;

    @NotBlank(message = "Field: email. Error: must not be blank. Value: null")
    @Length(min = 6, max = 254)
    @Email(message = "Field: email. Error: incorrect value")
    private String email;
}