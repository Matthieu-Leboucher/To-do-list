package todolist;

public abstract class Task {
	private String label;
	private Boolean state;
	
	protected String getLabel() {
		return label;
	}
	
	protected Boolean getState() {
		return state;
	}
	
	protected void setLabel(String label) {
		this.label = label;
	}
	
	protected void setState(Boolean state) {
		this.state = state;
	}
	
	/**
	 * Constructeur d'une tâche
	 * 
	 * @param label Description de la tâche
	 * @param state État de la tâche
	 */
	public Task(String label) {
		setLabel(label);
		setState(false);
	}

	/**
	 * Affichage d'une tâche sous forme de chaîne de caractère
	 * 
	 * @return Le nom de la tâche suivi de son état
	 */
	public abstract String toString();
	
	/**
	 * Passage d'une tâche à l'état "fait"
	 */
	public void done() {
		setState(true);
	}
	
	/**
	 * Indique l'état de la tâche
	 * 
	 * @return État de la tâche :<ul>
	 * 		<li>true : fait</li>
	 * 		<li>false : à faire</li>
	 * 	</ul>
	 */
	public Boolean isDone() {
		return getState();
	}
}
