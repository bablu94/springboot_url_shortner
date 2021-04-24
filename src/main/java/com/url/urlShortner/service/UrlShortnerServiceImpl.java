package com.url.urlShortner.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.google.common.hash.Hashing;
import com.url.urlShortner.model.UrlShortner;
import com.url.urlShortner.model.UrlShortnerDTO;
import com.url.urlShortner.repository.UrlShortnerRepository;

@Service
class UrlShortnerServiceImpl implements UrlShortnerService {

  private UrlShortnerRepository urlRepository;

  public UrlShortnerServiceImpl(UrlShortnerRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  @Override
  public UrlShortner generateShortLink(UrlShortnerDTO urlDto) {

    if (StringUtils.isNotEmpty(urlDto.getUrl())) {
      String encodedUrl = encodeUrl(urlDto.getUrl());
      UrlShortner urlToPersist = UrlShortner.builder().creationDate(LocalDateTime.now())
          .originalUrl(urlDto.getUrl()).shortLink(encodedUrl).build();
      urlToPersist.setExpirationDate(
          getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreationDate()));
      UrlShortner urlToRet = persistShortLink(urlToPersist);

      if (urlToRet != null)
        return urlToRet;

      return null;
    }
    return null;
  }

  private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
    if (StringUtils.isBlank(expirationDate)) {
      return creationDate.plusSeconds(60);
    }
    return LocalDateTime.parse(expirationDate);
  }

  private String encodeUrl(String url) {
    String encodedUrl = "";
    LocalDateTime time = LocalDateTime.now();
    encodedUrl = Hashing.murmur3_32()
        .hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
    return encodedUrl;
  }

  @Override
  public UrlShortner persistShortLink(UrlShortner url) {
    return urlRepository.save(url);

  }

  @Override
  public UrlShortner getEncodedUrl(String url) {
    return urlRepository.findByShortLink(url);
  }

  @Override
  public void deleteShortLink(UrlShortner url) {

    urlRepository.delete(url);
  }

}
