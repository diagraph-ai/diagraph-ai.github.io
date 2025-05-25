package in.diagraph

import in.diagraph.models.Node
import in.diagraph.objects.Constants.*

package object objects {
  extension (e: Double) {
    def px: String =
      e.toString + "px"
  }

  def isBetween(value: Double, min: Double, max: Double): Boolean =
    value >= min && value <= max

  def isOverlap(node1: Node, node2: Node): Boolean =
    (node1.east() >= node2.west() && node1.west() <= node2.east()) &&
      (node1.south() >= node2.north() && node1.north() <= node2.south())

  def isEast(node1: Node, node2: Node): Boolean =
    node2.west() >= node1.east() && node2.south() >= node1.north()

  def isSouth(node1: Node, node2: Node): Boolean =
    node2.north() >= node1.south() && node2.east() >= node1.west()

  def getPosition(node1: Node, node2: Node): Int =
    if isEast(node1, node2) then EAST
    else if isSouth(node1, node2) then SOUTH
    else INVALID
}
