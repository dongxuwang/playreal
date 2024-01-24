import components.{ManagerComponents, RepositoryComponents}
import controllers.HomeController
import managers.{CarManager, CarManagerLive}
import persistence.InMemoryPersistence
import play.api.mvc.EssentialFilter
import play.filters.HttpFiltersComponents
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.filters.csrf.CSRF
import repositories.CarRepository
import router.Routes


class AppApplicationLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {
    val components = new RestApiComponents(context)

    components.application
  }
}

class RestApiComponents(context: ApplicationLoader.Context)
  extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with ManagerComponents
    with RepositoryComponents {

  lazy val homeController: HomeController = new HomeController(carManager, controllerComponents)
  lazy val router: Router = new Routes(httpErrorHandler, homeController, "/")

  override def httpFilters: Seq[EssentialFilter] = super.httpFilters
}