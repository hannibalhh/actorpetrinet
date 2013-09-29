package org.haw.petrinet.st

import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory

object Actorpetrinet extends App {
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