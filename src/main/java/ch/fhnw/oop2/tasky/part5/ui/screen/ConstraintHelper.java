package ch.fhnw.oop2.tasky.part5.ui.screen;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * Diese Klasse hilft die Erzeugung und das Setzen von Constraints 
 * einfacher und expressiver zu machen.
 *
 */
final class ConstraintHelper {

	/**
	 * Setzt einen Column Constraint mit einem Prozent-Wert auf dem spezifizierten 
	 * GridPane Layout.
	 * 
	 * @param target  Das Layout auf dem die Constraint angwendet werden soll
	 * @param percentage Der Prozent-Wert
	 */
	static void setRowPercentConstraint(GridPane target, double percentage) {
		RowConstraints rc = new RowConstraints();
		rc.setPercentHeight(percentage);		
		target.getRowConstraints().add(rc);		
	}
	
	/**
	 * Setzt einen Row Constraint mit einem Prozent-Wert auf dem spezifizierten 
	 * GridPane Layout.
	 * 
	 * @param target Das Layout auf dem die Constraint angwendet werden soll
	 * @param percentage Der Prozent-Wert
	 */
	static void setColumnPercentConstraint(GridPane target, double percentage) {
		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(percentage);
		target.getColumnConstraints().add(cc);
	}
}
