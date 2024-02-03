package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.WishlistItem;

public interface WishlistRepository {

	public List<WishlistItem> findAllByKorisnik(Long korisnikId);
	public int save(WishlistItem wishlistItem);
	public void remove(Long idWishlist);
}
