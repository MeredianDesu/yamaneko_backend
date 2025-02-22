package org.yamaneko.yamaneko_back_end.api.controllers.private_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.team.TeamDTO
import org.yamaneko.yamaneko_back_end.dto.team.TeamRequestDTO
import org.yamaneko.yamaneko_back_end.service.team.TeamService

@RestController
@RequestMapping( "/api/team/v1" )
class TeamController(
    @Autowired private val teamService: TeamService,
) {

    @Operation( summary = "Get all teams from DB." )
    @GetMapping("")
    fun getAllTeams(): List<TeamDTO>{

        return teamService.getTeamList()
    }

    @Operation( summary = "Get member by ID." )
    @GetMapping("{memberId}")
    fun getTeamMemberById( @Parameter( description = "Enter member ID." ) @PathVariable( required = true ) memberId: Long ): ResponseEntity<TeamDTO>{
        val member = teamService.getMemberById( memberId )

        return if( member != null ){
            ResponseEntity.status( HttpStatus.OK ).body( member )
        }
        else{
            ResponseEntity.notFound().build()
        }
    }

    @Operation( summary = "Remove team member." )
    @DeleteMapping("{memberId}")
    fun removeTeamMember( @Parameter( description = "Enter member ID." ) @PathVariable( required = true ) memberId: Long ): ResponseEntity<Any>{

        return if(  teamService.removeTeamMember( memberId ).statusCode == HttpStatus.OK ){
            ResponseEntity.ok().build()
        }
        else{
            ResponseEntity.notFound().build()
        }
    }

    @Operation( summary = "Add new team member." )
    @PostMapping("")
    fun addTeamMember( @RequestBody request: TeamRequestDTO ): ResponseEntity<TeamDTO>{
        val response = teamService.createTeamMember( request )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }
}