package org.haw.petrinet.st

object PetrinetActions {
  /**
   * Standard response message of init data 
   */
  case object Thanks
  
  /**
   * Turn message of after init data: response after enaugh Thanks messages or delay  
   */
  case object Turn
  
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
  case object TokensAreYours
 
  
}