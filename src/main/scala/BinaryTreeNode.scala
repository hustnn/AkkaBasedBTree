/**
 * Created by nn on 1/17/2016.
 */

import BinaryTreeSet._
import akka.actor.{Actor, ActorRef, Props}

object BinaryTreeNode {
  trait Position

  case object Left extends Position
  case object Right extends Position

  def props(elem: Int, initiallyRemoved: Boolean) = Props(classOf[BinaryTreeNode], elem, initiallyRemoved)
}
