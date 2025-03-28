package org.yamaneko.yamaneko_back_end.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.Data

@Data
@Schema(description = "Authorization Request")
data class UserAuthRequest(
  @Schema(
    description = "Email",
    example = "johndoe@example.com",
  ) @NotBlank(message = "Email can not be blank") @Email(message = "Email must be in format ") val email: String,
  
  @Schema(description = "Password", example = "my_secret_password1") @Size(
    min = 6, max = 100, message = "Password must contain at least 6 characters long"
  ) val password: String,
)
