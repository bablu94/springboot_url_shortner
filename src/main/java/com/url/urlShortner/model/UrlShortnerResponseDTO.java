package com.url.urlShortner.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlShortnerResponseDTO {

  private String originalUrl;
  private String shortLink;
  private LocalDateTime expirationDate;

}
