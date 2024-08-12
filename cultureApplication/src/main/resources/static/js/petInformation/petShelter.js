let kakaoMap;

let mapContainerBox = document.getElementById('map-wrapper');
let mapContainer = document.getElementById("map");
mapContainerBox.style.display="block";

let options = {
    center: new kakao.maps.LatLng(35.95,128.25),
    level: 12
};
let map = new kakao.maps.Map(mapContainer, options);
kakaoMap = map;

let imageSrc = "../../assets/check.png";

for(let i=0;i<shelterData.length;i++){
    let imageSize = new kakao.maps.Size(45, 45);
    let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

    let placeMarker = new kakao.maps.Marker({
        map: map,
        position:  new kakao.maps.LatLng(shelterData[i].latitude, shelterData[i].longitude),
        image : markerImage
    });
    let phoneText;
    if (shelterData[i].phoneNumber[0] === '*'){
        phoneText = '등록된 번호가 없습니다.'
    }else{
        phoneText = shelterData[i].phoneNumber;
    }
    let content =`
                <div class="customOverlay-info">
                    <p class="customOverlay-info-placeName">${shelterData[i].placeName}</p>
                    <p class="customOverlay-info-manage"><i class="fa-solid fa-gear"></i> ${shelterData[i].manageName}</p>
                    <p class="customOverlay-info-address"><i class="fa-solid fa-location-dot"></i> ${shelterData[i].address}</p>
                    <p class="customOverlay-info-phoneNumber"><i class="fa-solid fa-phone"></i> ${phoneText}</p>
                </div>
            `
    let customOverlay = new kakao.maps.CustomOverlay({
        position:  new kakao.maps.LatLng(shelterData[i].latitude, shelterData[i].longitude),
        content: content,
        yAnchor: 1,
    });
    kakao.maps.event.addListener(placeMarker, 'mouseover', makeOverListener(map, placeMarker, customOverlay));
    kakao.maps.event.addListener(placeMarker, 'mouseout', makeOutListener(customOverlay));
    kakao.maps.event.addListener(customOverlay, 'mouseover', makeOverListener(map, placeMarker, customOverlay));
    kakao.maps.event.addListener(customOverlay, 'mouseout', makeOutListener(customOverlay));

}
function centerMarkerHandler() {
    const regionCodeValue = document.querySelector("#region").value;
    if (regionCodeValue === '지역 선택'){
        swal(
            'Warning!',
            '지역을 선택해주세요!',
            'warning'
        )
        return;
    }
    let latitude;
    let longitude;
    switch(regionCodeValue) {
        case '경기도':
            latitude = 37.2749769872425;
            longitude = 127.00892996953;
            break;
        case '전라남도':
            latitude = 34.8160821478848
            longitude = 126.462788333376
            break;
        case '서울특별시':
            latitude = 37.566826004661
            longitude = 126.978652258309
            break;
        case '인천광역시':
            latitude = 37.4560044656444
            longitude = 126.705258070068
            break;
        case '경상북도':
            latitude = 36.5759962255808
            longitude = 128.505799255401
            break;
        case '대구광역시':
            latitude = 35.8713802646197
            longitude = 128.601805491072
            break;
        case '세종특별자치시':
            latitude = 36.4800649113762
            longitude = 127.289195324698
            break;
        case '전북특별자치도':
            latitude = 35.8201963639272
            longitude = 127.108976712011
            break;
        case '경상남도':
            latitude = 35.2377742104522
            longitude = 128.69189688916
            break;
        case '강원특별자치도':
            latitude = 37.8853257858209
            longitude = 127.729829010354
            break;
        case '부산광역시':
            latitude = 35.1798200522868
            longitude = 129.075087492149
            break;
        case '광주광역시':
            latitude = 35.1601037626662
            longitude = 126.851629955742
            break;
        case '대전광역시':
            latitude = 36.3505388992836
            longitude = 127.38483484675
            break;
        case '충청북도':
            latitude = 36.6353581959954
            longitude = 127.491457326501
            break;
        case '제주특별자치도':
            latitude = 33.4889179032603
            longitude = 126.498229141199
            break;
        case '충청남도':
            latitude = 36.6588292532864
            longitude = 126.672776193822
            break;
        case '울산광역시':
            latitude = 35.5395955247058
            longitude = 129.311603446508
            break;
        case '전라북도':
            latitude = 35.8201963639272
            longitude = 127.108976712011
            break;
        default:
            break;
    }

    let moveLatLon = new kakao.maps.LatLng(latitude,longitude);
    kakaoMap.setCenter(moveLatLon);
    kakaoMap.setLevel(10);
}