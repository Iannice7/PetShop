package it.betacom.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="id_cliente")
	private int idCliente;
	
	private String nome;
	
	private String cognome;

	private String citta;

	private String indirizzo;

	private String telefono;

	//bi-directional many-to-one association to Animale
	@OneToMany(mappedBy="cliente")
	private List<Animale> animales;

	public Cliente() {
	}

	public int getIdCliente() {
		return this.idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getCitta() {
		return this.citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<Animale> getAnimales() {
		return this.animales;
	}

	public void setAnimales(List<Animale> animales) {
		this.animales = animales;
	}

	public Animale addAnimale(Animale animale) {
		getAnimales().add(animale);
		animale.setCliente(this);

		return animale;
	}

	public Animale removeAnimale(Animale animale) {
		getAnimales().remove(animale);
		animale.setCliente(null);

		return animale;
	}

	@Override
	public String toString() {
		return "Cliente [idCliente=" + idCliente + ", citta=" + citta + ", cognome=" + cognome + ", indirizzo="
				+ indirizzo + ", nome=" + nome + ", telefono=" + telefono + "]";
	}

}