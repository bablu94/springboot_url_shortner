package com.url.urlShortner.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlShortner {

  @Id
  @GeneratedValue
  private long id;
  @Lob
  private String originalUrl;
  private String shortLink;
  private LocalDateTime creationDate;
  private LocalDateTime expirationDate;

}
