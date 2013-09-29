package org.haw.petrinet.st

object PetrinetActions {
  
  case class CanYouReserveTokens(i:Int)
  case object TokensReserved
  case object TokensNotReserved
  
  case object ITakeMyReservedTokens
  case object IDontTakeMyReservedTokens
  case object ItsYours
  
  case class YouGetNewTokens(i:Int)
  
  case object Thanks
  case object Trigger
  case object Start
}