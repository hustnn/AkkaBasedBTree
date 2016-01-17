/**
 * Created by nn on 1/17/2016.
 */

import akka.actor._

object BinaryTreeSet {

  trait Operation {
    def requester: ActorRef
    def id: Int
    def elem: Int
  }

  trait OperationReply {
    def id: Int
  }

  case class Insert(requester: ActorRef, id: Int, elem: Int) extends Operation
  case class Contains(requester: ActorRef, id: Int, elem: Int) extends Operation
  case class Remove(requester: ActorRef, id: Int, elem: Int) extends Operation
  case class OperationFinished(id: Int) extends OperationReply
}

class BinaryTreeSet extends Actor {
  import BinaryTreeSet.Operation

  def createRoot: ActorRef = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = true))

  def root = createRoot

  def receive = normal

  def normal: Receive = {
    case operation: Operation => root ! operation
  }
}