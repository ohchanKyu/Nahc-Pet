package kr.ac.dankook.cultureApplication.service;

import kr.ac.dankook.cultureApplication.dto.request.CoordinateRequest;
import kr.ac.dankook.cultureApplication.dto.request.LocationRequest;
import kr.ac.dankook.cultureApplication.dto.response.*;
import kr.ac.dankook.cultureApplication.exception.ApiJsonParsingException;
import kr.ac.dankook.cultureApplication.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LocationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.kakao.app-key}")
    private String KAKAO_APP_KEY;
    @Value("${api.kakao.address-to-coordinate}")
    private String KAKAO_ADDRESS_TO_COORDINATE_URL;
    @Value("${api.kakao.coordinate-to-address}")
    private String KAKAO_COORDINATE_TO_ADDRESS;
    @Value("${api.kakao.route}")
    private String KAKAO_ROUTE_URL;
    @Value("${api.kakao.search-by-keyword}")
    private String KAKAO_SEARCH_BY_KEYWORD;
    @Value("${api.kakao.blog}")
    private String KAKAO_BLOG;

    public List<BlogResponse> getPetBlogProcess(String query){
        List<BlogResponse> blogList = new ArrayList<>();

        String apiKey = "KakaoAK " + KAKAO_APP_KEY;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",apiKey);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(KAKAO_BLOG)
                .queryParam("query",query)
                .build();
        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity,String.class);
        String body = response.getBody();
        try {
            JSONObject json = new JSONObject(body);
            JSONArray documents = json.getJSONArray("documents");
            for(Object object : documents) {
                JSONObject jsonObject = (JSONObject) object;
                String title = jsonObject.getString("title");
                String contents = jsonObject.getString("contents");
                String blogURL = jsonObject.getString("url");
                String blogName = jsonObject.getString("blogname");
                String blogImageURL = jsonObject.getString("thumbnail");
                String datetime = jsonObject.getString("datetime").split("T")[0];
                blogList.add(BlogResponse.builder()
                        .title(title)
                        .content(contents)
                        .blogURL(blogURL)
                        .blogName(blogName)
                        .blogImageURL(blogImageURL)
                        .datetime(datetime)
                        .build());
            }
        }catch(JSONException e){
            log.info(e.toString());
            log.info("Blog Process Error");
            return blogList;
        }
        return blogList;
    }
    public List<KakaoPlaceResponse> getAroundPlaceProcess(CoordinateRequest coordinateRequest,String query){
        List<KakaoPlaceResponse> placeList = new ArrayList<>();

        String apiKey = "KakaoAK " + KAKAO_APP_KEY;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",apiKey);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(KAKAO_SEARCH_BY_KEYWORD)
                .queryParam("query",query)
                .queryParam("x",Double.toString(coordinateRequest.getLongitude()))
                .queryParam("y",Double.toString(coordinateRequest.getLatitude()))
                .queryParam("size",15)
                .queryParam("radius",20000)
                .build();
        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity,String.class);
        String body = response.getBody();
        try {
            JSONObject json = new JSONObject(body);
            JSONArray documents = json.getJSONArray("documents");
            for(Object object : documents) {
                JSONObject jsonObject = (JSONObject) object;
                double longitude = jsonObject.getDouble("x");
                double latitude = jsonObject.getDouble("y");
                String placeName = jsonObject.getString("place_name");
                String placeURL = jsonObject.getString("place_url");
                String address = jsonObject.getString("address_name");
                String phoneNumber = jsonObject.getString("phone");
                String category = jsonObject.getString("category_name");
                RouteResponse routeResponse = getRouteInfoProcess(
                        new LocationRequest(
                                coordinateRequest.getLatitude(),coordinateRequest.getLongitude(),
                                latitude,longitude
                        )
                );
                placeList.add(new KakaoPlaceResponse(
                        latitude,longitude,placeName,address,phoneNumber,category,placeURL,routeResponse
                ));
            }
        }catch(JSONException e){
            log.info(e.toString());
            log.info("Around Process Error");
            log.info("Return result");
            return placeList;
        }
        return placeList;
    }

    public String getAddressProcess(CoordinateRequest coordinateRequest){
        String apiKey = "KakaoAK " + KAKAO_APP_KEY;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",apiKey);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(KAKAO_COORDINATE_TO_ADDRESS)
                .queryParam("x",Double.toString(coordinateRequest.getLongitude()))
                .queryParam("y",Double.toString(coordinateRequest.getLatitude()))
                .build();
        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity,String.class);
        String body = response.getBody();
        try{
            JSONObject json = new JSONObject(body);
            JSONArray documents = json.getJSONArray("documents");
            JSONObject addressObject = documents.getJSONObject(0).getJSONObject("road_address");
            return addressObject.getString("address_name");
        }catch(JSONException e){
            throw new ApiJsonParsingException(ErrorCode.API_JSON_PARSING_ERROR);
        }
    }
    public CoordinateResponse getCoordinateProcess(String address){

        String apiKey = "KakaoAK " + KAKAO_APP_KEY;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",apiKey);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(KAKAO_ADDRESS_TO_COORDINATE_URL)
                .queryParam("query",address)
                .build();
        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity,String.class);

        String body = response.getBody();
        try{
            JSONObject json = new JSONObject(body);
            JSONArray documents = json.getJSONArray("documents");
            double longitude = documents.getJSONObject(0).getDouble("x");
            double latitude = documents.getJSONObject(0).getDouble("y");
            return new CoordinateResponse(latitude,longitude);
        }catch(JSONException e){
            log.info("Coordinate Process Error");
            throw new ApiJsonParsingException(ErrorCode.API_JSON_PARSING_ERROR);
        }
    }

    public RouteResponse getRouteInfoProcess(LocationRequest locationRequest){

        String apiKey = "KakaoAK " + KAKAO_APP_KEY;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",apiKey);
        httpHeaders.set("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(KAKAO_ROUTE_URL)
                .queryParam("origin",locationRequest.getStartLongitude()+","+ locationRequest.getStartLatitude())
                .queryParam("destination",locationRequest.getEndLongitude()+","+ locationRequest.getEndLatitude())
                .queryParam("waypoints","")
                .build();

        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity,String.class);

        String body = response.getBody();
        try{
            JSONObject json = new JSONObject(body);
            JSONArray routes = json.getJSONArray("routes");
            JSONObject routeObject = (JSONObject) routes.get(0);
            JSONObject summaryObject = routeObject.getJSONObject("summary");

            String distance;
            int distanceObject = summaryObject.getInt("distance");
            if (distanceObject >= 1000){
                distanceObject = Math.round(distanceObject / 1000.f);
                distance = distanceObject+"km";
            }else {
                distance = distanceObject + "m";
            }
            int duration = Math.round(summaryObject.getInt("duration") / 60.0f);
            int taxiFare = summaryObject.getJSONObject("fare").getInt("taxi");
            int tollFare = summaryObject.getJSONObject("fare").getInt("toll");
            return new RouteResponse(distance,Integer.toString(duration)+"분",taxiFare+"원",tollFare+"원");
        }catch(JSONException e){
            log.info(e.toString());
            log.info("Route Process Error");
            log.info("Return Default Message");
            return new RouteResponse("교통정보 없음.","교통정보 없음.","교통정보 없음.","교통정보 없음.");
        }
    }
}
