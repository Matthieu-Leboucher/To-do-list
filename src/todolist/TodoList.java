package todolist;

public class TodoList {

	public static void main(String[] args) {
		TaskList list = new TaskList("todolist.db");
		
		list.addTask("Test");
		list.addTask("Test 2", 1, 10, 2015, 10, 30);
		
		System.out.println(list);
		list.finalize();
	}

}