package it.betacom.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the animale database table.
 * 
 */
@Entity
@NamedQuery(name="Animale.findAll", query="SELECT a FROM Animale a")
public class Animale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int matricola;
	
	@Column(name="tipo_animale")
	private String tipoAnimale;
	
	@Column(name="nome_animale")
	private String nomeAnimale;

	@Column(name="data_acquisto")
	private String dataAcquisto;

	private int prezzo;



	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	public Animale() {
	}

	public int getMatricola() {
		return this.matricola;
	}

	public void setMatricola(int matricola) {
		this.matricola = matricola;
	}

	public String getDataAcquisto() {
		return this.dataAcquisto;
	}

	public void setDataAcquisto(String dataAcquisto) {
		this.dataAcquisto = dataAcquisto;
	}

	public String getNomeAnimale() {
		return this.nomeAnimale;
	}

	public void setNomeAnimale(String nomeAnimale) {
		this.nomeAnimale = nomeAnimale;
	}

	public int getPrezzo() {
		return this.prezzo;
	}

	public void setPrezzo(int prezzo) {
		this.prezzo = prezzo;
	}

	public String getTipoAnimale() {
		return this.tipoAnimale;
	}

	public void setTipoAnimale(String tipoAnimale) {
		this.tipoAnimale = tipoAnimale;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Animale [matricola=" + matricola + ", dataAcquisto=" + dataAcquisto + ", nomeAnimale=" + nomeAnimale
				+ ", prezzo=" + prezzo + ", tipoAnimale=" + tipoAnimale + ", cliente=" + cliente + "]";
	}

	
}