package org.haw.petrinet.st

object Petrinet {

  case object Token

  case class Transition(label: Symbol) {
    def <--(p: Place) = UnweightedTPEdge(this, p)
    def <--(p: Some[Place]) = UnweightedTPEdge(this, p.get)
    override def toString = "("+label.name+")" 
  }
  implicit def symbol2Transition(label: Symbol) = Transition(label)

  case class Place(label: Symbol) {
    def -->(t: Transition) = UnweightedPTEdge(this, t)
    def -->(t: Some[Transition]) = UnweightedPTEdge(this, t.get)
    override def toString = "["+label.name+"]" 
  }
  implicit def symbol2Place(label: Symbol) = Place(label)
  
//  implicit def symbol2PlaceorTransition(label: Symbol) = label.name.toList match {
//    case head :: _ if head == 'p' =>  Place(label)
//    case head :: _ if head == 't' =>  Transition(label)
//    case _ => None
//  }

  trait Edge
  case class -->(p: Place, t: Transition, weight: Int) extends Edge{
    /**
     * hanged edge
     */
    def hanged = /->(p,weight)
    override def toString = p + "-"+ weight + "->" + t 
  }
  case class <--(t: Transition, p: Place, weight: Int) extends Edge{
    /**
     * hanged edge
     */
    def hanged = <-/(p,weight)
    override def toString = t + "<-"+ weight + "-" + p 
  }

   /**
   * Unweighted Edges (helps to sugar)
   */
  case class UnweightedPTEdge(p: Place, t: Transition) {
    def :=(weight: Int) = -->(p, t, weight)
  }
  case class UnweightedTPEdge(t: Transition, p: Place) {
    def :=(weight: Int) = <--(t, p, weight)
  }
  
  trait HangedEdge
  case class /->(p: Place, weight: Int) extends HangedEdge{
    override def toString = "-"+ weight + "/->" + p 
  }
  case class <-/(p: Place, weight: Int) extends HangedEdge{
    override def toString = p + "<-"+ weight + "-"
  }

  /**
   * places of a petri net
   */
  case object P {
    def apply(s: Symbol*) = new P(s.map(x => Place(x)).toSet)
  }
  case class P(places: Set[Place])

  /**
   * transitions of a petri net
   */
  case class T(transitions: Set[Transition])
  case object T {
    def apply(s: Symbol*) = new T(s.map(x => Transition(x)).toSet)
  }

  /**
   * pre and post condition of a petri net
   */
  case object W{
    def apply(pt: --> *)(tp: <-- *) = new W(pt.toSet,tp.toSet)
  }
  case class W(-->> : Set[-->], <<-- : Set[<--]){
    def size = -->>.size + <<--.size
    override def toString = {
      def set[X](s:Set[X]) = s.map(x => x.toString).fold("")((x,y) => x + "," + y).replaceFirst(",", "")
      "W({" + set(-->>) + "}, {"+ set(<<--) + "})"
    }
  }

  case class M(m: P => Int)

  case class N(p: P, t: T, w:W) //, m: M)
}

object Test extends App {
  import Petrinet._

  val p = P('p1, 'p2, 'p3)
  val t = T('t1, 't2, 't3)
  val e1 = 'p1 --> 't1 := 2
  val e3 = 'p3 --> 't1 := 4
  val e2 = 't1 <-- 'p1 := 2
  val w = W(e1,e3)(e2)

  val n = N(p, t,w) 
  println(n)
}