package org.yamaneko.yamaneko_back_end.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.Data

@Data
@Schema(description = "User registration request")
data class UserRegistrationRequest(
  
  @Schema(description = "User Name", example = "Jon") @Size(
    min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов"
  ) @NotBlank(message = "Имя пользователя не может быть пустыми") val username: String,
  
  @Schema(description = "Email", example = "jondoe@gmail.com") @Size(
    min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов"
  ) @NotBlank(message = "Адрес электронной почты не может быть пустыми") @Email(message = "Email адрес должен быть в формате user@example.com") val email: String,
  
  @Schema(description = "Password", example = "my_1secret1_password") @Size(
    max = 255, message = "Длина пароля должна быть не более 255 символов"
  ) val password: String,
  
  @Schema(description = "Repeated Password") @Size(
    min = 6,
    message = "Minimum password length is 6"
  ) val repeatedPassword: String
)
