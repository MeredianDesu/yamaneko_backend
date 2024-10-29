package org.yamaneko.yamaneko_back_end.service.team

import org.yamaneko.yamaneko_back_end.dto.team.TeamDTO
import org.yamaneko.yamaneko_back_end.dto.team.TeamRequestDTO
import org.yamaneko.yamaneko_back_end.entity.Team

interface TeamService {
    fun getTeamList(): List<TeamDTO>
    fun createTeamMember( request: TeamRequestDTO ): TeamDTO
}