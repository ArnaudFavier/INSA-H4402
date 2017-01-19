package hexanome.agenda.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import hexanome.agenda.util.MaterialColors;

/**
 * List of events for the whole application
 */
public final class ListEvent {

    /**
     * Direct access of the list of events
     */
    public static List<Event> events = new ArrayList<>();

    /* Instantiation of content */
    static {

        events.add(new Event(MaterialColors.deepPurple, new DateTime(2017, 1, 19, 13, 30), new DateTime(2017, 1, 19, 16, 0), "TOEIC"));

        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 16, 8, 15), new DateTime(2017, 1, 16, 9, 45), "Architectures des Ordinateurs", "Gaston Berger", "C. Wolf").setGroup(Event.EventGroup.IF3));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 16, 10, 0), new DateTime(2017, 1, 16, 12, 0), "LV2").setGroup(Event.EventGroup.IF3));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 16, 14, 0), new DateTime(2017, 1, 16, 18, 0), "Modélisation des Processus", "501:204", "A. Legait\nB. Ergin").setGroup(Event.EventGroup.IF3));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 17, 8, 0), new DateTime(2017, 1, 17, 10, 0), "LV1").setGroup(Event.EventGroup.IF3));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 17, 10, 0), new DateTime(2017, 1, 17, 12, 0), "Sciences Humaines et Communication").setGroup(Event.EventGroup.IF3));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 17, 14, 0), new DateTime(2017, 1, 17, 18, 0), "Poo - C++ - Avancée", "501:219", "J. Rouzaud-cornabas\nA. Benharkat").setGroup(Event.EventGroup.IF3));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 18, 14, 0), new DateTime(2017, 1, 18, 16, 0), "Architectures des Ordinateurs", "501:208", "F. Dupont de Dinechin\nG. Beslon").setGroup(Event.EventGroup.IF3));;
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 19, 9, 0), new DateTime(2017, 1, 19, 12, 0), "Matinée thématique AEDI").setGroup(Event.EventGroup.IF3));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 20, 10, 0), new DateTime(2017, 1, 20, 12, 0), "Fil Rouge", "Gaston Berger", "L. Brunie\nO. Hasan").setGroup(Event.EventGroup.IF3));
        events.add(new Event(MaterialColors.blue, new DateTime(2017, 1, 20, 14, 0), new DateTime(2017, 1, 20, 16, 0), "Traitement du Signal", "501:202", "S. Bres").setGroup(Event.EventGroup.IF3));

        events.add(new Event(MaterialColors.deepOrange, new DateTime(2017, 1, 16, 8, 0), new DateTime(2017, 1, 16, 12, 0), "OGP", "501:210", "B. Ergin\nA. Legait").setGroup(Event.EventGroup.IF4));
        events.add(new Event(MaterialColors.deepOrange, new DateTime(2017, 1, 16, 14, 0), new DateTime(2017, 1, 16, 16, 0), "LV2").setGroup(Event.EventGroup.IF4));
        events.add(new Event(MaterialColors.deepOrange, new DateTime(2017, 1, 17, 8, 0), new DateTime(2017, 1, 17, 12, 0), "IHM", "501:213", "L. Laporte\nJ. Rouzaud-Cornabas").setGroup(Event.EventGroup.IF4));
        events.add(new Event(MaterialColors.deepOrange, new DateTime(2017, 1, 18, 8, 0), new DateTime(2017, 1, 18, 12, 0), "OGP", "501:321", "B. Ergin\nA. Legait").setGroup(Event.EventGroup.IF4));
        events.add(new Event(MaterialColors.deepOrange, new DateTime(2017, 1, 18, 14, 0), new DateTime(2017, 1, 18, 16, 0), "Anglais").setGroup(Event.EventGroup.IF4));
        events.add(new Event(MaterialColors.deepOrange, new DateTime(2017, 1, 19, 9, 0), new DateTime(2017, 1, 19, 12, 0), "Matinée thématique AEDI").setGroup(Event.EventGroup.IF4));
        events.add(new Event(MaterialColors.deepOrange, new DateTime(2017, 1, 20, 8, 0), new DateTime(2017, 1, 20, 12, 0), "IHM", "501:213", "L. Laporte\nJ. Rouzaud-Cornabas").setGroup(Event.EventGroup.IF4));
    }

    public static List<Event> getEventsForSuscribedGroups() {
        ArrayList<Event> events = new ArrayList<>(100);

        for (Event event : ListEvent.events) {
            switch (event.group) {
                case IF3:
                    if (Options.has3IF)
                        events.add(event);
                    break;
                case IF4:
                    if (Options.has4IF)
                        events.add(event);
                    break;
                case IF5:
                    if (Options.has5IF)
                        events.add(event);
                    break;
                case GI3:
                    if (Options.has3GI)
                        events.add(event);
                    break;
                case GI4:
                    if (Options.has4GI)
                        events.add(event);
                    break;
                case GI5:
                    if (Options.has5GI)
                        events.add(event);
                    break;
                case BDE:
                    if (Options.hasBDE)
                        events.add(event);
                    break;
                case KFete:
                    if (Options.hasKFete)
                        events.add(event);
                    break;
                case Ragda:
                    if (Options.hasRagda)
                        events.add(event);
                    break;
                case Rotonde:
                    if (Options.hasRotonde)
                        events.add(event);
                    break;
                case URL:
                    if (Options.hasURL)
                        events.add(event);
                    break;
                default:
                    events.add(event);
            }
        }

        return events;
    }

    /**
     * Private constructor: no instantiation possible
     */
    private ListEvent() {
    }
}
