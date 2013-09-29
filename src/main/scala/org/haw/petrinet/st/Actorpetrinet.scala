package org.haw.petrinet.st

import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory

object Actorpetrinet extends App {
  val config = ConfigFactory.load()
  val system = ActorSystem("petrinet", config.getConfig("petrinet").withFallback(config))
  val petrinetActor = system.actorOf(Props[PetrinetActor], "petrinetactor")

  val n1 = {
    import Petrinet._
    val p = P('p1, 'p2, 'p3)
    val t = T('t1, 't2)
    val e1 = 'p1 --> 't1 := 1
    val e2 = 'p2 --> 't1 := 1
    val e3 = 'p2 --> 't2 := 1
    val e4 = 'p3 --> 't2 := 1
    val e5 = 't1 <-- 'p2 := 1
    val e6 = 't2 <-- 'p2 := 1
    val e7 = 't2 <-- 'p3 := 1
    val e8 = 't1 <-- 'p1 := 0
    val w = W(e1, e2, e3, e4)(e5, e6, e7, e8)
    N(p, t, w)
  }
  
  val n2 = {
    import Petrinet._
    val p = P('p1, 'p2, 'p3, 'p4)
    val t = T('t1, 't2)
    val e1 = 'p1 --> 't1 := 1
    val e2 = 'p2 --> 't1 := 1
    val e3 = 'p2 --> 't2 := 1
    val e4 = 'p3 --> 't2 := 1
    val e5 = 't1 <-- 'p4 := 1
    val e6 = 't2 <-- 'p4 := 1
    val w = W(e1, e2, e3, e4)(e5, e6)
    N(p, t, w)
  }
  
  val n3 = {
    import Petrinet._
    val p = P('p1, 'p2, 'p3)
    val t = T('t1, 't2)
    val e1 = 'p1 --> 't1 := 1
    val e2 = 't1 <-- 'p2 := 1
    val e3 = 'p2 --> 't2 := 1
    val e4 = 't2 <-- 'p1 := 1 
    val w = W(e1, e3)(e2, e4)
    N(p, t, w)
  }

  petrinetActor ! n3

}


