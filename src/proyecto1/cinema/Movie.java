package proyecto1.cinema;

public class Movie {

    private String actors;

    private String descripcion;

    private String director;

    private String duratation;

    private String languaje;

    private String prodCompany;

    private String relaseYear;

    private String title;

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

    @Override
    public String toString() {
        return "Movie{" + "actors=" + actors + ", descripcion=" + descripcion +
                ", director=" + director + ", duratation=" + duratation + 
                ", languaje=" + languaje + ", prodCompany=" + prodCompany +
                ", relaseYear=" + relaseYear + ", title=" + title + ", type=" 
                + type + '}';
    }
    
    
}
