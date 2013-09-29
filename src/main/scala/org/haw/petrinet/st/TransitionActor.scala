package org.haw.petrinet.st

import akka.actor.Actor
import Petrinet._
import PetrinetActions._
import org.haw.petrinet.actor.additions.Log
import org.haw.petrinet.actor.additions.Delay
import akka.actor.ActorRef

class TransitionActor extends Actor with Log with Delay{
  
  /**
   * start state: needs pre and post condition
   */
  def receive = {
    // init
    case Turn => {
      log.debug("start" )
      context.become(reserving())
    }
    
    // data
    case out: <-/ => {
      sender ! Thanks
      context.become(carrying(out = Set(out)))
    }
    case  in: /-> => {
      sender ! Thanks
      context.become(carrying(in = Set(in)))
    }
  }
  
  /**
   * carrying state: needs pre and post condition
   */
  def carrying(in:Set[/->] = Set(),out:Set[<-/] = Set()):Receive = {
    // init
    case Turn => {
      log.debug("start " + in + " " +  out)
      for (/-> <- in)
    	  context.actorFor(context.parent.path.child(/->.p.label.name)) ! CanYouReserveTokens(/->.weight)
      context.become(reserving(in,out,Set()))
    }
    
    // data
    case o: <-/ => {
      sender ! Thanks
      context.become(carrying(in,out + o))
    }
    case i: /-> => {
      sender ! Thanks      
      context.become(carrying(in + i,out))
    }
  }
  
  /**
   * reserving state: needs pre and post condition
   */
  def reserving(in:Set[/->] = Set(),out:Set[<-/] = Set(),reserved:Set[ActorRef] = Set()):Receive = {  
    // init
  	case Turn => {
      log.debug("i'm working " + in + " " +  out)
    }
    
    // data
    case o: <-/ => {
      sender ! Thanks
      context.become(reserving(in,out + o,reserved))
    }
    case i: /-> => {
      sender ! Thanks      
      context.become(reserving(in + i,out,reserved))
    }
    
    // actions
    case TokensReserved if in.size == reserved.size+1 => {
      log.debug("yehaaa")
      for (/-> <- in)
    	  context.actorFor(context.parent.path.child(/->.p.label.name)) ! ITakeMyReservedTokens
      context.become(taking(in,out))
    }
    case TokensReserved => {
      log.debug("check whether all okay: " + in.size +"=="+ (reserved.size+1))
      context.become(reserving(in,out,reserved + sender))
    }
    case TokensNotReserved => {
      log.debug("all -> IDontTakeMyReservedTokens")
      for (/-> <- in)
    	  context.actorFor(context.parent.path.child(/->.p.label.name)) ! IDontTakeMyReservedTokens
      context.become(reserving(in,out))
    }
    
    case TokensAreYours => {
      log.debug("i'm reserving")
    }
  }
  
  /**
   * taking state: needs pre and post condition
   */
  def taking(in:Set[/->] = Set(),out:Set[<-/] = Set(),taken:Set[ActorRef] = Set()):Receive = {  
    // init
  	case Turn => {
      log.debug("i'm working " + in + " " +  out)
    }
    
    // data
    case o: <-/ => {
      sender ! Thanks
      context.become(taking(in,out + o,taken))
    }
    case i: /-> => {
      sender ! Thanks      
      context.become(taking(in + i,out,taken))
    }
    
    // actions
    case TokensNotReserved => {
      log.debug("i'm taking")
    }
    case TokensAreYours if in.size == taken.size+1 => {
      log.debug("posts get new token")
      for (<-/ <- out)
        for (_ <- 1 to <-/.weight)
    	  context.actorFor(context.parent.path.child(<-/.p.label.name)) ! Token
      delay()
      context.become(carrying(in,out))
    }
    case TokensAreYours => {
      context.become(taking(in,out,taken + sender))
    }
  }
  
  override val logname = "transitionactor"
  override val delayname = "transitionactor"
  log.debug("started")
}