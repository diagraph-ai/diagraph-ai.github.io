package in.diagraph.models

import upickle.legacy.ReadWriter

case class Graph(var graphName: String, var graphValue: Node) derives ReadWriter
