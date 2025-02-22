package org.yamaneko.yamaneko_back_end.service.team

import org.springframework.http.ResponseEntity
import org.yamaneko.yamaneko_back_end.dto.team.TeamDTO
import org.yamaneko.yamaneko_back_end.dto.team.TeamRequestDTO

interface TeamService {
    fun getTeamList(): List<TeamDTO>
    fun createTeamMember( request: TeamRequestDTO ): TeamDTO
    fun getMemberById( memberId: Long ): TeamDTO?
    fun removeTeamMember( memberId: Long ): ResponseEntity<Any>
}