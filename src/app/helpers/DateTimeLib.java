package app.helpers;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Classe de méthodes statiques permettant des conversions facilitées entre date, heure et chaînes de caractères
 * (String).
 *
 * @author Jean-Claude Stritt
 * @version 1.0 / 01-JAN-2010
 * @version 1.1 / 01-JUN-2010 (ajout des méthodes sql et iso8601)
 * @version 1.2 / 28-DEC-2010 (ajout de getYear, getMonth, getDay, extractDateInfo)
 * @version 1.3 / 03-MAR-2011 (ajout de 1 à tous les "c.get(Calendar.MONTH)")
 * @version 1.4 / 20-FEV-2012 (ajout de méthodes pour récupérer la date courante)
 * @version 1.5 / 08-MAR-2012 (ajout et amélioration de quelques méthodes)
 * @version 1.6 / 24-SEP-2012 (getDate(String s) redevient "stringToDate")
 */
public class DateTimeLib {

  private static long timeStamp = 0;

  public static SimpleDateFormat getLocaleFormat(String format) {
    Locale locale = Locale.getDefault();
    return new SimpleDateFormat(format, locale);
  }

  /**
   * Retourne le format local d'une date.
   */
  public static SimpleDateFormat getLocaleDateFormat() {
    return getLocaleFormat("d.M.yyyy");
  }

  /**
   * Retourne le format local d'un temps.
   */
  public static SimpleDateFormat getLocaleTimeFormat() {
    return getLocaleFormat("hh:mm:ss");
  }

  /**
   * Retourne le format local pour une date et heure au standard européen.
   */
  public static SimpleDateFormat getLocaleDateTimeFormat() {
    return getLocaleFormat("d MMMM yyyy - hh:mm:ss");
  }

  /**
   * Retourne la date du jour sous la forme d'un string.<br>
   * Le format est définit dans getLocaleDateFormat avec la locale du pays.
   *
   * @return la date du jour sous la forme d'un string
   */
  public static String getCurrentDate() {
    SimpleDateFormat ldf = getLocaleDateFormat();
    return ldf.format(getDate());
  }

  /**
   * Retourne la date du jour sous la forme d'un string.<br>
   * Le format est définit en paramètre (ex: "d.M.yyyy"").
   *
   * @param format un format de date sous la forme d'un String
   *
   * @return la date du jour sous la forme d'un string
   */
  public static String getCurrentDate(String format) {
    SimpleDateFormat ldf = getLocaleFormat(format);
    return ldf.format(getDate());
  }

  /**
   * Retourne l'heure du jour sous la forme d'un string.<br>
   * Le format est définit dans getLocaleTimeFormat avec la locale du pays.
   *
   * @return l'heure du jour sous la forme d'un string
   */
  public static String getCurrentTime() {
    SimpleDateFormat ldf = getLocaleTimeFormat();
    return ldf.format(getDate());
  }

  /**
   * Retourne l'heure du jour sous la forme d'un string.<br>
   * Le format doit être fourni (ex: "hh:mm").
   *
   * @param format un format de temps sous la forme d'un String
   *
   * @return l'heure du jour sous la forme d'un string
   */
  public static String getCurrentTime(String format) {
    SimpleDateFormat ldf = getLocaleFormat(format);
    return ldf.format(getDate());
  }

  /**
   * Retourne la date du jour (avec heure, minutes, secondes, ms aussi).
   *
   * @return la date du jour
   */
  public static Date getDate() {
    Calendar c = new GregorianCalendar();
    return c.getTime();
  }

  /**
   * Calcule une date en fournissant le jour, le mois et l'année.<br>
   * L'heure est mise à zéro.
   *
   * @param day le jour
   * @param month le mois
   * @param year l'année
   * @return une date d'après les informations données
   */
  public static Date getDate(int day, int month, int year) {

    // récupère un objet calendrier et le remplit avec la date spécifiée
    Calendar cal = Calendar.getInstance();

    // met les infos de temps à zéro
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.YEAR, year);

    // met les infos de temps à zéro
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    // retourne la date
    Date date = cal.getTime();
    return date;
  }

  /**
   * Retourne la date spécifiée en paramètre sans les informations de temps (HH:MM:SS:mm).
   *
   * @param date une date quelconque (avec ou non les informations de temps)
   * @return une date sans les informations de temps
   */
  public static Date getDateWithoutTime(Date date) {

    // récupère un objet calendrier et le remplit avec la date spécifiée
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    // met les infos de temps à zéro
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    // retourne une date sans l'heure
    date = cal.getTime();
    return date;
  }

  /**
   * Calcule un temps temps en fournissant l'heure, les minutes, les secondes.
   *
   * @param hour l'heure
   * @param minute les minutes
   * @param second les secondes
   * @return un temps construit avec la date du jour et les informations fournies
   */
  public static Date getTime(int hour, int minute, int second) {

    // récupère un objet calendrier et le remplit avec la date spécifiée
    Calendar cal = Calendar.getInstance();

    // met les infos de temps avec les données fournies
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, second);
    cal.set(Calendar.MILLISECOND, 0);

    // retourne la date
    Date date = cal.getTime();
    return date;
  }

  /**
   * Retourne un tableau d'entiers avec le jour, le mois et l'année de la date spécifiée en paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau d'entiers avec le jour, le mois et l'année
   */
  public static int[] extractDateInfo(Date date) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    int[] info = new int[3];
    info[0] = c.get(Calendar.DAY_OF_MONTH);
    info[1] = c.get(Calendar.MONTH) + 1;
    info[2] = c.get(Calendar.YEAR);
    return info;
  }

  /**
   * Retourne un tableau d'entiers avec l'heure, les minutes, les secondes et les milisecondes de la date spécifiée en
   * paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau[4] d'entiers avec hh, mm, ss, ms
   */
  public static int[] extractTimeInfo(Date date) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    int[] info = new int[4];
    info[0] = c.get(Calendar.HOUR_OF_DAY);
    info[1] = c.get(Calendar.MINUTE);
    info[2] = c.get(Calendar.SECOND);
    info[3] = c.get(Calendar.MILLISECOND);
    return info;
  }

  /**
   * Retourne un tableau d'entiers avec le jour, le mois, l'année, l'heure, les minutes, les secondes et les
   * milisecondes de la date spécifiée en paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau[7] avec JJ, MM, AA, hh, mm, ss, ms
   */
  public static int[] extractDateTimeInfo(Date date) {
    Calendar c = new GregorianCalendar();
    int[] info = new int[7];
    info[0] = c.get(Calendar.DAY_OF_MONTH);
    info[1] = c.get(Calendar.MONTH) + 1;
    info[2] = c.get(Calendar.YEAR);
    info[3] = c.get(Calendar.HOUR_OF_DAY);
    info[4] = c.get(Calendar.MINUTE);
    info[5] = c.get(Calendar.SECOND);
    info[6] = c.get(Calendar.MILLISECOND);
    return info;
  }

  /**
   * Convertit une date (java.util.Date) vers une représentation String standard.
   *
   * @param date une date de la classe java.util.Date
   * @return la même date au format String
   */
  public static String dateToString(Date date) {
    String sDate = "";
    if (date != null) {
      SimpleDateFormat ldf = getLocaleDateFormat();
      sDate = ldf.format(date);
    }
    return sDate;
  }

  /**
   * Convertit une date (java.util.Date) vers une représentation String construite avec le format demandé.
   *
   * @param date une date de la classe java.util.Date
   * @param format un format de date (ou de temps) sous la forme d'un String
   * @return la même date au format String
   */
  public static String dateToString(Date date, String format) {
    String sDate = "";
    if (date != null) {
      SimpleDateFormat ldf = getLocaleFormat(format);
      sDate = ldf.format(date);
    }
    return sDate;
  }

  /**
   * Convertit une date avec heure vers une représentation String standard.
   *
   * @param dateTime une date-heure de la classe java.util.Date
   * @return la même date-heure au format String
   */
  public static String dateTimeToString(Date dateTime) {
    String sDate = "";
    if (dateTime != null) {
      SimpleDateFormat ldf = getLocaleDateTimeFormat();
      sDate = ldf.format(dateTime);
    }
    return sDate;
  }

  /**
   * Convertit une chaîne de caractères (String) représentant une date en une date de la classe java.util.Date.
   *
   * @param sDate la chaîne contenant une date
   * @return une date de la classe java.util.Date
   */
  public static Date stringToDate(String sDate) {
    Date date;
    SimpleDateFormat ldf = getLocaleDateFormat();
    ldf.setLenient(false);
    try {
      date = ldf.parse(sDate);
    } catch (ParseException ex) {
      date = getDate(1, 1, 1970);
    }
    return date;
  }

  /**
   * Extrait l'année d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return l'année extrait de la date donnée
   */
  public static int getYear(Date date) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    return c.get(Calendar.YEAR);
  }

  /**
   * Extrait le mois d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return le mois extrait de la date donnée
   */
  public static int getMonth(Date date) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    return c.get(Calendar.MONTH) + 1;
  }

  /**
   * Extrait le jour d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return le jour extrait de la date donnée
   */
  public static int getDay(Date date) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    return c.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Calcule et retourne l'âge d'une personne d'après sa date de naissance et la date courante.
   *
   * @param birthDate la date de naissance (java.util.Date)
   * @return l'âge sous la forme d'un entier (int)
   */
  public static int getCurrentAge(Date birthDate) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(birthDate);
    GregorianCalendar now = new GregorianCalendar();
    int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
    if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
            || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
            && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
      res--;
    }
    return res;
  }

  /**
   * Convertit une sqldate en sa représentation "chaîne de caractères".
   *
   * @param sqldate une date de la classe java.sql.Date
   * @return une chaîne avec la représentation de la date
   */
  public static String sqldateToString(java.sql.Date sqldate) {
    Date date = sqldateToDate(sqldate);
    return dateToString(date);
  }

  /**
   * Convertit une représentation de date (String) en une date de la classe java.sql.Date.
   *
   * @param sDate une chaîne avec une date à l'intérieur
   * @return une date de la class java.sql.Date
   */
  public static java.sql.Date stringToSqldate(String sDate) {
    Date d = stringToDate(sDate);
    return new java.sql.Date(d.getTime());
  }

  /**
   * Convertit une date de type java.sql.Date vers une date de type java.util.Date.
   *
   * @param sqlDate une date au format java.sql.Date
   * @return une date au format java.util.Date
   */
  public static Date sqldateToDate(java.sql.Date sqlDate) {
    return new java.util.Date(sqlDate.getTime());
  }

  /**
   * Convertit une date dans le format String Iso8601. C'est un format de type "YYYY-MM-DD HH:MM:SS".
   *
   * @param date une date de la classe java.util.Date
   * @return une chaîne avec une date au format Iso8601
   */
  public static String dateToIso8601(Date date) {
    String sDate = "";
    if (date != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      sDate = sdf.format(date);
    }
    return sDate;
  }

  /**
   * Convertit une date (java.util.Date) vers une chaîne de caractères de l'année à la seconde (idéal pour les noms de
   * fichiers d'impression).
   *
   * @param date une date de la classe java.util.Date
   * @return une chaîne avec une date au format Iso8601
   */
  public static String dateToFileName(Date date) {
    String sDate = "";
    if (date != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
      sDate = sdf.format(date);
    }
    return sDate;
  }

  /**
   * Reset du chnonomètre interne de cette classe.
   */
  public static void chronoReset() {
    GregorianCalendar now = new GregorianCalendar();
    timeStamp = now.getTime().getTime();
  }

  /**
   * Retourne sous forme numérique (float) le temps écoulé depuis la mise à zéro du chronomètre interne.
   */
  public static float chronoElapsedTime() {
    GregorianCalendar now = new GregorianCalendar();
    return (float) (now.getTime().getTime() - timeStamp) / 1000;
  }

  /**
   * Retourne sous forme d'une chaîne de caractères String le temps écoulé depuis la mise à zéro du chronomètre interne.
   */
  public static String chronoStringElapsedTime() {
    Locale locale = Locale.getDefault();
    DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(locale);
    df.applyPattern("#,##0.000");
    return df.format(chronoElapsedTime());
  }

  /**
   * Cette méthode transforme un objet LocalDate en un objet Date. L'objet LocalDate ne possédant pas d'heure, on lui
   * donne minuit et la zone horaire du système.
   *
   * @param localDate L'objet à transformé en Date.
   * @return L'objet Date correspondant à la date de l'objet LocalDate.
   */
  public static Date localDateToDate(LocalDate localDate) {
    if (localDate != null) {
      return Date.from(Instant.from(localDate.atStartOfDay(ZoneId.systemDefault())));
    } else {
      return null;
    }
  }

  /**
   * Cette méthode transforme un objet Date en un objet LocalDate. On définit la zone horaire de l'objet LocalDate à la
   * zone horaire du système.
   *
   * @param date L'objet à transformé en LocalDate.
   * @return L'objet LocalDate correspondant à la date de l'objet Date.
   */
  public static LocalDate dateToLocalDate(java.util.Date date) {
    if (date != null) {
      return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    } else {
      return null;
    }
  }

}
