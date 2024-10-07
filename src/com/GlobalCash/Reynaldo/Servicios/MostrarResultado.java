package com.GlobalCash.Reynaldo.Servicios;

import com.GlobalCash.Reynaldo.Servicios.Conversion;
import com.GlobalCash.Reynaldo.Servicios.CurrencyApi;
import com.GlobalCash.Reynaldo.modelo.Currency;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

public class MostrarResultado {

    /**
     * Método que muestra el menú para que el usuario ingrese un valor y realice la conversión.
     *
     * @param keyboard      Scanner para la entrada del usuario.
     * @param currencyApi   API que maneja las conversiones de moneda.
     * @param from          Moneda origen.
     * @param to            Moneda destino.
     * @return Conversion   El resultado de la conversión.
     */
    public static Conversion showResultMenu(Scanner keyboard, CurrencyApi currencyApi, Currency from, Currency to) {
        double amount = Double.NaN;

        while (Double.isNaN(amount) || amount <= 0) {  // Validar que el valor sea positivo
            System.out.print("Ingrese el valor que desea convertir de " + from + " a " + to);
            String input = keyboard.next().replace(",", ".");  // Reemplazar comas por puntos

            amount = parseNumber(input, Locale.US);  // Intentar convertir el input a número

            if (Double.isNaN(amount)) {
                System.out.println("Por favor ingrese un valor numérico válido.\n");
            } else if (amount <= 0) {
                System.out.println("El monto debe ser mayor que cero.\n");
            }
        }

        // Llamar al API para realizar la conversión
        return currencyApi.convertCurrency(from, to, amount);
    }

    /**
     * Método auxiliar para convertir una cadena de texto a un número, basado en la configuración local.
     *
     * @param input   Cadena de texto ingresada por el usuario.
     * @param locale  Configuración regional a usar para el formato numérico.
     * @return double Valor numérico resultante o NaN si no es posible convertir.
     */
    private static double parseNumber(String input, Locale locale) {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        try {
            return numberFormat.parse(input).doubleValue();  // Intentar parsear el número
        } catch (ParseException e) {
            System.err.println("Error al intentar convertir el valor: " + e.getMessage());  // Mostrar error en consola
            return Double.NaN;  // Retornar NaN si hay un error
        }
    }
}
