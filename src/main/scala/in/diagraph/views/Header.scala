package in.diagraph.views

import in.diagraph.controller.HeaderCtl
import in.diagraph.objects.Constants.*
import org.scalajs.dom
import org.scalajs.dom.*
import org.scalajs.dom.html.{Anchor, Button, Div, Heading, Select}

object Header extends HeaderCtl {

  var theme: String                         = "light"
  var graphSelectElement: HTMLSelectElement = null

  def header(): html.Heading = {
    val element = createElement[Heading]
    element.style.cssText = headerCss
    element.appendChild(headerDiv())
    element
  }

  private def headerDiv() = {
    val element = createElement[Div]
    element.style.cssText = headerDivCss
    element.appendChild(headerDivNavLeft())
    element.appendChild(headerDivNavRight())
    element
  }

  private def headerDivNavLeft() = {
    val element = headerDivNav()
    element.appendChild(headerDivNavTitle())
    // element.appendChild(headerDivNavLink("About"))
    element
  }

  private def headerDivNavTitle() = {
    val element  = createElement[Anchor]
    val iconPath = "./public/images/favicon.svg"
    element.innerHTML =
      s"""<img src="$iconPath" alt="Theme Icon" style="width: 36px; height: 36px; vertical-align: middle;">
         |<span style="vertical-align: middle;">Diagraph</span>""".stripMargin
    element.style.cssText = headerDivNavTitleCss
    element
  }

  private def headerDivNavRight() = {
    val element = headerDivNav()
//    element.appendChild(headerDivNavSelectBox())
    element.appendChild(headerDivNavLoadButton())
    element.appendChild(headerDivNavSaveButton())
    element.appendChild(headerDivNavTheme())
    // element.appendChild(headerDivNavLink("Login"))
    element
  }

  private def headerDivNavLoadButton() = {
    val element = createElement[Button]
    element.textContent = "Load"
    element.style.cssText = headerGraphSelectCss
    element.onclick = (e: dom.MouseEvent) => loadJsonFromLocal()
    element
  }

  private def headerDivNavSaveButton() = {
    val element = createElement[Button]
    element.textContent = "Save"
    element.style.cssText = headerGraphSelectCss
    element.onclick = (e: dom.MouseEvent) => saveGraphToLocal()
    element
  }

  private def headerDivNavTheme() = {
    val element = headerDivNavLink("")
    updateThemeIcon(element)
    element.onclick = (e: dom.MouseEvent) => {
      theme = if theme == "light" then "dark" else "light"
      document.body.style.cssText = bodyCss()
      updateThemeIcon(element)
      updateCircleFillColor()
      graphSelectElement.dispatchEvent(new Event("change"))
    }
    element
  }

  private def updateThemeIcon(element: html.Anchor): Unit = {
    val iconPath =
      if theme == "light" then "./public/images/light-theme.svg" else "./public/images/dark-theme.svg"
    element.innerHTML = s"""<img src="$iconPath" alt="Theme Icon" style="width: 24px; height: 24px;">"""
  }

  private def updateCircleFillColor(): Unit = {
    val circleElements = document.querySelectorAll("circle")
    for (i <- 0 until circleElements.length) {
      val circle = circleElements(i).asInstanceOf[dom.svg.Circle]
      circle.setAttribute("fill", svgBaseDotsColor())
    }
  }

  private def headerDivNav() = {
    val element = createElement[HTMLElement](NAV)
    element.style.cssText = headerDivNavCss
    element
  }

  private def headerDivNavLink(label: String) = {
    val element = createElement[Anchor]
    element.textContent = label
    element.style.cssText = headerDivNavLinkCss
    element
  }

  private def headerDivNavSelectBox() = {
    graphSelectElement = createElement[Select]
    graphSelectElement.id = GRAPH_SELECT
    graphSelectElement.style.cssText = headerGraphSelectCss

    // Fetch and parse the value from the hidden graphs element
    val graphsValue = document.getElementById("graphs").asInstanceOf[dom.html.Input].value
    val graphNames  = graphsValue.split(",").toList

    // Populate the select box with graph names
    graphNames.foreach { graphName =>
      val option = createElement[dom.html.Option]
      option.textContent = graphName
      option.value = graphName
      graphSelectElement.appendChild(option)
    }

    graphSelectElement.onchange = (e: dom.Event) => loadGraph(graphSelectElement.value)
    graphSelectElement
  }

}
