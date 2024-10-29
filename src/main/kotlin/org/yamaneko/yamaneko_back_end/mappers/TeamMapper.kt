package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.team.TeamDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserTeamDTO
import org.yamaneko.yamaneko_back_end.entity.Team

class TeamMapper {

    fun toDTO( team: Team ): TeamDTO {

        return TeamDTO(
            id = team.id,
            name = team.name,
            user = UserTeamDTO(
                id = team.user?.id,
                username = team.user?.username
            )
        )
    }
}