import com.softwaremill.macwire.wire
import components.{ManagerComponents, RepositoryComponents}
import controllers.HomeController
import org.apache.pekko.actor.ActorSystem
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.filters.HttpFiltersComponents
import repositories.DatabaseExecutionContext
import router.Routes


class AppApplicationLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {
    new RestApiComponents(context).application
  }
}

class RestApiComponents(context: ApplicationLoader.Context)
  extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with RepositoryComponents
    with ManagerComponents  {

  implicit lazy val as: ActorSystem = actorSystem
  implicit val ec: DatabaseExecutionContext = new DatabaseExecutionContext(as)

  lazy val homeController: HomeController = new HomeController(carManager, controllerComponents, computerManager)
  lazy val router: Router = new Routes(httpErrorHandler, homeController, "/")

  override def httpFilters: Seq[EssentialFilter] = super.httpFilters
}

