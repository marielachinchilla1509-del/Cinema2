package proyecto1.cinema;

/*
 * The Movie class represents a film displayed in the cinema system.
 * It contains descriptive information related to production,
 * release details, and artistic contributors. 
 * 
 * This class is typically used to register movies, display movie information
 * in listings, and associate films with scheduled showtimes.
 */
public class Movie {

    /* Actors or main cast of the movie. */
    private String actors;

    /* General description or synopsis of the movie. */
    private String descripcion;

    /* Director responsible for the film production. */
    private String director;

    /* Total duration of the movie. */
    private String duratation;

    /* Language in which the movie is originally produced. */
    private String languaje;

    /* Film production company name. */
    private String prodCompany;

    /* Year the movie was released. */
    private String relaseYear;

    /* Title of the movie. */
    private String title;

    /* Movie genre or type (e.g., Action, Horror, Comedy). */
    private String type;

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDuratation() {
        return duratation;
    }

    public void setDuratation(String duratation) {
        this.duratation = duratation;
    }

    public String getLanguaje() {
        return languaje;
    }

    public void setLanguaje(String languaje) {
        this.languaje = languaje;
    }

    public String getProdCompany() {
        return prodCompany;
    }

    public void setProdCompany(String prodCompany) {
        this.prodCompany = prodCompany;
    }

    public String getRelaseYear() {
        return relaseYear;
    }

    public void setRelaseYear(String relaseYear) {
        this.relaseYear = relaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /*
     * Returns a readable representation of the movie information.
     *
     * @return formatted movie details
     */
    @Override
    public String toString() {
        return "Movie{" +
                "actors=" + actors +
                ", descripcion=" + descripcion +
                ", director=" + director +
                ", duratation=" + duratation +
                ", languaje=" + languaje +
                ", prodCompany=" + prodCompany +
                ", relaseYear=" + relaseYear +
                ", title=" + title +
                ", type=" + type +
                '}';
    }
}
