package repositories

import org.apache.pekko.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext

class DatabaseExecutionContext(system: ActorSystem) extends CustomExecutionContext(system, "database.dispatcher")
