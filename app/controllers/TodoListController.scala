package controllers

import models.{Company, CompanyRepository, Computer, ComputerRepository, NewTodoListItem, Page, TodoListItem}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject._
import scala.collection.mutable
import scala.concurrent.ExecutionContext

@Singleton
class TodoListController @Inject()
(
  computerService: ComputerRepository,
  companyService: CompanyRepository,
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {
  private val todoList = new mutable.ListBuffer[TodoListItem]()
  todoList += TodoListItem(1, "test", isItDone = true)
  todoList += TodoListItem(2, "some other value", false)

  implicit val todoListJson: OFormat[TodoListItem] = Json.format[TodoListItem]
  implicit val newTodoListJson: OFormat[NewTodoListItem] = Json.format[NewTodoListItem]
  implicit val computerJson: OFormat[Computer] = Json.format[Computer]
  implicit val companyJson: OFormat[Company] = Json.format[Company]
  implicit val pageJson: OFormat[Page[(Computer, Option[Company])]] = Json.format[Page[(Computer, Option[Company])]]

  /**
   * Display the paginated list of computers.
   *
   * @param page    Current page number (starts from 0)
   * @param orderBy Column to be sorted
   * @param filter  Filter applied on computer names
   */
  def list(page: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    computerService.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")).map { page =>
      Ok(Json.toJson(page.items))
    }
  }
  // curl localhost:9000/todo
  def getAll: Action[AnyContent] = Action {
    if (todoList.isEmpty) NoContent else Ok(Json.toJson(todoList))
  }

  // curl localhost:9000/todo/1
  def getById(itemId: Long): Action[AnyContent] = Action {
    val foundItem = todoList.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

  // curl -X PUT localhost:9000/todo/done/1
  def markAsDone(itemId: Long): Action[AnyContent] = Action {
    val foundItem = todoList.find(_.id == itemId)
    foundItem match {
      case Some(item) =>
        val newItem = item.copy(isItDone = true)
        todoList.dropWhileInPlace(_.id == itemId)
        todoList += newItem
        Accepted(Json.toJson(newItem))
      case None => NotFound
    }
  }

  // curl -X DELETE localhost:9000/todo/done
  def deleteAllDone(): Action[AnyContent] = Action {
    todoList.filterInPlace(_.isItDone == false)
    Accepted
  }

  // curl -v -d '{"description": "some new item"}' -H 'Content-Type: application/json' -X POST localhost:9000/todo
  def addNewItem(): Action[AnyContent] = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val todoListItem: Option[NewTodoListItem] =
      jsonObject.flatMap(Json.fromJson[NewTodoListItem](_).asOpt)

    todoListItem match {
      case Some(newItem) =>
        val nextId = todoList.map(_.id).max + 1
        val toBeAdded = TodoListItem(nextId, newItem.description, false)
        todoList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }
}
