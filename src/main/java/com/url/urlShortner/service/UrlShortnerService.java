package com.url.urlShortner.service;

import org.springframework.stereotype.Service;
import com.url.urlShortner.model.UrlShortner;
import com.url.urlShortner.model.UrlShortnerDTO;

@Service
public interface UrlShortnerService {

  public UrlShortner generateShortLink(UrlShortnerDTO urlDto);

  public UrlShortner persistShortLink(UrlShortner url);

  public UrlShortner getEncodedUrl(String url);

  public void deleteShortLink(UrlShortner url);

}
