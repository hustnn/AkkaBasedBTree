/**
 * Created by nn on 1/17/2016.
 */

import akka.actor.{ActorSystem,Props,Actor}
import BinaryTreeSet.{Contains,ContainsResult}

class DriverActor extends Actor {
  def receive = {
    case msg: String => println("hello " + msg)
    case ContainsResult(id, result) => println(result)
    case _ => println("unexpected message.!!")
  }
}

object Driver {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("niuniu-akka")
    val binTree = system.actorOf(Props(new BinaryTreeSet), "binaryTree")
    val driverRef = system.actorOf(Props(new DriverActor), "driver")
    driverRef ! "test"
    binTree ! Contains(driverRef, 1, 1)
    system.terminate()
  }
}
