package controllers

import actions.CarActions
import play.api.mvc.{BaseController, ControllerComponents, PlayBodyParsers}
import managers.{CarManager, ComputerManager}
import persistence.entities.{CarDTO, CompanyDTO, ComputerDTO}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

class HomeController(val carManager: CarManager,
                     val controllerComponents: ControllerComponents,
                     val computerManager: ComputerManager)(implicit val ec: ExecutionContext)
  extends BaseController {

  def get(id: Long) = Action {
    carManager
      .get(id)
      .fold(
        error => BadRequest(error.message),
        result => result.map(car => Ok(Json.toJson(car))).getOrElse(NoContent)
      )
  }

  def getAll() = Action {
    carManager
      .getAll()
      .fold(
        error => BadRequest(error.message),
        result => Ok(Json.toJson(result))
      )
  }

  def update() = Action(parse.json[CarDTO]) { implicit request =>
    carManager
      .update(request.body)
      .fold(
        error => BadRequest(error.message),
        _ => NoContent
      )
  }

  def save() = Action(parse.json[CarDTO]) { implicit request =>
    carManager
      .save(request.body)
      .fold(
        error => BadRequest(error.message),
        car => Ok(Json.toJson(car))
      )
  }

  def company() = Action(parse.json[ComputerDTO]) { implicit request =>
    computerManager
      .save(request.body)
    Ok(Json.toJson(request.body))
  }

  def index() = Action {
    Ok("This is a test")
  }

  def playBodyParsers: PlayBodyParsers = controllerComponents.parsers
}
