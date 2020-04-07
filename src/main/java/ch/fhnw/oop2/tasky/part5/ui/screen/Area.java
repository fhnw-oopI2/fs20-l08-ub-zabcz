package ch.fhnw.oop2.tasky.part5.ui.screen;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * Diese Klasse erzeugt eine Fläche in Form einer Region. Nützlich zum debuggen der 
 * Layouts. Wir brauchen hier ein Label. Das können wir später direkt so weiter
 * verwenden. Ein Label ist auch eine Region.
 */
public final class Area {

	/**
	 * Erzeugt eine neue Region (hier ein Label).
	 * 
	 * @param color Color als Hex-String
	 * @return  Die neue Region
	 */
	public static Region createRegion(String color) {
		final Label label = new Label();
		label.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setStyle("-fx-background-color: " +color + ";");
		return label;
	}
}
