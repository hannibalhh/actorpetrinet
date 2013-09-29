package org.haw.petrinet.st

import akka.actor.Actor
import org.haw.petrinet.st.Petrinet._
import org.haw.petrinet.st.PetrinetActions._
import akka.actor.ActorRef
import scala.collection.immutable.HashMap
import org.haw.petrinet.actor.additions.Log

class PlaceActor extends Actor with Log{
  
  /**
   * start state: no token
   */
  def receive = {
    // data
    case Token => context.become(carrying(1,HashMap(),Set()))
    
    // actions
    case CanYouReserveTokens(i:Int) if i <= 0 =>  {
      log.debug("TokensReserved receive" + i)
      sender ! TokensReserved
    }
    case CanYouReserveTokens(i:Int) => {
      log.debug("TokensNotReserved receive+" + i)
      sender ! TokensNotReserved
    }
  }
  
  def carrying(tokens:Int,reservedTokens:Map[ActorRef,Int],neighbours:Set[/->]):Receive = {
    // data
    case Token => context.become(carrying(tokens+1,reservedTokens,neighbours))
    
    // actions
    case CanYouReserveTokens(i:Int) if i <= tokens => {
      log.debug("TokensReserved carrying" + i)
      sender ! TokensReserved
      context.become(carrying(tokens-i,reservedTokens+(sender->i),neighbours))
    }
    case CanYouReserveTokens(i:Int) => {
      log.debug("TokensNotReserved carrying+" + i)
      sender ! TokensNotReserved
    }
    
    case ITakeMyReservedTokens => {
      log.debug("ITakeMyReservedTokens")
      reservedTokens-(sender)
      sender ! ItsYours
    }
    case IDontTakeMyReservedTokens => {
      log.debug("IDontTakeMyReservedTokens")
      tokens + reservedTokens(sender)
      reservedTokens-(sender)
    }
    
  }
  
  override val logname = "placeactor"
  log.debug("started")
}