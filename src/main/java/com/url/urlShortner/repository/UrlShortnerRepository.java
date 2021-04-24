package com.url.urlShortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.url.urlShortner.model.UrlShortner;

@Repository
public interface UrlShortnerRepository extends JpaRepository<UrlShortner, Long> {

  public UrlShortner findByShortLink(String shortLink);


}
