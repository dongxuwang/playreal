package managers

import managers.errors.ManagerError
import persistence.entities.CarDTO
import repositories.CarRepository
import repositories.errors.RepositoryError

case class Car(id: Long, brand: String, model: String, cc: Int, /* new field -> */sales: Long)

trait CarManager {
  def get(id: Long): Either[ManagerError, Option[CarDTO]]
  def save(car: CarDTO): Either[ManagerError, CarDTO]
  def update(car: CarDTO): Either[ManagerError, Unit]
  def delete(car: CarDTO): Either[ManagerError, Unit]
  def getAll(): Either[ManagerError, List[CarDTO]]
  def getCarsOrderedBySales(): Either[ManagerError, List[CarDTO]]
}

class CarManagerLive(repository: CarRepository) extends CarManager {
  override def get(id: Long): Either[ManagerError, Option[CarDTO]] =
    repository
      .findById(id)
      .fold(
        error => Left(toManagerError(error)),
        car => Right(car.map(_.toDTO()))
      )

  override def save(carDTO: CarDTO): Either[ManagerError, CarDTO] =
    repository
      .save(carDTO.toCar())
      .fold(error => Left(toManagerError(error)), car => Right(car.toDTO()))

  override def update(carDTO: CarDTO): Either[ManagerError, Unit] =
    repository
      .update(carDTO.toCar())
      .fold(error => Left(toManagerError(error)), _ => Right(()))

  override def delete(carDTO: CarDTO): Either[ManagerError, Unit] =
    repository
      .delete(carDTO.id.getOrElse(0L))
      .fold(error => Left(toManagerError(error)), _ => Right(()))

  override def getAll(): Either[ManagerError, List[CarDTO]] =
    repository
      .findAll()
      .fold(
        error => Left(toManagerError(error)),
        carList => Right(carList.map(_.toDTO()))
      )

  private def toManagerError(repositoryError: RepositoryError) =
    ManagerError(repositoryError.message)
  override def getCarsOrderedBySales(): Either[ManagerError, List[CarDTO]] =
    repository
      .findAll()
      .fold(error => Left(toManagerError(error)), carList => Right(carList.map(_.toDTO())))

}
