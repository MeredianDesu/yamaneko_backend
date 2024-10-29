package org.yamaneko.yamaneko_back_end.service.team

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.team.TeamDTO
import org.yamaneko.yamaneko_back_end.dto.team.TeamRequestDTO
import org.yamaneko.yamaneko_back_end.entity.Team
import org.yamaneko.yamaneko_back_end.mappers.TeamMapper
import org.yamaneko.yamaneko_back_end.repository.CharacterRepository
import org.yamaneko.yamaneko_back_end.repository.TeamRepository
import org.yamaneko.yamaneko_back_end.repository.UserRepository

@Service
class TeamServiceImpl: TeamService {

    private val teamMapper = TeamMapper()

    @Autowired
    private lateinit var teamRepository: TeamRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var characterRepository: CharacterRepository


    override fun getTeamList(): List<TeamDTO> {
        val teamList = teamRepository.findAll()

        return teamList.map{ teamMapper.toDTO( it ) }
    }

    override fun createTeamMember( request: TeamRequestDTO ): TeamDTO {
        val user = userRepository.findById( request.user ).orElse( null )

        val member = Team().apply {
            name = request.name
            this.user = user
        }

        val savedMember = teamRepository.save( member )

        return teamMapper.toDTO( savedMember )
    }
}