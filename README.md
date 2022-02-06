# Edits4Credits

[[_TOC_]]

## Intro
Dieses Programm bietet eine simple Lösung einfache Bildbearbeitung vorzunehmen. Eine einfache GUI soll unerfahrenen Benutzen einen leichten Einstieg in die Welt des Grafikdesigns bieten. Unsere Architektur erlaubt eine einfache Erweiterung von benötigten Werkzeugen. 

## Programmstart und Login
Das Programm wird über dieK lasse `org.launcher.GuiDriver.java` gestartert.
Wenn man das Programm startet gelangt man zuerst zu einem **Login** Screen. Wenn man den Login Button drückt gelangt man zu einer Auswahl, bei der man entscheiden kann, ob man den **Editor** oder die **Galerie** öffnen möchte. Die Hauptfunktion unseres Editors besteht darin, Projekte bzw. Bilder zu erstellen und diese dann entweder in der Programm internen Galerie oder extern zu speichern. 

## Editor
Startet man nun den Editor, erscheint ein **Einstellungsfenster**, in der projektspezifische Daten wie Name, Breite und Höhe in Pixeln und Hintergrundfarbe festgelegt werden können. Außerdem kann man sich dazu entscheiden die Galerie zu öffnen, um von dort aus ein Bild als Hintergrund zu nehmen. Die Höhe und Breite passen sich dann automatisch an die Maße des Bildes an.
Im Editor angelangt begegnet einem die typische Bearbeitungsoberfläche. Am oberen Bildschirmrand befindet sich eine **Menüleiste**, mit der Punkte wie Projekt Im- und Export, Transformations Tools, Filter und Zugang zur Galerie gesteuert werden können. Auf der linken Seite befinden sich verschiedene **Tools** zur grafischen Bearbeitung mit unterschiedlichen Formen und Pinseln, mit denen auf das Bild gemalt werden kann.

## Filter und Effekte
Öffnet man die Filter- bzw Effectsoptionen über den jeweiligen Buttons, in der Menüleiste, öffnet sich ein Fenster, mit den Einstellungen der jeweiligen Filter und Effekte. Die Filter werden aktuell nur auf importierte Bilder angewendet. Möchte man sie auf gezeichnete Shapes anwenden, sollte man die Shapes zunächst exportieren und als Image importieren. Unser Programm bietet die Möglichkeit eine Vorschau des Filters im Editor Fenster anzeigen zu lassen, wenn man die Attribute im Optionsfenster ändert. Möchte man den Filter anwenden muss man dies über den Apply Button bestätigen. Schließt man das Optionsfenster ohne den Apply Button zu drücken, wird das Bild im Editor wieder auf seine ursprüngliche Version zurückgesetzt. 

## Transformation
Das Transformations Tool lässt sich ebenfalls über die Menüleiste öffnen. Eine Auswahl an Möglichkeiten befindet sich unter dem Menüpunkt Image. Mit dem Scale Tool kann man das importierte Bild skalieren. Ist der Scale Faktor größer als 100 wird das Bild vergrößert. Ist der Wert kleiner als 100 wird das Bild verkleinert. Die Position des Bildes kann mit dem Move Tool verändert werden. Über die X und Y Position im Bildraster kann man festlegen wo das Bild platziert werden soll. Hier kann man auch negative Werte eingeben. Auch die Transformations Tools bieten eine Preview bevor man die veränderten Werte bestätigt. Über die Enter Taste wird eine Vorschau im Editor Fenster dargestellt. Beim Positions Tool müssen ebenfalls die neuen Werte bestätigt werden.

## Zeichnen
Das Drawing Tool beinhaltet die Formen: Arc, Circle, Elipsis, Line, (Rounded) Rectangle, Polygon und Text. 
Zudem befinden sich Buttons für ein Pinsel Tool, das Bewegen von Formen, Step Forward und Step Backward und zum leeren der gesamten Leinwand. 
Sobald man auf einen der Form- oder Pinselbutton klickt öffnet sich ein Optionsfenster in dem man eine Vielzahl an Einstellungen vornehmen kann. Nach dem Schließen werden diese auf die nächsten Operationen übertragen, solange bis wieder ein Button aus der Leiste gedrückt wird.
Alle Drawing Tools lassen sich mit der Maus steuern. Das Malen mit dem Pinsel und das Erzeugen von Formen wird mit der Maus bedient. 
Zudem lassen sich mit dem Move Tool Formen über die Leinwand ziehen. Diese legen sich dabei immer auf oberste Ebene. Es sollte beachtet werden, dass sich die Pinselstriche nicht bewegen lassen.
Alle Operationen der Formen werden in einer Historie gespeichert. In dieser kann mit dem Step Forward- und Step Backward Tool zwischen den Versionen gewechselt werden Jedoch sollte man beachten, dass wenn man zurück geht und dann eine weitere „Draw“- oder „Move“-Operation durchführt, die restliche Historie gelöscht wird und ab diesem Punkt weiter gearbeitet wird.

## File
Über den Menüpunkt File kann man das Projekt exportieren. Dabei kann man verschiedene Dateitypen über den File Explorer auswählen.

---
## About
GIT
https://gitlab.mi.hdm-stuttgart.de/e4c/edits4credits


### Contact
* Tevin Zielke, tz024@hdm-stuttgart.de
* Johannes Rödel, jr125@hdm-stuttgart.de
* Yannick Kebbe, yk025@hdm-stuttgart.de
