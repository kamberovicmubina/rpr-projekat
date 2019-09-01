package sample;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldsValidation {
    public static boolean validName(String n) {
        for (char c : n.toCharArray())
        {
            if (Character.isDigit(c)) {
                return false;
            }
        }
        return validAddress(n);
    }

    public static boolean validAddress(String n) {
        boolean hasLetter = false;
        for (int i = 0; i < n.length(); i++) {
            if (((n.charAt(i) >= 'a' && n.charAt(i) <= 'z') || (n.charAt(i) >= 'A' && n.charAt(i) <= 'Z'))) {
                hasLetter = true;
                break;
            }
        }
        return !n.trim().isEmpty() && hasLetter;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validEMail (String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    public static boolean validPhone (String n) {
        return n.isEmpty() || n.matches("^(?=(?:0){1})(?=[0-9]{9,10}).*"); // pocinje nulom i ima 9 ili 10 cifara
    }

    public static boolean validDate (LocalDate n) {
        LocalDate now = LocalDate.now();
        return n != null && (n.isBefore(now) || n.isEqual(now));
    }

    public static boolean dateInFuture (LocalDate n) {
        LocalDate now = LocalDate.now();
        return n != null && n.isAfter(now);
    }
}
