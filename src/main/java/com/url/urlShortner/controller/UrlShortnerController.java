package com.url.urlShortner.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.url.urlShortner.model.UrlShortner;
import com.url.urlShortner.model.UrlShortnerDTO;
import com.url.urlShortner.model.UrlShortnerResponseDTO;
import com.url.urlShortner.model.UrlShortnerResponseErrorDTO;
import com.url.urlShortner.service.UrlShortnerService;


@RestController
public class UrlShortnerController {


  private UrlShortnerService urlService;

  public UrlShortnerController(UrlShortnerService urlService) {
    this.urlService = urlService;
  }

  @PostMapping("/generate")
  public ResponseEntity<?> generateShortLink(@RequestBody UrlShortnerDTO urlDto) {
    UrlShortner urlToRet = urlService.generateShortLink(urlDto);

    if (urlToRet != null) {
      UrlShortnerResponseDTO urlResponseDto = new UrlShortnerResponseDTO();
      urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
      urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
      urlResponseDto.setShortLink(urlToRet.getShortLink());
      return new ResponseEntity<UrlShortnerResponseDTO>(urlResponseDto, HttpStatus.OK);
    }

    UrlShortnerResponseErrorDTO urlErrorResponseDto = new UrlShortnerResponseErrorDTO();
    urlErrorResponseDto.setStatus(HttpStatus.NOT_FOUND.getReasonPhrase());
    urlErrorResponseDto.setError("There was an error processing your request. please try again.");
    return new ResponseEntity<UrlShortnerResponseErrorDTO>(urlErrorResponseDto, HttpStatus.OK);

  }

  @GetMapping("/{shortLink}")
  public ResponseEntity<UrlShortnerResponseErrorDTO> redirectToOriginalUrl(
      @PathVariable String shortLink, HttpServletResponse response) throws IOException {

    if (StringUtils.isEmpty(shortLink)) {
      UrlShortnerResponseErrorDTO urlErrorResponseDto = new UrlShortnerResponseErrorDTO();
      urlErrorResponseDto.setError("Invalid Url");
      urlErrorResponseDto.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
      return ResponseEntity.ok(urlErrorResponseDto);
    }
    UrlShortner urlToRet = urlService.getEncodedUrl(shortLink);

    if (urlToRet == null) {
      UrlShortnerResponseErrorDTO urlErrorResponseDto = new UrlShortnerResponseErrorDTO();
      urlErrorResponseDto.setError("Url does not exist");
      urlErrorResponseDto.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
      return ResponseEntity.ok(urlErrorResponseDto);
    }

    if (urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {
      urlService.deleteShortLink(urlToRet);
      UrlShortnerResponseErrorDTO urlErrorResponseDto = new UrlShortnerResponseErrorDTO();
      urlErrorResponseDto.setError("Url has been expired. Please try to generate new one");
      urlErrorResponseDto.setStatus(HttpStatus.OK.getReasonPhrase());
      return ResponseEntity.ok(urlErrorResponseDto);
    }

    response.sendRedirect(urlToRet.getOriginalUrl());
    return null;
  }

}
