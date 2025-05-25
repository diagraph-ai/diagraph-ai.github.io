package in.diagraph

import in.diagraph.objects.Constants.*
import org.scalajs.dom.{HTMLDivElement, HTMLSpanElement, document}

import scala.reflect.ClassTag

package object views {
  val elementMap: Map[Class[?], String] = Map(
    classOf[org.scalajs.dom.html.Div]       -> DIV,
    classOf[org.scalajs.dom.html.Span]      -> SPAN,
    classOf[org.scalajs.dom.svg.SVG]        -> SVG,
    classOf[org.scalajs.dom.html.Image]     -> IMG,
    classOf[org.scalajs.dom.html.Button]    -> BUTTON,
    classOf[org.scalajs.dom.html.Input]     -> INPUT,
    classOf[org.scalajs.dom.html.TextArea]  -> TEXTAREA,
    classOf[org.scalajs.dom.html.Canvas]    -> CANVAS,
    classOf[org.scalajs.dom.html.Select]    -> SELECT,
    classOf[org.scalajs.dom.html.Option]    -> OPTION,
    classOf[org.scalajs.dom.html.Table]     -> TABLE,
    classOf[org.scalajs.dom.html.TableRow]  -> TABLE_ROW,
    classOf[org.scalajs.dom.html.TableCell] -> TABLE_CELL,
    classOf[org.scalajs.dom.html.UList]     -> UL,
    classOf[org.scalajs.dom.html.OList]     -> OL,
    classOf[org.scalajs.dom.html.Heading]   -> HEADER,
    classOf[org.scalajs.dom.html.Anchor]    -> ANCHOR
  )

  def createElement[T](implicit classTag: ClassTag[T]): T = {
    val elementName = elementMap.getOrElse(
      classTag.runtimeClass,
      throw new IllegalArgumentException(s"$UNSUPPORTED_TYPE: ${classTag.runtimeClass}")
    )
    document.createElement(elementName).asInstanceOf[T]
  }

  val svgTags = Set(FOREIGN_OBJECT, SVG, TEXT_PATH, PATH, TEXT, CIRCLE, RECTANGLE, DEFS, MARKER, PATTERN)

  def createElement[T](tagName: String): T =
    if svgTags.contains(tagName) then document.createElementNS(SVG_NS, tagName).asInstanceOf[T]
    else document.createElement(tagName).asInstanceOf[T]

  extension (t: HTMLDivElement) {
    def withStyle(css: String): HTMLDivElement = {
      t.style.cssText = css
      t
    }
  }
  extension (t: HTMLSpanElement) {
    def withStyle(css: String): HTMLSpanElement = {
      t.style.cssText = css
      t
    }
  }
}
