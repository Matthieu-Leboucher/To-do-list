package todolist.special_task;
import todolist.Task;

/**
 * Définition d'une tâche simple
 */
public class SimpleTask extends Task {
	
	/**
	 * Constructeur d'une tâche
	 * 
	 * @param label Description de la tâche
	 */
	public SimpleTask(String label) {
		super(label);
	}
	
	/**
	 * Affichage d'une tâche sous forme de chaîne de caractères
	 * 
	 * @return Le nom de la tâche suivi de son état
	 */
	@Override
	public String toString() {
		String done;
		
		if (isDone()) {
			done = "(effectuée)";
		} else {
			done = "(à faire)";
		}
		return "Tâche simple : " + getLabel() + " " + done;
	}
}
