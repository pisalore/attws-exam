package com.unifi.attws.exam.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "Exhibition")
@Table(name = "exhibitions")
public class Exhibition {
	
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Museum museum;
	
	@Column(name = "Exhibition_Name", unique=true)
	private String name;
	
	@Column(name = "Total_seats")
	private int totalSeats;
	
	@Column(name = "Booked_seats")
	private int bookedSeats;
	

	public Exhibition(Museum museum, String name, int totalSeats) {
		super();
		this.museum = museum;
		this.name = name;
		this.totalSeats = totalSeats;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Museum getMuseum() {
		return museum;
	}

	public void setMuseum(Museum museum) {
		this.museum = museum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getBookedSeats() {
		return bookedSeats;
	}

	public void setBookedSeats(int bookedSeats) {
		this.bookedSeats = bookedSeats;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bookedSeats;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((museum == null) ? 0 : museum.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + totalSeats;
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
		Exhibition other = (Exhibition) obj;
		if (bookedSeats != other.bookedSeats)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (museum == null) {
			if (other.museum != null)
				return false;
		} else if (!museum.equals(other.museum))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (totalSeats != other.totalSeats)
			return false;
		return true;
	}

}
