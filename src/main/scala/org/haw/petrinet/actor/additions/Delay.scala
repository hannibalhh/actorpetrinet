package org.haw.petrinet.actor.additions

import akka.actor.ActorContext
import akka.actor.Actor
import org.haw.petrinet.st.PetrinetActions.Turn
import scala.concurrent.duration.FiniteDuration

/**
 * Delay communication for actors by configuration
 */
trait Delay extends Actor{
  
  val delayname = "transitionactor"

  /**
   * Configured delays like
   * responseactor.delay = 50
   */
   import scala.concurrent.duration._
   val delay = 80 millis //context.system.settings.config.getInt(delayname + ".delay")

    /**
   * A delay is realized as a future with after pattern
   * Future which should send actor a Trigger message
   * will be start after duration by execution context
   */
  
  def delay(d: FiniteDuration = delay) = {
    import context.dispatcher
    import akka.pattern.after
    after(d, using = context.system.scheduler) {
      import scala.concurrent.Future
      Future {
        self ! Turn
      }
    }
  }
 
}