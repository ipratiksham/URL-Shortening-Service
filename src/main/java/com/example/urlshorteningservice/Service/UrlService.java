package com.example.urlshorteningservice.Service;

import com.example.urlshorteningservice.Model.Url;
import com.example.urlshorteningservice.Model.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {

    public Url generateShortLink(UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);

}
