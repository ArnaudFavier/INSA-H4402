package agile.modele;

/**
 * Mesure un Temps en heures/minutes/secondes
 */
public class Temps implements Comparable<Temps> {
    /**
     * L'heure
     */
    private int heure;

    /**
     * Les minutes
     */
    private int minute;

    /**
     * Les secondes
     */
    private int seconde;

    /// TODO Ajouter les annotations cofoja pr�cisant les conditions de
    /// cr�ation
    /// (heure < 24, >= 0,...)
    /**
     * Constructeur
     * 
     * @param heure
     *            L'heure
     * @param minute
     *            Les minutes
     * @param seconde
     *            Les secondes
     */
    public Temps(int heure, int minute, int seconde) {
	this.heure = heure;
	this.minute = minute;
	this.seconde = seconde;
    }

    /**
     * Constructeur qui converti un nombre de secondes en
     * heures/minutes/secondes
     * 
     * @param secondes
     *            le nombre total de secondes
     */
    public Temps(int secondes) {
	this.heure = secondes / 3600;
	this.minute = secondes / 60 - this.heure * 60;
	this.seconde = secondes - this.minute * 60 - this.heure * 3600;
    }

    /**
     * Comparer deux Temps
     * 
     * @param autreTemps
     *            L'objet Temps avec laquelle on compare l'instance courante
     * @return 0 si �galit�, -1 si this est plus t�t, 1 si this est plus tard
     */
    @Override
    public int compareTo(Temps autreTemps) {
	if (heure == autreTemps.heure) {
	    if (minute == autreTemps.minute) {
		if (seconde == autreTemps.seconde) {
		    return 0;
		} else if (seconde < autreTemps.seconde) {
		    return -1;
		}
		return 1;
	    } else if (minute < autreTemps.minute) {
		return -1;
	    }
	    return 1;
	} else if (heure < autreTemps.heure) {
	    return -1;
	}
	return 1;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + heure;
	result = prime * result + minute;
	result = prime * result + seconde;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Temps other = (Temps) obj;
	if (heure != other.heure)
	    return false;
	if (minute != other.minute)
	    return false;
	if (seconde != other.seconde)
	    return false;
	return true;
    }

    /**
     * @return L'heure
     */
    public int getHeure() {
	return heure;
    }

    /**
     * @return Les minutes
     */
    public int getMinute() {
	return minute;
    }

    /**
     * @return Les secondes
     */
    public int getSeconde() {
	return seconde;
    }

    /**
     * @return La valeur totale des heures, minutes et secondes convertie en
     *         secondes
     */
    public int getTotalSecondes() {
	return ((heure * 60) + minute) * 60 + seconde;
    }

    @Override
    public String toString() {
	StringBuilder strBuilder = new StringBuilder();
	strBuilder.append(heure);
	strBuilder.append('h');

	if (minute < 10) {
	    strBuilder.append('0');
	}
	strBuilder.append(minute);
	return strBuilder.toString();
    }

    /**
     * @return Une durée en chaine de caractère. Le toString() est a utiliser si
     *         on veut représenter une horaire et non une durée
     */
    public String getTimeString() {
	StringBuilder strBuilder = new StringBuilder();
	if (heure != 0) {
	    strBuilder.append(heure);
	    strBuilder.append('h');

	    if (minute < 10) {
		strBuilder.append('0');
	    }
	    strBuilder.append(minute);
	    strBuilder.append('m');

	    if (seconde < 10 && seconde != 0) {
		strBuilder.append('0');
		strBuilder.append(seconde);
		strBuilder.append('s');
	    }
	} else if (minute != 0) {
	    strBuilder.append(minute);
	    strBuilder.append('m');

	    if (seconde < 10 && seconde != 0) {
		strBuilder.append('0');
		strBuilder.append(seconde);
		strBuilder.append('s');
	    }
	} else if (seconde != 0) {
	    strBuilder.append(seconde);
	    strBuilder.append('s');
	}

	return strBuilder.toString();
    }

}
