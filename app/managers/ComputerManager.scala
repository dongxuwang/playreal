package managers

import managers.errors.ManagerError
import persistence.entities.ComputerDTO
import repositories.ComputerRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

trait ComputerManager {

  def save(computer: ComputerDTO): Unit
}

class ComputerManagerLive(repository: ComputerRepository) extends ComputerManager {

  override def save(computer: ComputerDTO): Unit =
    repository.insert(computer.toComputer).onComplete {
      case Success(value) => Right(value)
      case Failure(exception) => Left(exception)
    }
}
