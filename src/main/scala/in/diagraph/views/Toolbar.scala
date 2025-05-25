package in.diagraph.views

import in.diagraph.objects.Constants.*
import org.scalajs.dom
import org.scalajs.dom.html.{Div, Image}
import org.scalajs.dom.{document, html}

object Toolbar {

  def toolbar(): html.Div = {
    val element = createElement[Div]
    element.style.cssText = toolbarCss
    element.appendChild(toolbarIcon("/versionedAssets/images/square-light.svg"))
    element
  }

  private def toolbarIcon(src: String) = {
    val element = createElement[Image]
    element.src = src
    element.style.cssText = toolbarIconCss
    element
  }
}
