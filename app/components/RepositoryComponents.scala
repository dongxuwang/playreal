package components

import com.softwaremill.macwire.wire
import persistence.entities.Car
import persistence.{InMemoryPersistence, Persistence}
import play.api.db.{DBComponents, Database, HikariCPComponents}
import repositories.{CarRepository, CompanyRepository, ComputerRepository, DatabaseExecutionContext}

import scala.collection.mutable.ListBuffer

trait RepositoryComponents extends
DBComponents
with HikariCPComponents {

  implicit def ec: DatabaseExecutionContext
  lazy val persistence: Persistence[Car, Long, ListBuffer] = InMemoryPersistence
  lazy val db: Database = dbApi.database("default")
  lazy val carRepository: CarRepository = wire[CarRepository]
  lazy val companyRepository: CompanyRepository = wire[CompanyRepository]
  lazy val computerRepository: ComputerRepository = wire[ComputerRepository]
}
