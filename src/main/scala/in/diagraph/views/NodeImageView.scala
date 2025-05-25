package in.diagraph.views

import in.diagraph.models.*
import org.scalajs.dom.html.Div
import org.scalajs.dom.{Event, window}

trait NodeImageView {
  this: Node =>
  def nodeImageView(): Div = {
    val content = createElement[Div]
    content.style.cssText = s"""
                overflow: auto;
                resize: both;
                box-sizing: border-box;
              """
    val image   = createElement[Div]
    props.width = if props.width == 0 then 100.0 else props.width
    props.height = if props.height == 0 then 100.0 else props.height
    image.innerHTML =
      s"<img src='${props.imagePath}' width='${props.width}px' height='${props.height}' ondragstart='return false;'>"
    image.ondblclick = (e: Event) => {
      props.imagePath = window.prompt("Enter Image Path")
      image.innerHTML =
        s"<img src='${props.imagePath}' width='${props.width}px' height='${props.height}' ondragstart='return false;'>"
    }
    content.appendChild(image)
    content
  }
}
