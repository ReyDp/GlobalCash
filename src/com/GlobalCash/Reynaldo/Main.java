package com.GlobalCash.Reynaldo;

import com.GlobalCash.Reynaldo.Servicios.Conversion;
import com.GlobalCash.Reynaldo.Servicios.CurrencyApi;
import com.GlobalCash.Reynaldo.modelo.Currency;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CurrencyApi currencyApi = new CurrencyApi();
        List<Conversion> conversions = new ArrayList<>();
        Scanner keyboard = new Scanner(System.in);
        int menu = -1;

        System.out.println("*************************************");
        System.out.println("Bienvenido al conversor de monedas \n");

        // Bucle principal para el menú
        while (menu != 0) {
            showMenu();  // Mostrar el menú de opciones
            try {
                menu = keyboard.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error, no estás ingresando un número válido.\n");
                keyboard.next();  // Limpiar el buffer
                continue;  // Reiniciar el ciclo si la entrada no es válida
            }

            switch (menu) {
                case 1 -> performConversion(keyboard, currencyApi, Currency.USD, Currency.COP, conversions);
                case 2 -> performConversion(keyboard, currencyApi, Currency.COP, Currency.USD, conversions);
                case 3 -> performConversion(keyboard, currencyApi, Currency.USD, Currency.BRL, conversions);
                case 4 -> performConversion(keyboard, currencyApi, Currency.BRL, Currency.USD, conversions);
                case 5 -> performConversion(keyboard, currencyApi, Currency.USD, Currency.ARS, conversions);
                case 6 -> performConversion(keyboard, currencyApi, Currency.ARS, Currency.USD, conversions);
                case 7 -> performConversion(keyboard, currencyApi, Currency.USD, Currency.EUR, conversions);
                case 8 -> performConversion(keyboard, currencyApi, Currency.EUR, Currency.USD, conversions);
                case 9 -> showConversionHistory(conversions);
                case 0 -> System.out.println("Saliendo del sistema.\n");
                default -> System.out.println("Opción incorrecta.\n");
            }
        }
    }

    // Método que imprime el menú de opciones
    private static void showMenu() {
        System.out.println("1. Dólar => Peso colombiano");
        System.out.println("2. Peso colombiano => Dólar");
        System.out.println("3. Dólar => Real Brasileño");
        System.out.println("4. Real Brasileño => Dólar");
        System.out.println("5. Dólar => Peso argentino");
        System.out.println("6. Peso argentino => Dólar");
        System.out.println("7. Dólar => Euro");
        System.out.println("8. Euro => Dólar");
        System.out.println("9. Historial");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opción del siguiente menú: ");
    }

    // Método para realizar una conversión y almacenarla en el historial
    private static void performConversion(Scanner keyboard, CurrencyApi currencyApi, Currency from, Currency to, List<Conversion> conversions) {
        Conversion conversion = showResultMenu(keyboard, currencyApi, from, to);
        System.out.println(conversion.getMessage());
        conversions.add(conversion);  // Añadir la conversión al historial
    }

    // Método para mostrar el historial de conversiones
    private static void showConversionHistory(List<Conversion> conversions) {
        System.out.println("Historial de conversiones:\n");
        if (conversions.isEmpty()) {
            System.out.println("Lista vacía, no ha hecho ninguna conversión.");
        } else {
            conversions.forEach(System.out::println);
        }
    }

    // Método para mostrar el menú de resultados y obtener una conversión
    private static Conversion showResultMenu(Scanner keyboard, CurrencyApi currencyApi, Currency from, Currency to) {
        double amount = Double.NaN;

        while (Double.isNaN(amount)) {
            System.out.println("Diga el valor que desea convertir de " + from + " a " + to);
            String input = keyboard.next().replace(",", ".");

            amount = parseNumber(input, Locale.US);  // Lógica de parseo

            if (Double.isNaN(amount)) {
                System.out.println("Por favor ingrese un valor numérico válido.\n");
            }
        }

        return currencyApi.convertCurrency(from, to, amount);  // Llamar al API para realizar la conversión
    }

    // Método auxiliar para convertir un String en número usando el formato local
    private static double parseNumber(String input, Locale locale) {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        try {
            return numberFormat.parse(input).doubleValue();
        } catch (ParseException e) {
            return Double.NaN;  // En caso de error, retorna NaN
        }
    }
}
