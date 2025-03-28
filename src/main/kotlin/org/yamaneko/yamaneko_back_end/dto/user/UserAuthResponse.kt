package org.yamaneko.yamaneko_back_end.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data

@Data
@Schema(description = "Authorization Response")
data class UserAuthResponse(
  @Schema(
    description = "JWT Token",
    example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj..."
  ) val accessToken: String?,
)
