package org.yamaneko.yamaneko_back_end.api.controllers.public_api

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class CustomErrorController: ErrorController {
  
  @RequestMapping("/error")
  @ResponseBody
  fun handleError(): String {
    return "Error"
  }
}