# Übung 8 – Tasky Part 6
In der Übung 7 (Tasky Part 5) haben wir die Anwendung mit der nötigen Funktionalität ergänzt. Tasky funktioniert zwar, weist aber relativ viele Abhängigkeiten unter den Klassen auf und ist so unnötig komplex.

Nehmen Sie als Ausgangspunkt Ihre Lösung von Tasky 5 (UB7), oder die Musterlösung von UB7.

**Aufgabe**
1.	Wir wollen Tasky einem Refactoring unterziehen. Dazu brauchen wir das **Architekturmuster Presentation Model**. Ziel ist es das Tasky das Presentation Model implementiert und somit einfacher und testbarer wird.
2.	Schreiben Sie eine JUnit Testklasse, welche das Presentation Model testet.

**Tipps**
* Schreiben Sie eine Klasse `TaskyPM`. Diese Klasse ist das Presentation Model.
* Stellen Sie sicher, dass es keine Abhängigkeiten von `TaskyPM` zu den View-Klassen gibt.
* Die Controls binden sich an das Presentation Model und dessen Properties.
* Event Handler rufen Methoden auf dem Presentation Model auf. Diese verändern wenn nötig den Zustand der eigenen Properties. Durch die Bindings werden diese Änderungen wieder an die View propagiert.
* Auch Zustände, wie Button-Enabled, Button-Disabled können im Presentation Model abgebildet werden.