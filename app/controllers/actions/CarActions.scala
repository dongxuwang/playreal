package controllers.actions

import managers.CarManager
import persistence.entities.CarDTO
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

trait CarActions {
  def playBodyParsers: PlayBodyParsers

  def carManager: CarManager

  implicit val ec: ExecutionContext

  class CarRequest[A](val carDTO: Option[CarDTO], request: Request[A]) extends WrappedRequest[A](request)

  def carAction(id: Long) = new ActionBuilder[CarRequest, AnyContent]
    with ActionTransformer[Request, CarRequest] {
    def transform[A](request: Request[A]): Future[CarRequest[A]] = {
      Future {
        carManager.get(id).fold(_ => new CarRequest(None, request), car => new CarRequest(car, request))
      }(executionContext)
    }

    override def parser: BodyParser[AnyContent] = playBodyParsers.default

    override protected def executionContext: ExecutionContext = ec
  }
}
