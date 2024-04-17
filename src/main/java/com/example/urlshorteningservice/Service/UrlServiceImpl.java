package com.example.urlshorteningservice.Service;



import com.example.urlshorteningservice.Model.Url;
import com.example.urlshorteningservice.Model.UrlDto;
import com.example.urlshorteningservice.Repository.UrlRepository;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto UrlDto) {
        if(StringUtils.isNotEmpty(UrlDto.getUrl())) {
            String encodeUrl = encodeurl(UrlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalLink(UrlDto.getUrl());
            urlToPersist.setShortLink(encodeUrl);
            urlToPersist.setExpirationDate(getExpirationDate(UrlDto.getExpirationDate(),urlToPersist.getCreationDate()));

            Url urlToReturn = persistShortLink(urlToPersist);

            if(urlToReturn!=null)
                return urlToReturn;

            return null;
        }
        return null;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
        if(StringUtils.isBlank(expirationDate)){
            return creationDate.plusMinutes(2);
        }
        //LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
        //return expirationDateToRet;
        return LocalDateTime.parse(expirationDate);
    }

    private String encodeurl(String url) {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32().hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
        return encodedUrl;
    }

    @Override
    public Url persistShortLink(Url url) {
        Url urlToReturn = urlRepository.save(url);
        return urlToReturn;
    }

    @Override
    public Url getEncodedUrl(String url) {
        Url urlToReturn = urlRepository.findByShortLink(url);
        return urlToReturn;
    }

    @Override
    public void deleteShortLink(Url url) {
       urlRepository.delete(url);
    }


}
