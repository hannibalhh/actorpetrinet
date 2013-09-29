package org.haw.petrinet.st

import akka.actor.Actor
import Petrinet._
import PetrinetActions._
import org.haw.petrinet.actor.additions.Log
import org.haw.petrinet.actor.additions.Delay

class TransitionActor extends Actor with Log with Delay{
  
  /**
   * start state: needs pre and post condition
   */
  def receive = {
    case Start => {
      log.debug("start" )
      context.become(working(Set(),Set()))
    }
    case out: <-/ => {
      sender ! Thanks
      context.become(carrying(Set(),Set(out)))
    }
    case  in: /-> => {
      sender ! Thanks
      context.become(carrying(Set(in),Set()))
    }
  }
  
  def carrying(in:Set[/->],out:Set[<-/]):Receive = {
    case Start => {
      log.debug("start " + in + " " +  out)
      canYouReserveTokens(in)
      context.become(working(in,out))
    }
    case o: <-/ => {
      sender ! Thanks
      context.become(carrying(in,out + o))
    }
    case i: /-> => {
      sender ! Thanks      
      context.become(carrying(in + i,out))
    }
  }
  
  def working(in:Set[/->],out:Set[<-/]):Receive = {
    case Start => {
      log.debug("i'm working " + in + " " +  out)
    }
    case o: <-/ => {
      sender ! Thanks
      context.become(carrying(in,out + o))
    }
    case i: /-> => {
      sender ! Thanks      
      context.become(carrying(in + i,out))
    }
    
    case TokensReserved => {
      log.debug("check whether all okay")
    }
    case TokensNotReserved => {
      log.debug("check whether all okay by not reserved")
    }
  }
  
  def canYouReserveTokens(in:Set[/->]){
    for (/-> <- in)
      context.actorFor(context.parent.path.child(/->.p.label.name)) ! CanYouReserveTokens(/->.weight)
  }
  
  override val logname = "transitionactor"
  override val delayname = "transitionactor"
  log.debug("started")
}