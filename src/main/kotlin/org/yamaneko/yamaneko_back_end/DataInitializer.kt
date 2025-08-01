package org.yamaneko.yamaneko_back_end

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.yamaneko.yamaneko_back_end.entity.Achievement
import org.yamaneko.yamaneko_back_end.entity.ReleaseState
import org.yamaneko.yamaneko_back_end.repository.AchievementRepository
import org.yamaneko.yamaneko_back_end.repository.ReleaseStateRepository

@Component
class DataInitializer(
  private val achievementRepository: AchievementRepository,
  private val releaseStateRepository: ReleaseStateRepository,
): CommandLineRunner {
  
  override fun run(vararg args: String?) {
    if(achievementRepository.count() == 0L) {
      var achievement = Achievement(
        name = "First Step into the World",
        image = "https://yamanekospace.fra1.cdn.digitaloceanspaces.com/achievements/First%20Step%20into%20the%20World",
        condition = "Awarded when a user completes their first registration."
      )
      achievementRepository.save(achievement)
      
      achievement = Achievement(
        name = "Beta Tester Extraordinaire",
        image = "https://yamanekospace.fra1.cdn.digitaloceanspaces.com/achievements/Beta%20Tester%20Extraordinaire",
        condition = "Awarded for participating in beta testing."
      )
      achievementRepository.save(achievement)
      
      achievement = Achievement(
        name = "Code Sorcerer",
        image = "https://yamanekospace.fra1.cdn.digitaloceanspaces.com/achievements/Code%20Sorcerer",
        condition = "Awarded for completing the first milestone in development."
      )
      achievementRepository.save(achievement)
      
      achievement = Achievement(
        name = "Legendary Developer",
        image = "https://yamanekospace.fra1.cdn.digitaloceanspaces.com/achievements/Legendary%20Developer",
        condition = "Awarded when a developer completes a major feature or release."
      )
      achievementRepository.save(achievement)
      
      achievement = Achievement(
        name = "Nani?! Five Picks",
        image = "https://yamanekospace.fra1.cdn.digitaloceanspaces.com/achievements/Nani?%20Five%20Picks!",
        condition = "Awarded when 5 releases are added to favorites."
      )
      achievementRepository.save(achievement)
      
      achievement = Achievement(
        name = "Senpai's First Spark",
        image = "https://yamanekospace.fra1.cdn.digitaloceanspaces.com/achievements/Senpai's%20First%20Spark",
        condition = "Awarded when a user creates their first post, igniting their journey in the community."
      )
      achievementRepository.save(achievement)
    }
    
    if(releaseStateRepository.count() == 0L) {
      var releaseState = ReleaseState(
        state = "Planned"
      )
      releaseStateRepository.save(releaseState)
      
      releaseState = ReleaseState(
        state = "Ongoing"
      )
      releaseStateRepository.save(releaseState)
      
      releaseState = ReleaseState(
        state = "Finished"
      )
      releaseStateRepository.save(releaseState)
      
      releaseState = ReleaseState(
        state = "Referred"
      )
      releaseStateRepository.save(releaseState)
    }
  }
}