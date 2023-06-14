package statki3;

import java.io.IOException;
import java.util.*;

public class Main {

	private static Scanner scanner;

	public static void main(String[] args) throws IOException, InterruptedException  {
		
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		
		int rozmiarPlanszy = 5;
		char woda = '~';
		char statek = 'S';
		char trafiony = 'X';
		char pudlo = 'O';
		int liczbaStatkow = 5;
		
		//zainicjowanie i pokazanie planszy na poczatku
		char[][] planszaKomp = tworzeniePlanszy(rozmiarPlanszy, woda, statek, liczbaStatkow);
		char[][] planszaGracz = tworzeniePlanszy(rozmiarPlanszy, woda, statek, liczbaStatkow);
		wyswietlPlansze(planszaKomp, 2, woda, statek, rozmiarPlanszy);
		wyswietlPlansze(planszaGracz, 1, woda, statek, rozmiarPlanszy);
		

		int niezatopioneStatkiKomp = liczbaStatkow;
		int niezatopioneStatkiGracz = liczbaStatkow;
		while (niezatopioneStatkiKomp > 0 || niezatopioneStatkiGracz > 0) {
			
			//ruchy czlowieka

			int[] zgadnijMiejsce = wybierzGdzieStrzelic(rozmiarPlanszy);
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			char updatePlanszy = sprawdzStrzal(zgadnijMiejsce, planszaKomp, statek, woda, trafiony, pudlo);
			if (updatePlanszy == trafiony) {
				niezatopioneStatkiKomp--;
			}
			planszaKomp = aktualizujPlansze(planszaKomp, zgadnijMiejsce, updatePlanszy);
			wyswietlPlansze(planszaKomp, 2, woda, statek, rozmiarPlanszy);
			
			//ruchy komputera
			
			int[] komputerStrzal = strzalLosowy(rozmiarPlanszy);
			updatePlanszy = sprawdzStrzal(komputerStrzal, planszaGracz, statek, woda, trafiony, pudlo);
			if (updatePlanszy == trafiony) {
				niezatopioneStatkiGracz--;
			}
			planszaGracz = aktualizujPlansze(planszaGracz, komputerStrzal, updatePlanszy);
			wyswietlPlansze(planszaGracz, 1, woda, statek, rozmiarPlanszy);

			
		}

	}

	private static char[][] tworzeniePlanszy(int rozmiarPlanszy, char woda, char statek, int liczbaStatkow) {
		char[][] plansza = new char[rozmiarPlanszy][rozmiarPlanszy]; // tworzenie nowej tablicy ktora bedzie
																		// reprezentowac plansze
		for (char[] row : plansza) { // wypelnianie calej planszy woda
			Arrays.fill(row, woda);
		}
		return wypelnionaPlansza(plansza, liczbaStatkow, woda, statek, rozmiarPlanszy); // zwracanie planszy wypelnionej
																						// statkami (funkcja ponizej)
	}

	private static char[][] wypelnionaPlansza(char[][] plansza, int liczbaStatkow, char woda, char statek,
			int rozmiarPlanszy) {
		int stworzoneStatki = 0; // licznik stawionych statkow
		while (stworzoneStatki < liczbaStatkow) {
			int[] miejsce = wylosujMiejsceStatku(rozmiarPlanszy); // wstawianie statkow na podstawie losowego generatora
																	// z funkcji wylosujMiejsceStatku
			char mozliwePostawienie = plansza[miejsce[0]][miejsce[1]];
			if (mozliwePostawienie == woda) { // nowy statek moze byc postawiony tylko na wodzie
				plansza[miejsce[0]][miejsce[1]] = statek;
				stworzoneStatki++;
			}
		}
		return plansza;
	}

	private static int[] wylosujMiejsceStatku(int rozmiarPlanszy) {
		int[] miejsce = new int[2];
		for (int i = 0; i < miejsce.length; i++) {
			miejsce[i] = new Random().nextInt(rozmiarPlanszy); // wstawianie nowego statku w losowe miejsce
		}
		return miejsce;
	}

	private static void wyswietlPlansze(char[][] plansza, int czyjaPlansza, char woda, char statek,
			int rozmiarPlanszy) { // czyjaPlansza==1 - gracz
									// czyjaPlansza==2 - komp
		
		

		if (czyjaPlansza == 2) {		//wyswietla plansze komputera - na niej statki nie moga byc widoczne
			System.out.println("Plansza komputera:");
			System.out.print("   ");

			for (int i = 0; i < rozmiarPlanszy; i++) {
				System.out.print(i + 1 + " ");
			}
			System.out.println();
			
			for (int row = 0; row < rozmiarPlanszy; row++) {
				if (row < 9) {
					System.out.print(" ");
				}
				System.out.print(row + 1 + " ");
				for (int col = 0; col < rozmiarPlanszy; col++) {
					if (plansza[row][col] == statek) {
						System.out.print(woda + " ");
					} else {
						System.out.print(plansza[row][col] + " ");
					}

				}
				System.out.println();
			}
		}
		
		else if (czyjaPlansza == 1) {	// wyswietla planszee gracza - statki widoczne
			System.out.println("Plansza gracza:");
			System.out.print("   ");

			for (int i = 0; i < rozmiarPlanszy; i++) {
				System.out.print(i + 1 + " ");
			}
			System.out.println();
			for (int row = 0; row < rozmiarPlanszy; row++) {
				if (row < 9) {
					System.out.print(" ");
				}
				System.out.print(row + 1 + " ");
				for (int col = 0; col < rozmiarPlanszy; col++) {
						System.out.print(plansza[row][col] + " ");
				}
				System.out.println();
			}
		}
		System.out.println();

	}

	private static int[] strzalLosowy(int rozmiarPlanszy) {
		int row, col;
		row = new Random().nextInt(rozmiarPlanszy);
		col = new Random().nextInt(rozmiarPlanszy);
		return new int[] { row, col };
	}

	private static int[] wybierzGdzieStrzelic(int rozmiarPlanszy) {
		int row, col;
		scanner = new Scanner(System.in);
		do {
			System.out.print("Wiersz: ");
			row = scanner.nextInt();
			
		} while (row < 1 || row > rozmiarPlanszy);
		do {
			System.out.print("Kolumna: ");
			col = scanner.nextInt();
		} while (col < 1 || col > rozmiarPlanszy);

		return new int[] { row - 1, col - 1 };
	}

	private static char sprawdzStrzal(int[] zgadnijMiejsce, char[][] plansza, char statek, char woda, char trafiony,
			char pudlo) {
		int row = zgadnijMiejsce[0];
		int col = zgadnijMiejsce[1];
		char cel = plansza[row][col];
		if (cel == statek) {
			System.out.println("Trafiony!");
			cel = trafiony;
		} else if (cel == woda) {
			System.out.println("Pudlo!");
			cel = pudlo;
		} else {
			System.out.println("Juz tu strzelales.");
		}
		return cel;
	}

	private static char[][] aktualizujPlansze(char[][] plansza, int[] zgadnijMiejsce, char updatePlanszy) {
		int row = zgadnijMiejsce[0];
		int col = zgadnijMiejsce[1];
		plansza[row][col] = updatePlanszy;
		return plansza;
	}
}
