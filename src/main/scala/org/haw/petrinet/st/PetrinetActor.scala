package org.haw.petrinet.st

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.haw.petrinet.actor.additions.Log
import com.typesafe.config.ConfigFactory

object TestMyNet extends App {
  val config = ConfigFactory.load() 
  val system = ActorSystem("petrinet",config.getConfig("petrinet").withFallback(config))
  val petrinetActor = system.actorOf(Props[PetrinetActor],"petrinetactor")

  import Petrinet._
  val p = P('p1, 'p2, 'p3, 'p4)
  val t = T('t1, 't2)
  val e1 = 'p1 --> 't1 := 1
  val e2 = 'p2 --> 't1 := 1
  val e3 = 'p2 --> 't2 := 1
  val e4 = 'p3 --> 't2 := 1
  val e5 = 't1 <-- 'p4 := 1
  val e6 = 't2 <-- 'p4 := 1
  val w = W(e1, e2,e3,e4)(e5,e6)
  val n = N(p, t, w)

  petrinetActor ! n
}

class PetrinetActor extends Actor with Log{

  import Petrinet._ 
  def receive = {
    /*
     * Init actor petri net 
     */
    case n: N => {
      for (place <- n.p.places)
        context.actorOf(Props[PlaceActor], place.label.name)
      for (place <- n.p.places)
        context.child(place.label.name).get ! Token // for test only
      for (transition <- n.t.transitions)
        context.actorOf(Props[TransitionActor], transition.label.name)
      for (--> <- n.w.-->>)
        context.child(-->.t.label.name).get ! -->.hanged  
      for (<-- <- n.w.<<--)
        context.child(<--.t.label.name).get ! <--.hanged
      context.become(waitOnThanks(n,n.w.size))
    }
  }
  
  import PetrinetActions._
  def waitOnThanks(n: N,i:Int):Receive = {
    case Thanks if i > 1 => {
      context.become(waitOnThanks(n,i-1))
    }
    case Thanks => {
      log.debug("ready to start petrinet ")
      for (transition <- n.t.transitions)
        context.child(transition.label.name).get ! Start 
      context.become(receive)
    }
  }

}