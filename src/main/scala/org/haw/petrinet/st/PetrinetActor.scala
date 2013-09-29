package org.haw.petrinet.st

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.haw.petrinet.actor.additions.Log
import com.typesafe.config.ConfigFactory

class PetrinetActor extends Actor with Log {

  import Petrinet._
  /**
   * distribute actor petri net
   */
  def receive = {
    case n: N => {
      for (place <- n.p.places)
        context.actorOf(Props[PlaceActor], place.label.name)
      for (place <- n.p.places)
        context.child(place.label.name).get ! Token /* TODO marking */
      for (transition <- n.t.transitions)
        context.actorOf(Props[TransitionActor], transition.label.name)
      for (--> <- n.w.-->>)
        context.child(-->.t.label.name).get ! -->.hanged
      for (<-- <- n.w.<<--)
        context.child(<--.t.label.name).get ! <--.hanged
      context.become(waitOnStart(n, n.w.size))
    }
  }

  /**
   * wait on start actor petri net
   */
  import PetrinetActions._
  def waitOnStart(n: N, i: Int): Receive = {
    case Thanks if i > 1 => {
      context.become(waitOnStart(n, i - 1))
    }
    case Thanks => {
      log.debug("ready to start petrinet ")
      for (transition <- n.t.transitions)
        context.child(transition.label.name).get ! Turn
      context.become(receive)
    }
    case n: N => log.error("TODO stashing of new petrinets") /* TODO stashing */
  }

}