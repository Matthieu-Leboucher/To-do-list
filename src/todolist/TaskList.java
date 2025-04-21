package todolist;

import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import todolist.special_task.RDV;
import todolist.special_task.SimpleTask;

/**
 * Définition d'une liste de tâches
 */
public class TaskList {
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private String base = null;
	private Connection connection = null;
	
	/*
	 * Constructeur de TaskList
	 */
	public TaskList() {
	}
	
	/**
	 * Constructeur de TaskList
	 * 
	 * @param base Nom de la base de données
	 */
	public TaskList(String base) {
		this.base = base;
	
		try {
			// Connexion
			connection = DriverManager.getConnection("jdbc:sqlite:" + this.base);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
		
			// Création de la table Tasks
			try {
				statement.executeUpdate("create table Tasks (" +
						"id integer primary key autoincrement, " +
						"label string, state boolean, " +
						"day integer default 0, month integer default 0, " +
						"year integer default 0, hour integer default 0, " +
						"minutes integer default 0)");
				System.out.println("Création de la table Tasks");
			} catch (SQLException e) {
				System.out.println("La table Tasks existait déjà");
			}
		
			// Lecture des enregistrements
			ResultSet rs = statement.executeQuery("select * from Tasks");
			while (rs.next()) {
				if (rs.getInt("year") == 0) {
					SimpleTask t = new SimpleTask(rs.getString("label"));
					tasks.add(t);
				} else {
					RDV rdv = new RDV(rs.getString("label"), rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hour"), rs.getInt("minutes"));
					tasks.add(rdv);
				}
			}
		} catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}
	
	public void finalize() {
		// Déconnexion
		try {
			if (connection != null) {
				connection.close();
				System.out.println("Déconnexion de la base de données");
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Affichage d'une liste de tâches sous forme de chaîne de caractères
	 * 
	 * @return La liste des tâches et leur état
	 */
	public String toString() {
		String result = "";
		int len = tasks.size();
		
		for (int i = 0; i < len; i++) {
			result += i + 1 + "/" + len + " : " + tasks.get(i) + "\n";
		}
		
		return result;
	}
	
	/**
	 * Ajout d'une tâche
	 * 
	 * @param newTask Tâche à ajouter
	 * @return <ul>
	 * 		<li>true : la tâche a été ajoutée</li>
	 * 		<li>false : la tâche n'a pas pu être rajoutée</li>
	 * 	</ul>
	 */
	public Boolean addTask(Task newTask) {
		// Si la base de données est utilisée, il faut enregistrer la base
		if (base != null) {
			PreparedStatement stmt = null;
			try {
				if (newTask instanceof SimpleTask) {
					stmt = connection.prepareStatement("insert into Tasks(label, state) values(?, ?)");
					stmt.setString(1,  newTask.getLabel());
					stmt.setInt(2,  0);
				} else {
					stmt = connection.prepareStatement("insert into Tasks(label, state, day, month, year, hour, minutes) values(?, ?, ?, ?, ?, ?, ?)");
					stmt.setString(1,  newTask.getLabel());
					stmt.setInt(2,  0);
					stmt.setInt(3,  ((RDV) newTask).getDate().get(Calendar.DAY_OF_MONTH));
					stmt.setInt(4,  ((RDV) newTask).getDate().get(Calendar.MONTH));
					stmt.setInt(5,  ((RDV) newTask).getDate().get(Calendar.YEAR));
					stmt.setInt(6,  ((RDV) newTask).getDate().get(Calendar.HOUR));
					stmt.setInt(7,  ((RDV) newTask).getDate().get(Calendar.MINUTE));
				}
				stmt.executeUpdate();
				System.out.println("Ajout d'un enregistrement");
			} catch (SQLException e) {
				System.out.println("Ajout impossible : " + e.getMessage());
			}
		}
		
		return tasks.add(newTask);
	}
	
	/**
	 * Ajout d'une tâche simple
	 * 
	 * @param label Label de la tâche à ajouter
	 * @return <ul>
	 * 		<li>true : la tâche a été ajoutée</li>
	 * 		<li>false : la tâche n'a pas pu être rajoutée</li>
	 * 	</ul>
	 */
	public Boolean addTask(String label) {
		SimpleTask newTask = new SimpleTask(label);
		return addTask(newTask);
	}
	
	/**
	 * Ajout d'un rendez-vous
	 * 
	 * @param label Label du RDV à ajouter
	 * @param day Jour du RDV
	 * @param month Mois du RDV
	 * @param year Année du RDV
	 * @param hour Heure du rendez-vous
	 * @param minutes Minutes du RDV
	 * @return <ul>
	 * 		<li>true : la tâche a été ajoutée</li>
	 * 		<li>false : la tâche n'a pas pu être rajoutée</li>
	 * 	</ul>
	 */
	public Boolean addTask(String label, int day, int month, int year, int hour, int minutes) {
		RDV newTask = new RDV(label, day, month, year, hour, minutes);
		return addTask(newTask);
	}
	
	/**
	 * Passage d'une tâche de la liste à l'état "fait"
	 * 
	 * @param idTask Index de la tâche dans la liste
	 * @return <ul>
	 * 		<li>true : la tâche a été notée comme effectuée</li>
	 * 		<li>false : l'état n'a pas pu être modifié</li>
	 * 	</ul>
	 */
	public Boolean taskDone(int idTask) throws IndexOutOfBoundsException {
		if ((idTask <0) || (idTask >= tasks.size())) {
			throw new IndexOutOfBoundsException("Erreur !");
		} else if (tasks.get(idTask).isDone()) {
			return false;
		} else {
			tasks.get(idTask).done();
			return true;
		}
	}
}
