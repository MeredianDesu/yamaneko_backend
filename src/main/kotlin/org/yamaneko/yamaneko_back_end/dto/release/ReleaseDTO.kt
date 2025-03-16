package org.yamaneko.yamaneko_back_end.dto.release

import org.yamaneko.yamaneko_back_end.dto.RolesDTO
import org.yamaneko.yamaneko_back_end.dto.episode.EpisodeDTO
import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO

data class ReleaseDTO(
  val id: Long,
  val originalName: String,
  val translatedName: String,
  val ageRestriction: String?,
  val maxEpisodes: Int?,
  val status: String?,
  val posterImageUrl: String?,
  val previewVideoUrl: String?,
  val episodes: MutableList<EpisodeDTO>?,
  val synopsis: String,
  val info: String,
  val dubbers: List<RolesDTO>,
  val genres: List<GenreDTO>,
  val updatedAt: String,
  val uploadedAt: String)
