package org.haw.petrinet.actor.additions

import akka.actor.Actor
import akka.event.Logging

/**
 * Easy Logging for Actor combined with configuration like
 * 	log {
	   naoactor{
		info = true
		error = true
		wrongMessage = true
	  }
	}
 */
trait Log extends Actor{
  val logname = self.path.name
  def trace(a: Any) = if (context.system.settings.config.getBoolean(logname+"log.info")) log.info(a.toString)
  def error(a: Any) = if (context.system.settings.config.getBoolean(logname+"log.error")) log.warning(a.toString)
  def wrongMessage(a: Any, state: String) = if (context.system.settings.config.getBoolean(logname+"log.wrongmessage")) log.warning("wrong message: " + a + " in " + state)
  import akka.event.Logging
  val log = Logging(context.system, this)
}