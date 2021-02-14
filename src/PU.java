import java.util.Scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Comparator;

class Date implements Comparable<Date> { //Klas koito vie razvihte v chas 
	// I use it because is very convenient and helpful for me to accomplish the task
	private int val;

	private static boolean leapYear(int y) {
		if (y % 400 == 0)
			return true;
		if (y % 100 == 0)
			return false;
		return (y & 3) == 0;
	}

	Date(String s) {
		final HashSet<Integer> shortMonths = new HashSet<Integer>(Arrays.asList(2, 4, 6, 9, 11));
		val = 0;
		s += '.';
		int[] d = new int[3];
		for (int i = 0; i < 3; i++) {
			int p = s.indexOf('.');
			if (p < 0)
				return;
			try {
				d[i] = Integer.parseInt(s.substring(0, p));
			} catch (Exception e) {
				return;
			}
			s = s.substring(p + 1);
		}
		if (d[2] < 0)
			return;
		if (d[1] < 1 || d[1] > 12)
			return;
		if (d[0] < 1 || d[0] > 31)
			return;
		if (shortMonths.contains(d[1]) && d[0] > 30)
			return;
		if (d[1] == 2) {
			if (d[0] > 29)
				return;
			if (d[0] > 28 && !leapYear(d[2]))
				return;
		}
		val = d[0] + 100 * d[1] + 10000 * d[2];
	}

	@Override
	public int compareTo(Date d) throws IllegalStateException {
		if (val == 0)
			throw new IllegalStateException();
		return this.val - d.val;
	}

	int getYear() {
		return val / 10000;
	}

	@Override
	public String toString() {
		if (val == 0)
			return "Illegal date";
		return String.format("%02d.%02d.%04d", val % 100, (val / 100) % 100, val / 10000);
	}
}

class Store implements Comparable<Store> {
	private String code;
	private String name;
	private double quantity;
	private int avail;
	private String group;
	private Date regDate;
	private String position;

	Store(Scanner inp, boolean prompt) {
		if (prompt)
			System.out.print("Code: ");
		code = inp.nextLine();
		if (prompt)
			System.out.print("Name: ");
		name = inp.nextLine();
		if (prompt)
			System.out.print("Product's quantity: ");
		quantity = inp.nextDouble();
		if (prompt)
			System.out.print("Days to expire: ");
		avail = inp.nextInt();
		if (prompt)
			System.out.print("Special or normal product ( E or S) : ");
		group = inp.next();
		if (prompt)
			System.out.print("Registration date (dd.mm.yyyy): ");
		String s = inp.next();
		regDate = new Date(s);
		if (prompt)
			System.out.print("Position in the store: ");
		position = inp.next();

		inp.nextLine();
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public double getQuantity() {
		return quantity;
	}

	public int getAvail() {
		return avail;
	}

	public String getGroup() {
		return group;
	}

	public Date getRegDate() {
		return regDate;
	}

	public String getPosition() {
		return position;
	}

	public String AdvancedGroups(String a) {
		String s = "";
		if (a.equals("E"))
			s = ", B%=40 ";
		return s;
	}

	@Override
	public String toString() {
		return position + ", " + code + ", " + name + ", " + String.format("%.2f", quantity) + ", " + regDate + ", "
				+ avail + AdvancedGroups(group);
	}

	@Override
	public int compareTo(Store c) {
		return this.position.compareTo(c.position);
	}
}

class Cmp1 implements Comparator<Store> {
	@Override
	public int compare(Store a, Store b) {
		if (a.getRegDate().compareTo(b.getRegDate()) < 0)
			return -1;
		if (a.getRegDate().compareTo(b.getRegDate()) > 0)
			return 1;

		if (a.getAvail() < b.getAvail())
			return -1;
		if (a.getAvail() > b.getAvail())
			return 1;
		return 0;
	}
}

class PU {
	static int N;
	static Scanner inp = null;
	static ArrayList<Store> stores = new ArrayList<Store>();
	static ArrayList<Store> stores2 = new ArrayList<Store>();

	static void problem1(boolean prompt) {
		for (int i = 0; i < N; i++) {
			if (prompt)
				System.out.printf("Client #%d data%n", i + 1);
			stores.add(new Store(inp, prompt));
		}
	}

	static void problem2() {
		Collections.sort(stores);
		for (Store s : stores)
			System.out.println(s);
	}

	static void problem3() {
		for (Store c : stores) {
			if (c.getGroup().equals("E"))
				stores2.add(c);
		}

		stores2.sort(new Cmp1());
		for (Store s : stores2)
			System.out.println(s);
	}

	static void problem4(String a) {
		stores2.clear();
		double sum = 0;

		Collections.sort(stores);
		for (Store c : stores) {
			if (c.getCode().equals(a))
				stores2.add(c);
		}

		for (Store s : stores2) {
			System.out.println(s);
			sum += s.getQuantity();
		}

		System.out.println("The whole quantity is: " + String.format("%.2f", sum));

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final boolean prompt = false;
		File f = null;
		if (prompt) {
			inp = new Scanner(System.in);
			do {
				System.out.print("Products count (1 to 10000): ");
				N = inp.nextInt();
			} while (N < 1 || N > 10000);
		} else {
			f = new File("Data.txt");
			try {
				inp = new Scanner(f);
			} catch (Exception e) {
				System.out.println("File Data.txt not found");
				return;
			}
			N = inp.nextInt();
		}
		inp.nextLine();
		problem1(prompt);
		problem2();
		System.out.println();
		problem3();
		System.out.println();

		if (prompt)
			System.out.print("Code: ");
		String r = inp.nextLine();
		inp.close();
		System.out.println("You choose code " + r);
		problem4(r);
	}

}
