/**
 * Created by nn on 1/17/2016.
 */

import akka.actor._
import scala.collection.immutable.Queue

import BinaryTreeNode.{CopyTo,CopyFinished}

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
  case object GC
  case class OperationFinished(id: Int) extends OperationReply
  case class ContainsResult(id: Int, result: Boolean) extends OperationReply
}

class BinaryTreeSet extends Actor {
  import BinaryTreeSet.{Operation,GC}

  def createRoot: ActorRef = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = true))
  var root = createRoot
  var pendingQueue = Queue.empty[Operation]

  def receive = normal

  def normal: Receive = {
    case operation: Operation => root ! operation

    case GC => {
      val newRoot = createRoot
      root ! CopyTo(newRoot)
      context.become(garbageCollecting(newRoot))
    }
  }

  def garbageCollecting(newRoot: ActorRef): Receive = {
    case operation: Operation => pendingQueue.enqueue(operation)

    case CopyFinished => {
      root ! PoisonPill
      val newRoot = createRoot
      root = newRoot
      pendingQueue.map(root ! _)
      pendingQueue = Queue.empty
      context.become(normal)
    }
  }
}