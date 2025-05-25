package in.diagraph.models

import in.diagraph.objects.*
import in.diagraph.objects.Constants.*
import upickle.legacy.ReadWriter

import scala.collection.mutable.ListBuffer

case class Props(
  var id: String = "",
  var x: Double = 0,
  var y: Double = 0,
  var format: String = "",
  var title: String = "",
  var text: String = "",
  var imagePath: String = "",
  var showChildren: Boolean = false,
  var toolTipText: String = "",
  var biDirectional: Boolean = false,
  var width: Double = 0,
  var height: Double = 0,
  var toolTipWidth: Double = 0,
  var toolTipHeight: Double = 0,
  var x1: Double = 0,
  var y1: Double = 0,
  var x2: Double = 0,
  var y2: Double = 0,
  var d: String = "",
  var sNode: String = "",
  var tNode: String = "",
  var sEdge: Int = 0,
  var tEdge: Int = 0,
  var sRad: Double = 0,
  var tRad: Double = 0,
  var iPaths: ListBuffer[String] = ListBuffer(),
  var oPaths: ListBuffer[String] = ListBuffer()
) derives ReadWriter {}
