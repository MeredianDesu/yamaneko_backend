package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.team.TeamDTO
import org.yamaneko.yamaneko_back_end.dto.team.TeamRequestDTO
import org.yamaneko.yamaneko_back_end.service.team.TeamService

@RestController
@RequestMapping( "/api/v1/team" )
class TeamController(
    @Autowired private val teamService: TeamService,
) {

    @Operation( summary = "Get all teams from DB." )
    @GetMapping("")
    fun getAllTeams(): List<TeamDTO>{

        return teamService.getTeamList()
    }

    @Operation( summary = "Add new team member." )
    @PostMapping("")
    fun addTeamMember( @RequestBody request: TeamRequestDTO ): ResponseEntity<TeamDTO>{
        val response = teamService.createTeamMember( request )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }
}