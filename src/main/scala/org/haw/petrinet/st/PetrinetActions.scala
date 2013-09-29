package org.haw.petrinet.st

object PetrinetActions {
  /**
   * Standard response message of init data 
   */
  case object Thanks
  
  /**
   * Start message of after init data, response after enaugh Thanks messages 
   */
  case object Start
  
  /**
   * start of first phase of two phase commit: CanYouReserveTokens
   */
  case class CanYouReserveTokens(i:Int)
  
  /**
   * first phase of two phase commit: answer of CanYouReserveTokens
   */
  case object TokensReserved
  
  /**
   * first phase of two phase commit: answer of CanYouReserveTokens
   */
  case object TokensNotReserved
  
  /**
   * second phase of two phase commit: answer of TokensReserved
   */
  case object ITakeMyReservedTokens
  
  /**
   * econd phase of two phase commit: answer of any TokensNotReserved
   */
  case object IDontTakeMyReservedTokens
  
  /**
   * end of second phase of two phase commit: answer of ITakeMyReservedTokens
   */
  case object ItsYours
  
  /**
   * end of second phase of two phase commit: post conditions places get new tokens
   */
  case class YouGetNewTokens(i:Int)
  
  /**
   * Trigger message for delay to itself or other actors
   */
  case object Trigger
  
}